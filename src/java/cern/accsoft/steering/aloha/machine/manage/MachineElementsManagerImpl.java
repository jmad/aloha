/*
 * $Id: MachineElementsManagerImpl.java,v 1.3 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.machine.manage;

import cern.accsoft.steering.aloha.conf.MonitorSelection;
import cern.accsoft.steering.aloha.machine.AbstractMachineElement;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.MachineElementListener;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.elem.JMadElementType;
import cern.accsoft.steering.jmad.domain.elem.MadxElementType;
import cern.accsoft.steering.jmad.gui.mark.MarkerXProvider;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.jmad.util.ListUtil.Mapper;
import cern.accsoft.steering.util.meas.data.DataValue;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.Status;
import cern.accsoft.steering.util.meas.data.yasp.MeasuredData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * the basic implementation of a class, that keeps track of active monitors and correctors.
 * 
 * @author kfuchsbe
 */
public class MachineElementsManagerImpl implements MachineElementsManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(MachineElementsManagerImpl.class);

    /** the list of all correctors */
    private List<Corrector> correctors = new ArrayList<>();

    /** the list of all monitors */
    private List<Monitor> monitors = new ArrayList<>();

    /** the listeners to this class */
    private List<MachineElementsManagerListener> listeners = new ArrayList<>();

    /* the actual corrector/monitor, which is displayed at the moment. */
    private int activeCorrectorNumber = 0;
    private int activeMonitorNumber = 0;

    /** indicates, if the element-lists are filled */
    private boolean filled = false;

    /**
     * if true, then no event is sent, if one of the elements changes its active state.
     */
    private boolean suppressActiveElementsChangedEvent = false;

    /**
     * if true then no event is sent if one of the monitor-gains is changed.
     */
    private boolean suppressChangedMonitorGainsEvent = false;

    /**
     * if true then no event is sent if one of the corrector-gains is changed.
     */
    private boolean suppressChangedCorrectorGainsEvent = false;

    /**
     * the marker-provider which indicates the border between H and V- plane
     */
    private MarkerXProvider monitorHVBorderProvider = new MarkerXProvider() {
        @Override
        public List<Double> getXPositions(String elementName) {
            List<Double> xValues = new ArrayList<>();
            if (MarkerXProvider.ELEMENT_NAME_HV_BORDER.equals(elementName)) {
                int allMonitorsCount = getActiveMonitorsCount();
                int horMonitorsCount = getActiveMonitorsCount(Plane.HORIZONTAL);
                if (allMonitorsCount > horMonitorsCount) {
                    xValues.add(horMonitorsCount - 0.5);
                }
            }
            return xValues;
        }
    };

    /**
     * the provider which indicates the border between H and V- plane
     */
    private MarkerXProvider correctorHVBorderProvider = new MarkerXProvider() {
        @Override
        public List<Double> getXPositions(String elementName) {
            List<Double> xValues = new ArrayList<>();
            if (MarkerXProvider.ELEMENT_NAME_HV_BORDER.equals(elementName)) {
                int allCorrectorsCount = getActiveCorrectorsCount();
                int horCorrectorsCount = getActiveCorrectorsCount(Plane.HORIZONTAL);
                if (allCorrectorsCount > horCorrectorsCount) {
                    xValues.add(horCorrectorsCount - 0.5);
                }
            }
            return xValues;
        }
    };

    /**
     * This is the instance of the listener, which we attach to all elements, in order to keep track of selection
     * changes.
     */
    private final MachineElementListener machineElementListener = new MachineElementListener() {
        @Override
        public void changedActiveState(AbstractMachineElement element) {
            fireChangedActiveElements();
        }

        @Override
        public void changedGain(AbstractMachineElement element) {
            if (element instanceof Corrector) {
                fireChangedCorrectorGains();
            } else if (element instanceof Monitor) {
                fireChangedMonitorGains();
            }
        }

    };

    /**
     * sets all the fields to the initial values
     */
    @Override
    public void clear() {
        this.activeCorrectorNumber = 0;
        this.activeMonitorNumber = 0;
        this.correctors.clear();
        this.monitors.clear();
        this.filled = false;
    }

    @Override
    public void fill(JMadModel model) {
        clear();
        /*
         * first we look for all correctors in both planes
         */
        List<Element> modelCorrectors = model.getActiveRange().getElements(JMadElementType.CORRECTOR);
        for (Plane plane : Plane.values()) {
            for (Element modelCorrector : modelCorrectors) {
                Plane typePlane = getPlaneFromMadxElementType(modelCorrector.getMadxElementType());

                /*
                 * if the plane from the type is given and it does not match, then we skip the element.
                 */
                if ((typePlane != null) && (!plane.equals(typePlane))) {
                    continue;
                }
                Corrector corrector = new Corrector(modelCorrector.getName(), plane);
                /*
                 * for correctors we set the status to NOT_OK, since it makes no sense to assume use them, if we have no
                 * measurements (which is most likely for most correctors)
                 */
                corrector.setStatus(Status.NOT_OK);
                this.correctors.add(corrector);
            }
        }

        /*
         * then for all monitors
         */
        List<Element> modelMonitors = model.getActiveRange().getElements(JMadElementType.MONITOR);
        for (Plane plane : Plane.values()) {
            for (Element modelMonitor : modelMonitors) {
                Plane typePlane = getPlaneFromMadxElementType(modelMonitor.getMadxElementType());

                /*
                 * if the plane from the type is given and it does not match, then we skip the element.
                 */
                if ((typePlane != null) && (!plane.equals(typePlane))) {
                    continue;
                }
                Monitor monitor = new Monitor(modelMonitor.getName(), plane);
                this.monitors.add(monitor);
            }
        }

        registerListener(this.correctors);
        registerListener(this.monitors);
        this.filled = true;
        fireChangedElements();
    }

    @Override
    public <T extends DataValue> void deactivateUnavailableMonitors(Collection<? extends MeasuredData<T>> readingDatas) {
        boolean oldSuppress = this.suppressActiveElementsChangedEvent;
        setSuppressActiveElementsChangedEvent(true);
        for (Monitor monitor : getAllMonitors()) {
            for (MeasuredData<T> readingData : readingDatas) {
                DataValue monitorValue = readingData.getMonitorValue(monitor.getKey());
                if (monitorValue != null) {
                    if (!Status.OK.equals(monitorValue.getStatus())) {
                        LOGGER.debug("Found bad monitor status for monitor '" + monitor.getName()
                                + "' in at least one file");
                        monitor.setStatus(Status.NOT_OK);
                        break;
                    }
                } else {
                    LOGGER.debug("Setting status for monitor '" + monitor.getName()
                            + "' to NOT_OK, because not entries in all datas available.");
                    monitor.setStatus(Status.NOT_OK);
                    /*
                     * We do not deactivate monitors that are found in the data, but with bad status. They shall be
                     * visible!
                     */
                    monitor.setActive(false);
                    break;
                }
            }
        }
        setSuppressActiveElementsChangedEvent(oldSuppress);
    }

    @Override
    public ArrayList<String> getActiveMonitorNames() {
        ArrayList<String> names = new ArrayList<>();
        for (AbstractMachineElement monitor : getActiveMonitors()) {
            names.add(monitor.getName());
        }
        return names;
    }

    @Override
    public int getActiveMonitorsCount() {
        return getActiveMonitors().size();
    }

    @Override
    public int getActiveCorrectorsCount() {
        return getActiveCorrectors().size();
    }

    @Override
    public ArrayList<String> getActiveCorrectorNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Corrector corrector : getActiveCorrectors()) {
            names.add(corrector.getName());
        }
        return names;
    }

    @Override
    public List<Monitor> getActiveMonitors() {
        ArrayList<Monitor> workingMonitors = new ArrayList<>();
        List<Monitor> allMonitors = getAllMonitors();

        for (Monitor monitor : allMonitors) {
            if (monitor.isActive()) {
                workingMonitors.add(monitor);
            }
        }
        return workingMonitors;
    }

    @Override
    public List<Corrector> getActiveCorrectors() {
        ArrayList<Corrector> activeCorrectors = new ArrayList<>();
        List<Corrector> allCorrectors = getAllCorrectors();

        for (Corrector corrector : allCorrectors) {
            if (corrector.isActive()) {
                activeCorrectors.add(corrector);
            }
        }
        return activeCorrectors;
    }

    @Override
    public void addListener(MachineElementsManagerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(MachineElementsManagerListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * notifies all listeners, that the active elements have changed
     */
    private void fireChangedActiveElements() {
        if (isSuppressActiveElementsChangedEvent()) {
            return;
        }

        for (MachineElementsManagerListener listener : this.listeners) {
            listener.changedActiveElements();
        }
    }

    /**
     * notifies all listeners that corrector gains have changed
     */
    private void fireChangedCorrectorGains() {
        if (isSuppressChangedCorrectorGainsEvent()) {
            return;
        }

        for (MachineElementsManagerListener listener : this.listeners) {
            listener.changedCorrectorGains();
        }
    }

    /**
     * notifies all listeners that monitor gains have changed
     */
    private void fireChangedMonitorGains() {
        if (isSuppressChangedMonitorGainsEvent()) {
            return;
        }

        for (MachineElementsManagerListener listener : this.listeners) {
            listener.changedMonitorGains();
        }
    }

    private void fireChangedElements() {
        for (MachineElementsManagerListener listener : this.listeners) {
            listener.changedElements();
        }
    }

    private void registerListener(List<? extends AbstractMachineElement> elements) {
        for (AbstractMachineElement element : elements) {
            element.addListener(this.machineElementListener);
        }
    }

    @Override
    public List<Corrector> getAllCorrectors() {
        return correctors;
    }

    @Override
    public List<Monitor> getAllMonitors() {
        return monitors;
    }

    @Override
    public List<String> getActiveCorrectorNames(Plane plane) {
        ArrayList<String> names = new ArrayList<>();
        for (AbstractMachineElement corrector : getActiveCorrectors()) {
            if (plane.equals(corrector.getPlane())) {
                names.add(corrector.getName());
            }
        }
        return names;
    }

    @Override
    public List<Corrector> getActiveCorrectors(Plane plane) {
        List<Corrector> allCorrectors = new ArrayList<>();
        for (Corrector corrector : getActiveCorrectors()) {
            if (plane.equals(corrector.getPlane())) {
                allCorrectors.add(corrector);
            }
        }
        return allCorrectors;
    }

    @Override
    public List<String> getActiveMonitorNames(Plane plane) {
        ArrayList<String> names = new ArrayList<>();
        for (AbstractMachineElement monitor : getActiveMonitors()) {
            if (plane.equals(monitor.getPlane())) {
                names.add(monitor.getName());
            }
        }
        return names;
    }

    @Override
    public List<Monitor> getActiveMonitors(Plane plane) {
        List<Monitor> allMonitors = new ArrayList<>();
        for (Monitor monitor : getActiveMonitors()) {
            if (plane.equals(monitor.getPlane())) {
                allMonitors.add(monitor);
            }
        }
        return allMonitors;
    }

    @Override
    public int getActiveCorrectorsCount(Plane plane) {
        return getActiveCorrectorNames(plane).size();
    }

    @Override
    public int getActiveMonitorsCount(Plane plane) {
        return getActiveMonitorNames(plane).size();
    }

    void fireChangedActiveCorrector() {
        if ((activeCorrectorNumber < 0) || (activeCorrectorNumber >= getActiveCorrectors().size())) {
            return;
        }
        Corrector corrector = getActiveCorrectors().get(activeCorrectorNumber);
        for (MachineElementsManagerListener listener : listeners) {
            listener.changedActiveCorrector(activeCorrectorNumber, corrector);
        }
    }

    void fireChangedActiveMonitor() {
        if ((activeMonitorNumber < 0) || (activeMonitorNumber >= getActiveMonitors().size())) {
            return;
        }
        Monitor monitor = getActiveMonitors().get(activeMonitorNumber);
        for (MachineElementsManagerListener listener : listeners) {
            listener.changedActiveMonitor(activeMonitorNumber, monitor);
        }
    }

    @Override
    public void setActiveCorrectorNumber(int activeCorrectorNumber) {
        this.activeCorrectorNumber = activeCorrectorNumber;
        fireChangedActiveCorrector();
    }

    @Override
    public void setActiveMonitorNumber(int activeMonitorNumber) {
        this.activeMonitorNumber = activeMonitorNumber;
        fireChangedActiveMonitor();
    }

    @Override
    public int getActiveCorrectorNumber() {
        return activeCorrectorNumber;
    }

    @Override
    public int getActiveMonitorNumber() {
        return activeMonitorNumber;
    }

    @Override
    public MarkerXProvider getMonitorHVBorderProvider() {
        return monitorHVBorderProvider;
    }

    @Override
    public MarkerXProvider getCorrectorHVBorderProvider() {
        return correctorHVBorderProvider;
    }

    public boolean isFilled() {
        return filled;
    }

    @Override
    public Integer convertToActiveCorrectorIndex(int allCorrectorIndex) {
        Corrector corrector = getAllCorrectors().get(allCorrectorIndex);
        if (!corrector.isActive()) {
            return null;
        }

        int count = 0;
        for (Corrector corr : getActiveCorrectors()) {
            if (corr.equals(corrector)) {
                return count;
            }
            count++;
        }

        return null;
    }

    @Override
    public Integer convertToActiveMonitorIndex(int allMonitorIndex) {
        Monitor monitor = getAllMonitors().get(allMonitorIndex);
        if (!monitor.isActive()) {
            return null;
        }

        int count = 0;
        for (Monitor mon : getActiveMonitors()) {
            if (mon.equals(monitor)) {
                return count;
            }
            count++;
        }

        return null;
    }

    @Override
    public void setSuppressActiveElementsChangedEvent(boolean suppressActiveElementsChangedEvent) {
        this.suppressActiveElementsChangedEvent = suppressActiveElementsChangedEvent;
        /*
         * when set back to false, then we send one common event.
         */
        if (!this.suppressActiveElementsChangedEvent) {
            fireChangedActiveElements();
        }
    }

    private boolean isSuppressActiveElementsChangedEvent() {
        return suppressActiveElementsChangedEvent;
    }

    @Override
    public void setSuppressChangedMonitorGainsEvent(boolean suppressChangedMonitorGainsEvent) {
        this.suppressChangedMonitorGainsEvent = suppressChangedMonitorGainsEvent;

        /*
         * when set back to false, then we send one common event.
         */
        if (!this.suppressChangedMonitorGainsEvent) {
            fireChangedMonitorGains();
        }
    }

    private boolean isSuppressChangedMonitorGainsEvent() {
        return suppressChangedMonitorGainsEvent;
    }

    @Override
    public void setSuppressChangedCorrectorGainsEvent(boolean suppressChangedCorrectorGainsEvent) {
        this.suppressChangedCorrectorGainsEvent = suppressChangedCorrectorGainsEvent;

        /*
         * when set back to false, then we send one common event.
         */
        if (!this.suppressChangedCorrectorGainsEvent) {
            fireChangedCorrectorGains();
        }
    }

    private boolean isSuppressChangedCorrectorGainsEvent() {
        return suppressChangedCorrectorGainsEvent;
    }

    @Override
    public List<Double> getActiveCorrectorGains() {
        return getGains(getActiveCorrectors());
    }

    @Override
    public List<Double> getActiveMonitorGains() {
        return getGains(getActiveMonitors());
    }

    @Override
    public List<Double> getActiveCorrectorGainErrors() {
        return getGainErrors(getActiveCorrectors());
    }

    @Override
    public List<Double> getActiveMonitorGainErrors() {
        return getGainErrors(getActiveMonitors());
    }

    private <T extends AbstractMachineElement> List<Double> getGains(List<T> elements) {
        return ListUtil.map(elements, new Mapper<T, Double>() {
            @Override
            public Double map(T element) {
                return element.getGain();
            }
        });
    }

    private <T extends AbstractMachineElement> List<Double> getGainErrors(List<T> elements) {
        return ListUtil.map(elements, new Mapper<T, Double>() {
            @Override
            public Double map(T element) {
                return element.getGainError();
            }
        });
    }

    @Override
    public void resetAllGains() {
        setSuppressChangedCorrectorGainsEvent(true);
        resetGains(getAllCorrectors());
        setSuppressChangedCorrectorGainsEvent(false);

        setSuppressChangedMonitorGainsEvent(true);
        resetGains(getAllMonitors());
        setSuppressChangedMonitorGainsEvent(false);
    }

    private <T extends AbstractMachineElement> void resetGains(List<T> elements) {
        for (AbstractMachineElement element : elements) {
            element.resetGain();
        }
    }

    @Override
    public synchronized void apply(MonitorSelection monitorSelection) {
        /* do not notify listeners */
        this.setSuppressActiveElementsChangedEvent(true);

        Set<String> keys = monitorSelection.getActiveKeys();
        for (Monitor monitor : getAllMonitors()) {
            if (keys.contains(monitor.getKey())) {
                monitor.setActive(true);
            } else {
                monitor.setActive(false);
            }
        }

        /* no notify listeners */
        this.setSuppressActiveElementsChangedEvent(false);
    }

    @Override
    public synchronized MonitorSelection getActiveMonitorSelection() {
        MonitorSelection monitorSelection = new MonitorSelection();
        for (Monitor monitor : getActiveMonitors()) {
            monitorSelection.addActiveKey(monitor.getKey());
        }
        return monitorSelection;
    }

    /**
     * returns the plane which corresponds to the given madx-type, if it is definite.
     * 
     * @param type The madx-element type for which to get the plane
     * @return the plane, or <code>null</code> if the plane is undefined
     */
    private final static Plane getPlaneFromMadxElementType(MadxElementType type) {
        if (MadxElementType.HKICKER.equals(type) || MadxElementType.HMONITOR.equals(type)) {
            return Plane.HORIZONTAL;
        } else if (MadxElementType.VKICKER.equals(type) || MadxElementType.VMONITOR.equals(type)) {
            return Plane.VERTICAL;
        } else {
            return null;
        }
    }

}
