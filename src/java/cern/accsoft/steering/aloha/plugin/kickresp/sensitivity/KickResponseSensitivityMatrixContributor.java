/*
 * $Id: KickResponseSensityMatrixContributor.java,v 1.6 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.6 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.kickresp.sensitivity;

import org.apache.log4j.Logger;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.calc.sensitivity.AbstractSensitivityMatrixContributor;
import cern.accsoft.steering.aloha.calc.sensitivity.PerturbedColumn;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CombinedKickResponseData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.ModelKickResponseData;
import cern.accsoft.steering.util.TMatrix;

/**
 * this class creates the parts of the sensity-matrix which correspond to kick-response measurements
 * 
 * @author kfuchsbe
 */
public class KickResponseSensitivityMatrixContributor extends AbstractSensitivityMatrixContributor implements
        SensitivityMatrixContributor, NoiseWeighterAware, MachineElementsManagerAware {

    /** the logger for the class */
    private final static Logger logger = Logger.getLogger(KickResponseSensitivityMatrixContributor.class);

    /** the name of this contributor */
    private final static String CONTRIBUTOR_NAME_PREFIX = "Kick-response";

    /** the minimal norm */
    private final static double minNorm = 0.0000001;

    /** the class which takes care of scaling by the noise correctly */
    private NoiseWeighter noiseWeighter;

    /** the class who knows, what elements are selected */
    private MachineElementsManager machineElementsManager;

    /** from this data we will retrieve the difference-vector */
    private KickResponseMeasurementImpl kickResponseMeasurement;

    //
    // the following fields are used internally
    //

    /** the model-response matrix of the unperturbed model */
    private Matrix unperturbedModelResponseMatrix;

    /** the norm of the unperturbed matrix */
    double unperturbedNorm = 1;

    @Override
    public int getMatrixRowCount() {
        int monitorCount = getMachinElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachinElementsManager().getActiveCorrectorsCount();
        return monitorCount * correctorCount;
    }

    @Override
    public Matrix calcMonitorSensityMatrix() {
        int monitorCount = getMachinElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachinElementsManager().getActiveCorrectorsCount();
        TMatrix<Boolean> validityMatrix = getValidityMatrix();
        Matrix noises = getNoises();
        Matrix responseMatrixModel = getModelKickResponseData().getResponseMatrix();

        int sensityRowCount = monitorCount * correctorCount;
        logger.debug("creating " + sensityRowCount + "x" + monitorCount + " monitor-sensity-matrix...");
        Matrix sensityMatrix = new Matrix(sensityRowCount, monitorCount);

        for (int i = 0; i < monitorCount; i++) {
            for (int j = 0; j < correctorCount; j++) {
                // just leave the values for defect monitors/correctors at zero.
                if (!validityMatrix.get(i, j)) {
                    continue;
                }

                int row = (i * correctorCount) + j;
                /* columns correspond to the monitor number */
                int col = i;
                sensityMatrix.set(row, col, getNoiseWeighter().calcNoisyValue(responseMatrixModel.get(i, j),
                        noises.get(i, j)));
            }
        }
        return sensityMatrix;
    }

    @Override
    public Matrix calcCorrectorSensityMatrix() {
        int monitorCount = getMachinElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachinElementsManager().getActiveCorrectorsCount();
        TMatrix<Boolean> validityMatrix = getValidityMatrix();
        Matrix noises = getNoises();
        Matrix responseMatrixModel = getModelKickResponseData().getResponseMatrix();

        int sensityRowCount = monitorCount * correctorCount;
        logger.debug("creating " + sensityRowCount + "x" + correctorCount + " corrector-sensity-matrix...");
        Matrix sensityMatrix = new Matrix(sensityRowCount, correctorCount);

        for (int i = 0; i < monitorCount; i++) {
            for (int j = 0; j < correctorCount; j++) {

                // just leave the values for defect monitors/correctors at zero.
                if (!validityMatrix.get(i, j)) {
                    continue;
                }

                int row = (i * correctorCount) + j;
                /* column corresponds to corrector-number */
                int col = j;
                sensityMatrix.set(row, col, getNoiseWeighter().calcNoisyValue(responseMatrixModel.get(i, j),
                        noises.get(i, j)));

            }
        }
        return sensityMatrix;
    }

    @Override
    public String getName() {
        return CONTRIBUTOR_NAME_PREFIX + ":" + getMeasurement().getName();
    }

    @Override
    public PerturbedColumn calcPerturbedColumn(double delta, Double normalizationFactor) {
        int monitorCount = getMachinElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachinElementsManager().getActiveCorrectorsCount();
        TMatrix<Boolean> validityMatrix = getValidityMatrix();
        Matrix noises = getNoises();

        int sensityRowCount = monitorCount * correctorCount;
        logger.debug("creating " + sensityRowCount + "x" + correctorCount + " corrector-sensity-matrix...");
        Matrix sensityMatrix = new Matrix(sensityRowCount, 1);

        Matrix deltaMatrix = calcDeltaResponseMatrix(delta);

        /* if no normalization-factor is given, we have to calc our own. */
        if (normalizationFactor == null) {
            if (this.unperturbedNorm < minNorm) {
                logger.warn("unperturbed normalization factor is smaller than " + minNorm
                        + ". -> not normalizing the matrix.");
                normalizationFactor = 1.0;
            } else {
                normalizationFactor = deltaMatrix.normF() / this.unperturbedNorm;
                if (normalizationFactor < minNorm) {
                    logger.warn("Normalization Factor for perturbed response matrix is smaller than " + minNorm
                            + ". Maybe the choice for delta of the parameter was too small.");
                    normalizationFactor = 1.0;
                }
            }
        }

        int col = 0;
        for (int i = 0; i < monitorCount; i++) {
            for (int j = 0; j < correctorCount; j++) {

                /*
                 * again we leave the values for defect monitors/correctors at zero.
                 */
                if (!validityMatrix.get(i, j)) {
                    continue;
                }

                int row = (i * correctorCount) + j;
                sensityMatrix.set(row, col, getNoiseWeighter().calcNoisyValue(
                        deltaMatrix.get(i, j) / normalizationFactor, noises.get(i, j)));
            }
        }
        return new PerturbedColumn(sensityMatrix, normalizationFactor);
    }

    @Override
    public void initUnperturbed() {
        this.unperturbedModelResponseMatrix = getModelKickResponseData().getResponseMatrix();
        this.unperturbedNorm = this.unperturbedModelResponseMatrix.normF();
    }

    @Override
    public Matrix getDifferenceVector() {
        return getCombinedKickResponseData().getDifferenceVector();
    }

    /**
     * @return the actual noises
     */
    private Matrix getNoises() {
        return this.kickResponseMeasurement.getData().getRelativeRmsValues();
        // TrajectoryData stabilityData = getMeasurement().getData().getStabilityData();
        // if (stabilityData != null) {
        // return stabilityData.getRmsValues();
        // } else {
        // return ArrayUtil.createDefaultValueList(machineElementsManager.getActiveMonitorsCount(), 0.0);
        // }
    }

    /**
     * @return the validity matrix for the measurement
     */
    private TMatrix<Boolean> getValidityMatrix() {
        return this.kickResponseMeasurement.getData().getValidityMatrix();
    }

    /**
     * @param delta the delta, which to use to norm the matrix
     * @return the difference-response matrix, normalized over the given delta
     */
    private Matrix calcDeltaResponseMatrix(double delta) {
        Matrix responseMatrix = getModelKickResponseData().getResponseMatrix();
        return (responseMatrix.minus(this.unperturbedModelResponseMatrix)).times(1 / delta);
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
    private NoiseWeighter getNoiseWeighter() {
        return noiseWeighter;
    }

    /**
     * @param machinElementsManager the machinElementsManager to set
     */
    @Override
    public void setMachineElementsManager(MachineElementsManager machinElementsManager) {
        this.machineElementsManager = machinElementsManager;
    }

    /**
     * @return the machinElementsManager
     */
    public MachineElementsManager getMachinElementsManager() {
        return machineElementsManager;
    }

    /**
     * @return the unperturbedModelResponseMatrix
     */
    public Matrix getUnperturbedModelResponseMatrix() {
        return unperturbedModelResponseMatrix;
    }

    /**
     * @param kickResponseMeasurement the combinedKickResponseData to set
     */
    public void setMeasurement(KickResponseMeasurementImpl kickResponseMeasurement) {
        this.kickResponseMeasurement = kickResponseMeasurement;
    }

    /**
     * @return the combinedKickResponseData
     */
    private CombinedKickResponseData getCombinedKickResponseData() {
        return this.kickResponseMeasurement.getCombinedData();
    }

    @Override
    public KickResponseMeasurementImpl getMeasurement() {
        return this.kickResponseMeasurement;
    }

    private ModelKickResponseData getModelKickResponseData() {
        return this.kickResponseMeasurement.getModelData();
    }

    @Override
    public Matrix getDifferenceVectorErrors() {
        Matrix errorMatrix = getNoises();
        /*
         * just flatten the matrix to a vector.
         */
        int correctorCount = errorMatrix.getColumnDimension();
        int monitorCount = errorMatrix.getRowDimension();

        Matrix vector = new Matrix(correctorCount * monitorCount, 1, 1.0);
        if (getNoiseWeighter().isActiveNoise()) {
            /*
             * if the noise is active, then the error vector is simply a vector containing only ones.
             */
            return vector;
        } else {
            /*
             * otherwise we have to flatten it.
             */
            for (int mon = 0; mon < monitorCount; mon++) {
                for (int corr = 0; corr < correctorCount; corr++) {
                    int row = mon * correctorCount + corr;
                    vector.set(row, 0, errorMatrix.get(mon, corr));
                }
            }

            return vector;
        }
    }

}
