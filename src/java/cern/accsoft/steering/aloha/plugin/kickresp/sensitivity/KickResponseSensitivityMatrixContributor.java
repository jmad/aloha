/*
 * $Id: KickResponseSensitivityMatrixContributor.java,v 1.6 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.6 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.kickresp.sensitivity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.calc.sensitivity.PerturbedColumn;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CombinedKickResponseData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.ModelKickResponseData;
import cern.accsoft.steering.util.TMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class creates the parts of the sensitivity-matrix which correspond to kick-response measurements
 * 
 * @author kfuchsbe
 */
public class KickResponseSensitivityMatrixContributor implements
        SensitivityMatrixContributor, NoiseWeighterAware, MachineElementsManagerAware {
    private final static Logger LOGGER = LoggerFactory.getLogger(KickResponseSensitivityMatrixContributor.class);

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
        int monitorCount = getMachineElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachineElementsManager().getActiveCorrectorsCount();
        return monitorCount * correctorCount;
    }

    @Override
    public Matrix calcMonitorSensitivityMatrix() {
        int monitorCount = getMachineElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachineElementsManager().getActiveCorrectorsCount();
        TMatrix<Boolean> validityMatrix = getValidityMatrix();
        Matrix noises = getNoises();
        Matrix responseMatrixModel = getModelKickResponseData().getResponseMatrix();

        int sensitivityRowCount = monitorCount * correctorCount;
        LOGGER.debug("creating " + sensitivityRowCount + "x" + monitorCount + " monitor-sensitivity-matrix...");
        Matrix sensitivityMatrix = new Matrix(sensitivityRowCount, monitorCount);

        for (int i = 0; i < monitorCount; i++) {
            for (int j = 0; j < correctorCount; j++) {
                // just leave the values for defect monitors/correctors at zero.
                if (!validityMatrix.get(i, j)) {
                    continue;
                }

                int row = (i * correctorCount) + j;
                /* columns correspond to the monitor number */
                int col = i;
                sensitivityMatrix.set(row, col, getNoiseWeighter().calcNoisyValue(responseMatrixModel.get(i, j),
                        noises.get(i, j)));
            }
        }
        return sensitivityMatrix;
    }

    @Override
    public Matrix calcCorrectorSensitivityMatrix() {
        int monitorCount = getMachineElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachineElementsManager().getActiveCorrectorsCount();
        TMatrix<Boolean> validityMatrix = getValidityMatrix();
        Matrix noises = getNoises();
        Matrix responseMatrixModel = getModelKickResponseData().getResponseMatrix();

        int sensitivityRowCount = monitorCount * correctorCount;
        LOGGER.debug("creating " + sensitivityRowCount + "x" + correctorCount + " corrector-sensitivity-matrix...");
        Matrix sensitivityMatrix = new Matrix(sensitivityRowCount, correctorCount);

        for (int i = 0; i < monitorCount; i++) {
            for (int j = 0; j < correctorCount; j++) {

                // just leave the values for defect monitors/correctors at zero.
                if (!validityMatrix.get(i, j)) {
                    continue;
                }

                int row = (i * correctorCount) + j;
                /* column corresponds to corrector-number */
                int col = j;
                sensitivityMatrix.set(row, col, getNoiseWeighter().calcNoisyValue(responseMatrixModel.get(i, j),
                        noises.get(i, j)));

            }
        }
        return sensitivityMatrix;
    }

    @Override
    public String getName() {
        return CONTRIBUTOR_NAME_PREFIX + ":" + getMeasurement().getName();
    }

    @Override
    public PerturbedColumn calcPerturbedColumn(double delta, Double normalizationFactor) {
        int monitorCount = getMachineElementsManager().getActiveMonitorsCount();
        int correctorCount = getMachineElementsManager().getActiveCorrectorsCount();
        TMatrix<Boolean> validityMatrix = getValidityMatrix();
        Matrix noises = getNoises();

        int sensitivityRowCount = monitorCount * correctorCount;
        LOGGER.debug("creating " + sensitivityRowCount + "x" + correctorCount + " corrector-sensitivity-matrix...");
        Matrix sensitivityMatrix = new Matrix(sensitivityRowCount, 1);

        Matrix deltaMatrix = calcDeltaResponseMatrix(delta);

        /* if no normalization-factor is given, we have to calc our own. */
        if (normalizationFactor == null) {
            if (this.unperturbedNorm < minNorm) {
                LOGGER.warn("unperturbed normalization factor is smaller than " + minNorm
                        + ". -> not normalizing the matrix.");
                normalizationFactor = 1.0;
            } else {
                normalizationFactor = deltaMatrix.normF() / this.unperturbedNorm;
                if (normalizationFactor < minNorm) {
                    LOGGER.warn("Normalization Factor for perturbed response matrix is smaller than " + minNorm
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
                sensitivityMatrix.set(row, col, getNoiseWeighter().calcNoisyValue(
                        deltaMatrix.get(i, j) / normalizationFactor, noises.get(i, j)));
            }
        }
        return new PerturbedColumn(sensitivityMatrix, normalizationFactor);
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
    public MachineElementsManager getMachineElementsManager() {
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
