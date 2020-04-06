/*
 * $Id: MadXModelOpticsData.java,v 1.4 2009-02-25 18:48:43 kfuchsbe Exp $
 *
 * $Date: 2009-02-25 18:48:43 $
 * $Revision: 1.4 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.model.data;

import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.data.AbstractDynamicData;
import cern.accsoft.steering.aloha.model.JMadModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateListener;
import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.domain.optics.Optic;
import cern.accsoft.steering.jmad.domain.optics.OpticPoint;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.util.meas.data.Plane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this is the basic implementation of a class, that provides data from the
 * model.
 * <p>
 * NOTE: The actuak monitor gains are taken into account only for the lists for
 * the monitors only. The lists for all values contain the values as retrieved
 * from the model.
 *
 * @author kfuchsbe
 */
public class JMadModelOpticsData extends AbstractDynamicData implements ModelOpticsData {
    private final static Logger LOGGER = LoggerFactory.getLogger(JMadModelOpticsData.class);

    /**
     * the model-delegate
     */
    private ModelDelegate modelDelegate;

    /**
     * the buffered dispersion values for the monitors
     */
    private Map<String, List<Double>> monitorValuesMap = new HashMap<>();

    /**
     * the buffered dispersion values for all elements
     */
    private List<String> allNames = new ArrayList<>();

    public JMadModelOpticsData(ModelDelegate modelDelegate,
                               MachineElementsManager machineElementsManager) {
        setMachineElementsManager(machineElementsManager);
        setModelDelegate(modelDelegate);
    }

    /**
     * this enum just serves as prefix for the keys
     *
     * @author kfuchsbe
     */
    private enum KeyPrefix {
        DISPERSION, BETA, PHASE, S_POSITION, POS;
    }

    /**
     * a prefix, which list to use, the one with values for all elements, or the
     * one for monitors only.
     *
     * @author kfuchsbe
     */
    private enum ListPrefix {
        ALL, MONITORS;
    }

    /**
     * adds one value to a certain list, determined by prefix and plane
     *
     * @param listPrefix the kind of data, where to set the value
     * @param plane  the plane for which to set the value
     * @param value  the value to set
     */
    private void addValue(ListPrefix listPrefix, KeyPrefix keyPrefix,
                          Plane plane, Double value) {
        getValueList(listPrefix, keyPrefix, plane).add(value);
    }

    /**
     * puts a given List to the map
     *
     * @param listPrefix    the kind of list to put
     * @param plane     the plane for which to put the list
     * @param valueList the list to put
     */
    private void putValueList(ListPrefix listPrefix, KeyPrefix keyPrefix,
                              Plane plane, List<Double> valueList) {
        this.monitorValuesMap.put(createKey(listPrefix, keyPrefix, plane),
                valueList);
    }

    /**
     * @param listPrefix the kind of data to retrieve
     * @param plane  the plane for which to get the date
     * @return the list of values
     */
    private List<Double> getValueList(ListPrefix listPrefix,
                                      KeyPrefix keyPrefix, Plane plane) {
        return this.monitorValuesMap
                .get(createKey(listPrefix, keyPrefix, plane));
    }

    /**
     * creates a unique key out of prefix and plane. plane may be null.
     *
     * @param listPrefix
     * @param plane
     * @return the key for the map
     */
    private String createKey(ListPrefix listPrefix, KeyPrefix keyPrefix,
                             Plane plane) {
        if (plane != null) {
            return listPrefix + "-" + keyPrefix + "-" + plane;
        } else {
            return listPrefix + "-" + keyPrefix;
        }
    }

    /**
     * recalculates the data
     */
    @Override
    protected void calc() {
        JMadModel model = getModel();
        if (model == null) {
            return;
        }

        /* first we set the lists to fresh ones */
        allNames.clear();
        for (ListPrefix listPrefix : ListPrefix.values()) {
            for (KeyPrefix keyPrefix : KeyPrefix.values()) {
                if (KeyPrefix.S_POSITION.equals(keyPrefix)) {
                    /* position is stored only once for both planes. */
                    putValueList(listPrefix, keyPrefix, null,
							new ArrayList<>());
                } else {
                    for (Plane plane : Plane.values()) {
                        putValueList(listPrefix, keyPrefix, plane,
								new ArrayList<>());
                    }
                }
            }
        }

        /* first we cache the values for all optics-points */
        for (OpticPoint point : getOptics().getAllPoints()) {
            allNames.add(point.getName());
            for (Plane plane : Plane.values()) {
                /*
                 * we do not take into account the monitor-gains, when reading
                 * all values (=1)
                 */
                addPoint(ListPrefix.ALL, point, plane, 1);
            }
        }

        /* then the positions for all elements */
        List<Element> elements = getElements();
        if (elements != null) {
            for (Element element : elements) {
                addValue(ListPrefix.ALL, KeyPrefix.S_POSITION, null, element
                        .getPosition().getValue());
            }
        }

        /* then we cycle through the active monitors */
        List<Monitor> monitors = getActiveMonitors();
        int monitorNumber = 0;
        for (Monitor monitor : monitors) {
            /* optics values */
            OpticPoint point = getOpticsPoint(monitor.getName());
            Plane plane = monitor.getPlane();
            if (point == null) {
                for (KeyPrefix keyPrefix : KeyPrefix.values()) {
                    addValue(ListPrefix.MONITORS, keyPrefix, plane, null);
                }
            } else {
                double monitorGain = monitor.getGain();
                addPoint(ListPrefix.MONITORS, point, plane, monitorGain);
            }

            /* position */
            Element element = getElement(monitor.getName());
            if (element == null) {
                addValue(ListPrefix.MONITORS, KeyPrefix.S_POSITION, null, null);
            } else {
                addValue(ListPrefix.MONITORS, KeyPrefix.S_POSITION, null,
                        element.getPosition().getValue());
            }

            monitorNumber++;
        }
    }

    /**
     * This adds a point for all optic values. The given monitor Gain is applies
     * to the dispersion and the position values.
     *
     * @param point
     * @param plane
     * @param monitorGain
     */
    private void addPoint(ListPrefix listPrefix, OpticPoint point, Plane plane,
                          double monitorGain) {
        if (Plane.HORIZONTAL.equals(plane)) {
            addValue(listPrefix, KeyPrefix.DISPERSION, plane, point.getDx()
                    * monitorGain);
            addValue(listPrefix, KeyPrefix.BETA, plane, point.getBetx());
            addValue(listPrefix, KeyPrefix.PHASE, plane, point.getMux());
            addValue(listPrefix, KeyPrefix.POS, plane, point.getX()
                    * monitorGain);
        } else if (Plane.VERTICAL.equals(plane)) {
            addValue(listPrefix, KeyPrefix.DISPERSION, plane, point.getDy()
                    * monitorGain);
            addValue(listPrefix, KeyPrefix.BETA, plane, point.getBety());
            addValue(listPrefix, KeyPrefix.PHASE, plane, point.getMuy());
            addValue(listPrefix, KeyPrefix.POS, plane, point.getY()
                    * monitorGain);
        } else {
            LOGGER.warn("unknown plane '" + plane + "'");
        }
    }

    /**
     * retrieves the element from the model.
     *
     * @param elementName the name of the element to retrieve
     * @return the element
     */
    private Element getElement(String elementName) {
        if (getModel() == null) {
            return null;
        }
        return getModel().getActiveRange().getElement(elementName);
    }

    /**
     * @return all elements
     */
    private List<Element> getElements() {
        if (getModel() == null) {
            return null;
        }
        return getModel().getActiveRange().getElements();
    }

    /**
     * returns the opticsPoint for the given element
     *
     * @param elementName the name of the element for which to retrieve the opticsPoint
     * @return the OpticsPoint
     */
    private OpticPoint getOpticsPoint(String elementName) {
        Optic optics = getOptics();

        OpticPoint point = optics.getPointByName(elementName);
        if (point == null) {
            LOGGER.warn("No optics point for Element '" + elementName
                    + "' found in the model!");
        }
        return point;
    }

    /**
     * @return the actual optics
     */
    private Optic getOptics() {
        JMadModel model = getModel();
        if (model == null) {
            return null;
        }

        Optic optics = null;
        try {
            optics = model.getOptics();
        } catch (JMadModelException e) {
            LOGGER.error("Error while retrieving optics from model.", e);
        }
        return optics;
    }

    /**
     * @return the actual model
     */
    private JMadModel getModel() {
        ModelDelegate delegate = getModelDelegate();
        if (delegate instanceof JMadModelDelegate) {
            return delegate.getJMadModel();
        }
        return null;
    }

    @Override
    public List<Double> getMonitorDispersions(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.MONITORS, KeyPrefix.DISPERSION, plane);
    }

    @Override
    public List<Double> getMonitorBetas(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.MONITORS, KeyPrefix.BETA, plane);
    }

    @Override
    public List<Double> getMonitorBetas() {
        List<Double> betas = new ArrayList<>(
				getMonitorBetas(Plane.HORIZONTAL));
        betas.addAll(getMonitorBetas(Plane.VERTICAL));
        return betas;
    }

    @Override
    public List<Double> getMonitorPhases(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.MONITORS, KeyPrefix.PHASE, plane);
    }

    /**
     * @param modelDelegate the modelDelegate to set
     */
    private void setModelDelegate(ModelDelegate modelDelegate) {
        this.modelDelegate = modelDelegate;
        this.modelDelegate.addListener(new ModelDelegateListener() {

            @Override
            public void becameDirty() {
                setDirty(true);
            }
        });
    }

    /**
     * @return the modelDelegate
     */
    private ModelDelegate getModelDelegate() {
        return modelDelegate;
    }

    @Override
    public List<Double> getMonitorDispersions() {
        List<Double> values = new ArrayList<>(
				getMonitorDispersions(Plane.HORIZONTAL));
        values.addAll(getMonitorDispersions(Plane.VERTICAL));
        return values;
    }

    @Override
    public List<Double> getAllBetas(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.ALL, KeyPrefix.BETA, plane);
    }

    @Override
    public List<Double> getAllDispersions(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.ALL, KeyPrefix.DISPERSION, plane);
    }

    @Override
    public List<Double> getAllPhases(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.ALL, KeyPrefix.PHASE, plane);
    }

    @Override
    public List<String> getAllNames() {
        ensureUpToDate();
        return this.allNames;
    }

    @Override
    public Double getPhase(String elementName, Plane plane) {
        OpticPoint point = getOpticsPoint(elementName);

        if (point == null) {
            return null;
        }

        if (Plane.HORIZONTAL.equals(plane)) {
            return point.getMux();
        } else if (Plane.VERTICAL.equals(plane)) {
            return point.getMuy();
        } else {
            return null;
        }
    }

    @Override
    public List<Double> getAllSPositions() {
        ensureUpToDate();
        return getValueList(ListPrefix.ALL, KeyPrefix.S_POSITION, null);
    }

    @Override
    public Double getSPosition(String elementName) {
        Element element = getElement(elementName);
        if (element != null) {
            return element.getPosition().getValue();
        } else {
            return null;
        }
    }

    @Override
    public List<Double> getAllPos(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.ALL, KeyPrefix.POS, plane);
    }

    @Override
    public List<Double> getMonitorPos(Plane plane) {
        ensureUpToDate();
        return getValueList(ListPrefix.MONITORS, KeyPrefix.POS, plane);
    }

    @Override
    public List<Double> getMonitorPos() {
        List<Double> values = new ArrayList<>(
				getMonitorPos(Plane.HORIZONTAL));
        values.addAll(getMonitorPos(Plane.VERTICAL));
        return values;
    }

    @Override
    public List<Double> getMonitorSPositions(Plane plane) {
        List<Monitor> monitors = getMachineElementsManager().getActiveMonitors(
                plane);
        List<Double> sPositions = new ArrayList<>();
        for (Monitor monitor : monitors) {
            sPositions.add(getSPosition(monitor.getName()));
        }
        return sPositions;
    }

}
