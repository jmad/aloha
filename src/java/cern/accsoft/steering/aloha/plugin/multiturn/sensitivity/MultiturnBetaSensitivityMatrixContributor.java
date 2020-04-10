/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.sensitivity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.calc.sensitivity.PerturbedColumn;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.MultiturnMeasurement;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnVar;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.jmad.util.MatrixUtil;
import cern.accsoft.steering.util.meas.data.Plane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kfuchsbe
 */
public class MultiturnBetaSensitivityMatrixContributor implements SensitivityMatrixContributor,
        MachineElementsManagerAware, NoiseWeighterAware {
    private final static Logger LOGGER = LoggerFactory.getLogger(MultiturnBetaSensitivityMatrixContributor.class);

    private MultiturnMeasurement multiturnMeasurement;

    private MachineElementsManager machineElementsManager;

    private final static double minNorm = 0.0000001;

    private NoiseWeighter noiseWeighter;

    /** the beta-values for the unperturbed model */
    private Matrix unperturbedVector = new Matrix(1, 1);

    /** the norm of the unperturbed vector */
    private double unperturbedNorm = 1;

    public MultiturnBetaSensitivityMatrixContributor(MultiturnMeasurement multiturnMeasurement) {
        this.multiturnMeasurement = multiturnMeasurement;
    }

    @Override
    public Matrix calcCorrectorSensitivityMatrix() {
        /*
         * For the moment we do not take into account the change in beta depending on corrector gains.
         * 
         * TODO: check if ok
         */
        return null;
    }

    @Override
    public Matrix calcMonitorSensitivityMatrix() {
        /*
         * For the moment we do not take into account the change in beta depending on monitor gains.
         * 
         * TODO: check if ok
         */
        return null;
    }

    @Override
    public PerturbedColumn calcPerturbedColumn(double delta, Double normalizationFactor) {
        int monitorCount = getMachineElementsManager().getActiveMonitorsCount();
        List<Boolean> validity = getMultiturnMeasurement().getData().getValidityValues();
        List<Double> errorValues = getMultiturnMeasurement().getData().getValues(MultiturnVar.BETA_ERROR);

        LOGGER.debug("creating " + monitorCount + "x" + 1 + " disturbed-sensitivity-matrix-column...");
        Matrix sensitivityMatrix = new Matrix(monitorCount, 1);

        Matrix deltaVector = calcDeltaVector(delta);

        /* if no normalization-factor is given, we have to calc our own. */
        if (normalizationFactor == null) {
            normalizationFactor = deltaVector.normF() / this.unperturbedNorm;
            if (normalizationFactor < minNorm) {
                LOGGER.warn("Normalization Factor for perturbed Beta column is smaller than " + minNorm
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
                    errorValues.get(i)));
        }
        return new PerturbedColumn(sensitivityMatrix, normalizationFactor);
    }

    /**
     * @param delta the delta, which to use to norm the matrix
     * @return the difference-response matrix, normalized over the given delta
     */
    private Matrix calcDeltaVector(double delta) {
        Matrix betaVector = MatrixUtil.createVector(getModelOpticsData().getMonitorBetas());
        betaVector.minusEquals(this.unperturbedVector);
        betaVector.timesEquals(1 / delta);
        return betaVector;

    }

    @Override
    public Matrix getDifferenceVector() {
        List<Double> values = new ArrayList<>();
        values.addAll(calcNoisyMonitorBetaDiff(Plane.HORIZONTAL));
        values.addAll(calcNoisyMonitorBetaDiff(Plane.VERTICAL));
        return MatrixUtil.createVector(values);

    }

    /**
     * calculates the beta differences values (measurement - model)
     * 
     * @param plane
     * @return
     */
    private List<Double> calcNoisyMonitorBetaDiff(Plane plane) {
        List<Double> diff = ListUtil.diff(getMultiturnMeasurement().getData().getValues(MultiturnVar.BETA, plane),
                getModelOpticsData().getMonitorBetas(plane));
        return getNoiseWeighter().calcNoisyValues(diff,
                getMultiturnMeasurement().getData().getValues(MultiturnVar.BETA_ERROR, plane));
    }

    @Override
    public int getMatrixRowCount() {
        return getMachineElementsManager().getActiveMonitorsCount();
    }

    @Override
    public Measurement getMeasurement() {
        return this.multiturnMeasurement;
    }

    @Override
    public String getName() {
        return "Multiturn beta";
    }

    @Override
    public void initUnperturbed() {
        this.unperturbedVector = MatrixUtil.createVector(getModelOpticsData().getMonitorBetas());
        this.unperturbedNorm = this.unperturbedVector.normF();
    }

    public void setMultiturnMeasurement(MultiturnMeasurement multiturnMeasurement) {
        this.multiturnMeasurement = multiturnMeasurement;
    }

    private MultiturnMeasurement getMultiturnMeasurement() {
        return multiturnMeasurement;
    }

    @Override
    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    private MachineElementsManager getMachineElementsManager() {
        return this.machineElementsManager;
    }

    /**
     * @return the modelOpticsData
     */
    private ModelOpticsData getModelOpticsData() {
        return getMultiturnMeasurement().getModelDelegate().getModelOpticsData();
    }

    @Override
    public void setNoiseWeighter(NoiseWeighter noiseWeighter) {
        this.noiseWeighter = noiseWeighter;
    }

    private NoiseWeighter getNoiseWeighter() {
        return this.noiseWeighter;
    }

    @Override
    public Matrix getDifferenceVectorErrors() {
        int rowCount = getMatrixRowCount();
        List<Double> errorValues = getMultiturnMeasurement().getData().getValues(MultiturnVar.BETA_ERROR);

        boolean activeNoise = getNoiseWeighter().isActiveNoise();

        Matrix vector = new Matrix(rowCount, 1);
        for (int i = 0; i < rowCount; i++) {
            /* if the noise is active then the difference vector is weighted by the noise, so the error results in one. */
            double error = (activeNoise ? 1 : errorValues.get(i));
            vector.set(i, 0, error);
        }
        return vector;
    }
}
