/*
 * $Id: CombinedDispersionDataImpl.java,v 1.6 2009-03-16 16:38:11 kfuchsbe Exp $
 *
 * $Date: 2009-03-16 16:38:11 $
 * $Revision: 1.6 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.meas.data;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.meas.data.DynamicDataListener;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurementImpl;
import cern.accsoft.steering.jmad.util.MatrixUtil;
import cern.accsoft.steering.util.meas.data.Plane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class provides data, which is calculated as a combination of
 * measurement- and model-data
 *
 * @author kfuchsbe
 */
public class CombinedDispersionDataImpl implements CombinedDispersionData, NoiseWeighterAware {
    private final static Logger LOGGER = LoggerFactory.getLogger(CombinedDispersionDataImpl.class);

    /**
     * indicates, if the data has to be recalculated
     */
    private boolean dirty = true;

    /**
     * the map which contains the calculated values
     */
    private Map<String, List<Double>> valuesMap = new HashMap<>();

    /**
     * the listeners, which can be added to this class
     */
    private List<DynamicDataListener> listeners = new ArrayList<>();

    /**
     * the object, which correctly handles calculating noisy values
     */
    private NoiseWeighter noiseWeighter;

    /**
     * the dispersion measurement, this combined data belongs to
     */
    private DispersionMeasurementImpl dispersionMeasurement;

    /**
     * this enum just defines prefixes for the hashmap-keys
     *
     * @author kfuchsbe
     */
    public enum KeyPrefix {
        NOISY_DIFFERENCE, NORMALIZED_DIFFERENCE, NORMALIZED_RMS;
    }

    /**
     * the listener, which we add to both, the model data and the
     * measurement-dispersion data, in order to get notified about changes.
     */
    private DynamicDataListener dataListener = new DynamicDataListener() {
        @Override
        public void becameDirty() {
            setDirty(true);
        }
    };

    /**
     * create a key for the hashmap
     *
     * @param prefix the prefix to use
     * @param plane  the plane
     * @return the key
     */
    private String createKey(KeyPrefix prefix, Plane plane) {
        return prefix + "-" + plane;
    }

    /**
     * @param dirty the value, which to set the dirty-flag
     */
    private void setDirty(boolean dirty) {
        this.dirty = dirty;
        if (this.dirty) {
            fireBecameDirty();
        }
    }

    /**
     * @return the dirty-flag
     */
    private boolean isDirty() {
        return this.dirty;
    }

    /**
     * ensures, that the data is up to date.
     */
    private void ensureUpToDate() {
        if (isDirty()) {
            calc();
        }
    }

    /**
     * (re) calculates all the values
     */
    private void calc() {
        for (Plane plane : Plane.values()) {
            calcData(plane);
        }
        setDirty(false);

        double fitQuality = calcFitQuality();
        LOGGER.info("norm of relative dispersion-vector / sqrt(#elements - 1) = " + fitQuality);
    }

    /**
     * calculates the data for one plane
     *
     * @param plane the plane for which to calculate the data
     * @return the data
     */
    private void calcData(Plane plane) {
        List<Double> diffData = new ArrayList<>();
        List<Double> normalizeDiffData = new ArrayList<>();
        List<Double> normalizedRmsData = new ArrayList<>();

        DispersionData dispersionData = getDispersionMeasurement().getData();
        if (dispersionData != null) {
            List<Double> dispValuesMeas = dispersionData.getValues(plane);
            List<Double> dispRmsMeas = dispersionData.getRms(plane);
            List<Double> dispValuesModel = getModelOpticsData()
                    .getMonitorDispersions(plane);
            List<Double> betas = getModelOpticsData().getMonitorBetas(plane);

            for (int i = 0; i < dispValuesMeas.size(); i++) {
                double value = (dispValuesMeas.get(i) - dispValuesModel.get(i));
                double sqrtBeta = Math.sqrt(betas.get(i));
                diffData.add(getNoiseWeighter().calcNoisyValue(value,
                        dispRmsMeas.get(i)));
                normalizeDiffData.add(value / sqrtBeta);
                normalizedRmsData.add(dispRmsMeas.get(i) / sqrtBeta);
            }
        }
        this.valuesMap.put(createKey(KeyPrefix.NOISY_DIFFERENCE, plane),
                diffData);
        this.valuesMap.put(createKey(KeyPrefix.NORMALIZED_DIFFERENCE, plane),
                normalizeDiffData);
        this.valuesMap.put(createKey(KeyPrefix.NORMALIZED_RMS, plane),
                normalizedRmsData);
    }

    /**
     * @return the fit quality
     */
    private double calcFitQuality() {
        if (getDispersionMeasurement().getData() == null) {
            return 0.0;
        }

        List<Boolean> validity = getDispersionMeasurement().getData()
                .getValidity();

        /* count the elements */
        int elementsCount = 0;
        for (Boolean bValue : validity) {
            if (bValue.equals(true)) {
                elementsCount++;
            }
        }

        return getNoisyDifferenceVector().normF()
                / ((elementsCount > 1) ? Math.sqrt(elementsCount - 1) : 1);
    }

    @Override
    public List<Double> getMonitorNormalizedDispersionDiff(Plane plane) {
        ensureUpToDate();
        return this.valuesMap.get(createKey(KeyPrefix.NORMALIZED_DIFFERENCE,
                plane));
    }

    /**
     * notify all listeners, that the data has changed
     */
    private void fireBecameDirty() {
        for (DynamicDataListener listener : listeners) {
            listener.becameDirty();
        }
    }

    @Override
    public void addListener(DynamicDataListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(DynamicDataListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * @return the modelOpticsData
     */
    private ModelOpticsData getModelOpticsData() {
        return getDispersionMeasurement().getModelDelegate()
                .getModelOpticsData();
    }

    @Override
    public Matrix getNoisyDifferenceVector() {
        ensureUpToDate();

        List<Double> values = new ArrayList<>();
        values.addAll(getNoisyMonitorDispersionDiff(Plane.HORIZONTAL));
        values.addAll(getNoisyMonitorDispersionDiff(Plane.VERTICAL));
        return MatrixUtil.createVector(values);
    }

    @Override
    public List<Double> getNoisyMonitorDispersionDiff(Plane plane) {
        ensureUpToDate();
        return this.valuesMap.get(createKey(KeyPrefix.NOISY_DIFFERENCE, plane));
    }

    /**
     * @param noiseWeighter the noiseWeighter to set
     */
    @Override
    public void setNoiseWeighter(NoiseWeighter noiseWeighter) {
        this.noiseWeighter = noiseWeighter;
    }

    /**
     * @return the noiseWeighter
     */
    public NoiseWeighter getNoiseWeighter() {
        return noiseWeighter;
    }

    @Override
    public List<Double> getMonitorNormalizedDispersionRms(Plane plane) {
        ensureUpToDate();
        return this.valuesMap.get(createKey(KeyPrefix.NORMALIZED_RMS, plane));
    }

    public void setDispersionMeasurement(
            DispersionMeasurementImpl dispersionMeasurement) {
        this.dispersionMeasurement = dispersionMeasurement;
        dispersionMeasurement.getData().addListener(dataListener);
        dispersionMeasurement.getModelDelegate().getModelOpticsData()
                .addListener(dataListener);
    }

    private DispersionMeasurementImpl getDispersionMeasurement() {
        return dispersionMeasurement;
    }

}
