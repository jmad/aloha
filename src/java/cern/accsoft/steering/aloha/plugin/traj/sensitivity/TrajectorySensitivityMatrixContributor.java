package cern.accsoft.steering.aloha.plugin.traj.sensitivity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.calc.sensitivity.PerturbedColumn;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;
import cern.accsoft.steering.jmad.util.MatrixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/*
 * $Id: DispersionSensitivityMatrixContributor.java,v 1.2 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ $Revision: 1.2 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
/**
 * This class is responsible for creating correct parts of the sensitivity-matrix, corresponding to the dispersion-data
 * 
 * @author kfuchsbe, tbaer
 */
public class TrajectorySensitivityMatrixContributor implements SensitivityMatrixContributor, NoiseWeighterAware,
        MachineElementsManagerAware {
    private final static Logger LOGGER = LoggerFactory.getLogger(TrajectorySensitivityMatrixContributor.class);

    /** the name of this contributor */
    private final static String CONTRIBUTOR_NAME = "Trajectory";

    private final static double minNorm = 0.0000001;

    /** the manager, which keeps track of active monitors and correctors */
    private MachineElementsManager machineElementsManager;

    /** the class which takes care of correct weighting the values to noise */
    private NoiseWeighter noiseWeighter;

    /** the trajectory-measurement which provides all the data */
    private TrajectoryMeasurement trajectoryMeasurement;

    /*
     * the following is used internally
     */
    /** the dispersion-values for the unperturbed model */
    private Matrix unperturbedVector = new Matrix(1, 1);

    /** the norm of the unperturbed vector */
    private double unperturbedNorm = 1;

    @Override
    public Matrix calcCorrectorSensitivityMatrix() {
        /*
         * we do not take into account dispersion-change depending on corrector gains
         */
        return null;
    }

    @Override
    public Matrix calcMonitorSensitivityMatrix() {
        if (getTrajectoryData() == null) {
            return null;
        }

        List<Double> rmsValues = getTrajectoryData().getRmsValues();
        List<Boolean> validity = getTrajectoryData().getValidityValues();
        List<Double> modelTrajectoryValues = getModelOpticsData().getMonitorPos();
        int monitorCount = getMachineElementsManager().getActiveMonitorsCount();

        LOGGER.debug("creating " + monitorCount + "x" + monitorCount + " monitor-sensitivity-matrix...");
        Matrix sensitivityMatrix = new Matrix(monitorCount, monitorCount);

        for (int i = 0; i < monitorCount; i++) {
            /* just leave the values for defect monitors at zero. */
            if (!validity.get(i)) {
                continue;
            }

            /*
             * rows and columns correspond to the monitor number (matrix is diagonal)
             */
            sensitivityMatrix.set(i, i, getNoiseWeighter().calcNoisyValue(modelTrajectoryValues.get(i), rmsValues.get(i)));
        }
        return sensitivityMatrix;
    }

    @Override
    public PerturbedColumn calcPerturbedColumn(double delta, Double normalizationFactor) {
        int monitorCount = getMachineElementsManager().getActiveMonitorsCount();
        List<Boolean> validity = getTrajectoryData().getValidityValues();
        List<Double> rmsValues = getTrajectoryData().getRmsValues();

        LOGGER.debug("creating " + monitorCount + "x" + 1 + " disturbed-sensitivity-matrix-column...");
        Matrix sensitivityMatrix = new Matrix(monitorCount, 1);

        Matrix deltaVector = calcDeltaVector(delta);

        /* if no normalization-factor is given, we have to calc our own. */
        if (normalizationFactor == null) {
            normalizationFactor = deltaVector.normF() / this.unperturbedNorm;
            if (normalizationFactor < minNorm) {
                LOGGER.warn("Normalization Factor for perturbed Trajectory column is smaller than " + minNorm
                        + ". Maybe the choice for delta of the parameter was too small.");
                normalizationFactor = 1.0;
            }
        }

        for (int i = 0; i < monitorCount; i++) {

            /*
             * again we leave the values for defect monitors/correctors at zero.
             */
            if (!validity.get(i)) {
                continue;
            }

            sensitivityMatrix.set(i, 0, getNoiseWeighter().calcNoisyValue(deltaVector.get(i, 0) / normalizationFactor,
                    rmsValues.get(i)));
        }
        return new PerturbedColumn(sensitivityMatrix, normalizationFactor);
    }

    /**
     * @param delta the delta, which to use to norm the matrix
     * @return the difference-response matrix, normalized over the given delta
     */
    private Matrix calcDeltaVector(double delta) {
        List<Double> trajectoryValues = getModelOpticsData().getMonitorPos();
        Matrix trajectoryVector = MatrixUtil.createVector(trajectoryValues);
        trajectoryVector.minusEquals(this.unperturbedVector);
        trajectoryVector.timesEquals(1 / delta);
        return trajectoryVector;

    }

    @Override
    public Matrix getDifferenceVector() {
        return getTrajectoryMeasurement().getCombinedData().getNoisyDifferenceVector();
    }

    @Override
    public int getMatrixRowCount() {
        return getMachineElementsManager().getActiveMonitorsCount();
    }

    @Override
    public String getName() {
        return CONTRIBUTOR_NAME;
    }

    @Override
    public void initUnperturbed() {
        this.unperturbedVector = MatrixUtil.createVector(getModelOpticsData().getMonitorPos());
        this.unperturbedNorm = this.unperturbedVector.normF();
    }

    /**
     * @param machineElementsManager the machineElementsManager to set
     */
    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    /**
     * @return the machineElementsManager
     */
    private MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    /**
     * @return the dispersionData
     */
    private TrajectoryData getTrajectoryData() {
        return getTrajectoryMeasurement().getData();
    }

    /**
     * @param noiseWeighter the noiseWeighter to set
     */
    public void setNoiseWeighter(NoiseWeighter noiseWeighter) {
        this.noiseWeighter = noiseWeighter;
    }

    /**
     * @return the noiseWeighter
     */
    private NoiseWeighter getNoiseWeighter() {
        return noiseWeighter;
    }

    /**
     * @return the modelOpticsData
     */
    private ModelOpticsData getModelOpticsData() {
        return trajectoryMeasurement.getModelDelegate().getModelOpticsData();
    }

    public void setTrajectoryMeasurement(TrajectoryMeasurement trajectoryMeasurement) {
        this.trajectoryMeasurement = trajectoryMeasurement;
    }

    private TrajectoryMeasurement getTrajectoryMeasurement() {
        return trajectoryMeasurement;
    }

    @Override
    public Measurement getMeasurement() {
        return this.trajectoryMeasurement;
    }

    @Override
    public Matrix getDifferenceVectorErrors() {
        int rowCount = getMatrixRowCount();
        List<Double> rmsValues = getTrajectoryData().getRmsValues();

        boolean activeNoise = getNoiseWeighter().isActiveNoise();

        Matrix vector = new Matrix(rowCount, 1);
        for (int i = 0; i < rowCount; i++) {
            /* if the noise is active then the difference vector is weighted by the noise, so the error results in one. */
            double error = (activeNoise ? 1 : rmsValues.get(i));
            vector.set(i, 0, error);
        }
        return vector;
    }

}
