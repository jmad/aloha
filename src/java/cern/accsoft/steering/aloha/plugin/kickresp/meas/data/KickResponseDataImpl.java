/*
 * $Id: KickResponseDataImpl.java,v 1.4 2009-03-16 16:38:12 kfuchsbe Exp $
 *
 * $Date: 2009-03-16 16:38:12 $ $Revision: 1.4 $ $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Jama.Matrix;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.meas.data.AbstractDynamicData;
import cern.accsoft.steering.aloha.meas.data.InconsistentDataException;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;
import cern.accsoft.steering.jmad.tools.response.DeflectionSign;
import cern.accsoft.steering.util.TMatrix;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.yasp.CorrectorValue;
import cern.accsoft.steering.util.meas.data.yasp.MonitorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this is the basic implementation of the data of a kick-response-measurement.
 *
 * @author kfuchsbe
 */
public class KickResponseDataImpl extends AbstractDynamicData implements KickResponseData {

    private final static Logger LOGGER = LoggerFactory.getLogger(KickResponseDataImpl.class);

    private final static double DELTA_KICK_LIMIT = 0.1;

    private Map<String, CorrectorKickData> correctorKickDataPlus = new HashMap<>();
    private Map<String, CorrectorKickData> correctorKickDataMinus = new HashMap<>();

    private Matrix responseMatrix = new Matrix(0, 0);
    private Matrix relativeRmsValues = new Matrix(0, 0);
    private TMatrix<Boolean> validityMatrix = new TMatrix<>(0, 0, true);

    private Map<String, Matrix> trajMatrices = new HashMap<>();
    private Map<String, TMatrix<Boolean>> trajValidityMatrices = new HashMap<>();

    private double toModelConversionFactor = 1.0;

    /**
     * The kick-response measurement which this data belongs to (if any)
     */
    private KickResponseMeasurement kickResponseMeasurement = null;

    /**
     * here we store the corrector kicks for the correctors
     */
    private Map<String, Double> correctorKicks = new HashMap<>();

    public void init() throws InconsistentDataException {
        calc();
    }

    @Override
    public Matrix getResponseMatrix() {
        if (isDirty()) {
            try {
                calc();
                setDirty(false);
            } catch (InconsistentDataException e) {
                LOGGER.error("Error while calculating response - matrix", e);
            }
        }
        return responseMatrix;
    }

    public void calc() throws InconsistentDataException {

        /* clear the trajectories */
        trajMatrices.clear();
        trajValidityMatrices.clear();
        correctorKicks.clear();

        /* active Correctors */
        List<Corrector> activeCorrectors = getActiveCorrectors();

        /* number of files (one per kicked corrector) */
        int activeCorrectorsCount = activeCorrectors.size();

        /* active Monitors */
        List<Monitor> activeMonitors = getActiveMonitors();

        /* number of functional Monitors */
        int activeMonitorsCount = activeMonitors.size();

        LOGGER.debug("We have " + activeCorrectorsCount + " active correctors and " + activeMonitorsCount
                + " active Monitors. So the Response-Matrix will be a " + activeMonitorsCount + "x"
                + activeCorrectorsCount + " Matrix.");

        responseMatrix = new Matrix(activeMonitorsCount, activeCorrectorsCount);
        validityMatrix = new TMatrix<>(activeMonitorsCount, activeCorrectorsCount, false);

        /* add new empty trajectory-matrices */
        for (Plane plane : Plane.values()) {
            for (DeflectionSign sign : DeflectionSign.values()) {
                int monitorCount = getMachineElementsManager().getActiveMonitorsCount(plane);
                addTrajectoryMatrices(plane, sign, new Matrix(monitorCount, activeCorrectorsCount),
                        new TMatrix<>(monitorCount, activeCorrectorsCount, false));
            }
        }

        int activeCorrectorNumber = 0;
        for (Corrector activeCorrector : activeCorrectors) {

            CorrectorKickData dataPlus = correctorKickDataPlus.get(activeCorrector.getKey());
            CorrectorKickData dataMinus = correctorKickDataMinus.get(activeCorrector.getKey());

            /*
             * if we have no data for this corrector, then we just leave the elements at zero.
             */
            if ((dataPlus != null) && (dataMinus != null)) {

                /* now we actually can fill the matrix */
                double correctorKick = 0;

                /*
                 * we use another corrector - loop to ensure that there is only one kick inside one file!
                 */
                for (Corrector corrector : activeCorrectors) {
                    /* do we get values from both files? */
                    CorrectorValue correctorValueMinus = dataMinus.getCorrectorValue(corrector.getKey());
                    CorrectorValue correctorValuePlus = dataPlus.getCorrectorValue(corrector.getKey());

                    if (correctorValueMinus == null && correctorValuePlus == null) {
                        continue;
                    }

                    if (correctorValueMinus == null) {
                        throw new InconsistentDataException("No corrector value for corrector '" + corrector.getName()
                                + "' found in minus file for corrector '" + activeCorrector.getName() + "'!");
                    }

                    if (correctorValuePlus == null) {
                        throw new InconsistentDataException("No corrector value for corrector '" + corrector.getName()
                                + "' found in plus file for corrector '" + activeCorrector.getName() + "'!");
                    }

                    /* for the moment lets keep microrad. */
                    double deltaKick = correctorValuePlus.kick - correctorValueMinus.kick;
                    if (Math.abs(deltaKick) > DELTA_KICK_LIMIT) {
                        /* only one corrector is allowed to kick in one file! */
                        if (correctorKick == 0) {
                            if (!activeCorrector.getName().equals(corrector.getName())) {
                                throw new InconsistentDataException("The corrector with nonzero kick ("
                                        + corrector.getName()
                                        + ") does not correspond to the corrector in the filename("
                                        + activeCorrector.getName() + ")!");
                            }
                            correctorKick = deltaKick;
                        } else {
                            throw new InconsistentDataException(
                                    "There seem to be more than one nonzero kicks in one datafile (datafile for"
                                            + " corrector '" + dataPlus.getCorrectorName()
                                            + "')! - This is not allowed.");
                        }
                    }
                }

                if (correctorKick == 0) {
                    throw new InconsistentDataException("There is no nonzero kick in files for corrector '"
                            + dataPlus.getCorrectorName() + "'. This is not allowed!");
                }

                this.correctorKicks.put(activeCorrector.getKey(), toModel(correctorKick));

                int activeMonitorNumber = 0;
                int hMonitorNumber = 0;
                int vMonitorNumber = 0;
                for (Monitor monitor : activeMonitors) {
                    if (monitor.isActive()) {
                        MonitorValue monitorValuePlus = dataPlus.getMonitorValue(monitor.getKey());
                        MonitorValue monitorValueMinus = dataMinus.getMonitorValue(monitor.getKey());

                        boolean valid = ((monitorValuePlus != null) && (monitorValueMinus != null)
                                && monitorValuePlus.isOk() && monitorValueMinus.isOk() && activeCorrector.isOk());
                        double posPlus = 0.0;
                        if (monitorValuePlus != null) {
                            posPlus = monitorValuePlus.getBeamPosition();
                        }

                        double posMinus = 0.0;
                        if (monitorValueMinus != null) {
                            posMinus = monitorValueMinus.getBeamPosition();
                        }
                        double deltaPos = posPlus - posMinus;

                        responseMatrix.set(activeMonitorNumber, activeCorrectorNumber, deltaPos / correctorKick);
                        validityMatrix.set(activeMonitorNumber, activeCorrectorNumber, valid);
                        /*
                         * we determine the plane from the plus-value
                         */
                        Plane plane = monitor.getPlane();
                        if ((monitorValuePlus != null) && (monitorValueMinus != null)) {

                            /*
                             * also save the trajectories
                             */

                            int monitorNumber;
                            if (Plane.HORIZONTAL.equals(plane)) {
                                monitorNumber = hMonitorNumber;
                            } else {
                                monitorNumber = vMonitorNumber;
                            }

                            DeflectionSign sign = DeflectionSign.PLUS;
                            getTrajectoryMatrix(plane, sign).set(monitorNumber, activeCorrectorNumber,
                                    toModel(monitorValuePlus.getBeamPosition()));
                            getTrajectoryValidity(plane, sign).set(monitorNumber, activeCorrectorNumber,
                                    monitorValuePlus.isOk());

                            sign = DeflectionSign.MINUS;
                            getTrajectoryMatrix(plane, sign).set(monitorNumber, activeCorrectorNumber,
                                    toModel(monitorValueMinus.getBeamPosition()));
                            getTrajectoryValidity(plane, sign).set(monitorNumber, activeCorrectorNumber,
                                    monitorValueMinus.isOk());
                        }

                        if (Plane.HORIZONTAL.equals(plane)) {
                            hMonitorNumber++;
                        } else {
                            vMonitorNumber++;
                        }

                        activeMonitorNumber++;
                    }
                }
            }
            activeCorrectorNumber++;
        }

        this.relativeRmsValues = calcRelativeRmsValues();
    }

    private void addTrajectoryMatrices(Plane plane, DeflectionSign sign, Matrix traj, TMatrix<Boolean> validity) {
        String key = createTrajKey(plane, sign);
        this.trajMatrices.put(key, traj);
        this.trajValidityMatrices.put(key, validity);
    }

    public TMatrix<Boolean> getTrajectoryValidity(Plane plane, DeflectionSign sign) {
        return this.trajValidityMatrices.get(createTrajKey(plane, sign));
    }

    public Matrix getTrajectoryMatrix(Plane plane, DeflectionSign sign) {
        return this.trajMatrices.get(createTrajKey(plane, sign));
    }

    final String createTrajKey(Plane plane, DeflectionSign sign) {
        return plane.getTag() + "-" + sign.getTag();
    }

    private double toModel(double value) {
        return this.getToModelConversionFactor() * value;
    }

    public void decouple() {
        List<Corrector> activeCorrectors = getActiveCorrectors();
        List<Monitor> workingMonitors = getActiveMonitors();

        for (int i = 0; i < responseMatrix.getRowDimension(); i++) {
            for (int j = 0; j < responseMatrix.getColumnDimension(); j++) {
                if (activeCorrectors.get(j).getPlane() != workingMonitors.get(i).getPlane()) {
                    responseMatrix.set(i, j, 0);
                }
            }
        }
    }

    public TMatrix<Boolean> getValidityMatrix() {
        return validityMatrix;
    }

    public void addDataPlus(CorrectorKickData data) {
        this.correctorKickDataPlus.put(data.getCorrectorKey(), data);
    }

    public void addDataMinus(CorrectorKickData data) {
        this.correctorKickDataMinus.put(data.getCorrectorKey(), data);
    }

    public void removeDataPlus(String correctorKey) {
        this.correctorKickDataPlus.remove(correctorKey);
    }

    public void removeDataMinus(String correctorKey) {
        this.correctorKickDataMinus.remove(correctorKey);
    }

    /**
     * @return the correctorKickDataPlus
     */
    public final Map<String, CorrectorKickData> getCorrectorKickDataPlus() {
        return correctorKickDataPlus;
    }

    /**
     * @return the correctorKickDataMinus
     */
    public final Map<String, CorrectorKickData> getCorrectorKickDataMinus() {
        return correctorKickDataMinus;
    }

    /**
     * @param toModelConversionFactor the toModelConversionFactor to set
     */
    public void setToModelConversionFactor(double toModelConversionFactor) {
        this.toModelConversionFactor = toModelConversionFactor;
    }

    /**
     * @return the toModelConversionFactor
     */
    public double getToModelConversionFactor() {
        return toModelConversionFactor;
    }

    @Override
    public CorrectorKickData getCorrectorKickData(Corrector corrector, DeflectionSign sign) {
        if (DeflectionSign.PLUS.equals(sign)) {
            return this.correctorKickDataPlus.get(corrector.getKey());
        } else if (DeflectionSign.MINUS.equals(sign)) {
            return this.correctorKickDataMinus.get(corrector.getKey());
        } else {
            return null;
        }
    }

    /**
     * @return the rms-values relative to the corrector-kicks
     */
    @Override
    public Matrix getRelativeRmsValues() {
        return this.relativeRmsValues;
    }

    /**
     * calculates the rms-values relative to the corrector-kicks
     *
     * @return the calculated matrix
     */
    private Matrix calcRelativeRmsValues() {
        List<Monitor> monitors = getMachineElementsManager().getActiveMonitors();
        List<Corrector> correctors = getMachineElementsManager().getActiveCorrectors();

        Matrix matrix = new Matrix(monitors.size(), correctors.size());

        TrajectoryData stabilityData = getStabilityData();

        if (stabilityData != null) {
            List<Double> noises = stabilityData.getRmsValues();

            /* fill the matrix */
            for (int i = 0; i < monitors.size(); i++) {
                for (int j = 0; j < correctors.size(); j++) {
                    Double kick = getCorrectorKick(correctors.get(j));
                    if (kick != null) {
                        matrix.set(i, j, noises.get(i) / kick);
                    }
                }
            }
        }
        return matrix;
    }

    @Override
    public Double getCorrectorKick(Corrector corrector) {
        return this.correctorKicks.get(corrector.getKey());
    }

    public void setKickResponseMeasurement(KickResponseMeasurement kickResponseMeasurement) {
        this.kickResponseMeasurement = kickResponseMeasurement;
    }

    /**
     * @return the stability-data
     */
    @Override
    public TrajectoryData getStabilityData() {
        if (this.kickResponseMeasurement != null) {
            TrajectoryMeasurementImpl trajectoryMeasurement = this.kickResponseMeasurement.getStabilityMeasurement();
            if (trajectoryMeasurement != null) {
                return trajectoryMeasurement.getData();
            }
        }
        return null;
    }

}
