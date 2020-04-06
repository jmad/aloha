/*
 * $Id: SensityMatrixManagerImpl.java,v 1.4 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.4 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResult;
import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.calc.variation.VariationParameter;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.meas.MeasurementManager;
import cern.accsoft.steering.aloha.meas.MeasurementManagerListener;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements the methods for creating sensity-matrices and applying the fitted values. It uses several
 * contributors, from which it composes the total sensity-matrix. This for example may be sensity-matrizes from
 * response-matrizes or dispersion-responses ... etc.
 * 
 * @author kfuchsbe
 */
public class SensitivityMatrixManagerImpl implements SensitivityMatrixManager, SensitivityMatrixManagerConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(SensitivityMatrixManagerImpl.class);

    //
    // fields used for configuration
    //

    /** if set, monitor gains are used as fit-parameter */
    private boolean varyMonitorGains = true;

    /** if set, corrector gains are used as fit-parameter */
    private boolean varyCorrectorGains = true;

    /**
     * this is the minimum of the norm of the perturbed response-matrices. If the norm is below, they will not be
     * normalized.
     */
    private double minNorm = 0.0000001;

    //
    // stuff to be injected
    //

    /**
     * the variation-data, which determines, how many additional parameters are used, their initial and actual values.
     */
    private VariationData variationData;

    /**
     * this manager knows, which monitors and correctors are selected and therefore determines the size of the matrices.
     */
    private MachineElementsManager machineElementsManager;

    /**
     * the manager which knows all the {@link SensitivityMatrixContributorFactory}s and therefore can create
     * {@link SensitivityMatrixContributor}s
     */
    private SensitivityMatrixContributorFactoryManager sensityMatrixContributorFactoryManager;

    /** the configs for the contributors */
    private List<SensitivityMatrixContributorState> contributorStates = new ArrayList<SensitivityMatrixContributorState>();

    /** the normalization-factors for the perturbed columns */
    private List<Double> perturbedColumnFactors = new ArrayList<Double>();

    /** the listeners to this class */
    private List<SensitivityMatrixManagerListener> listeners = new ArrayList<SensitivityMatrixManagerListener>();

    @Override
    public void apply(MatrixSolverResult solverResult) {

        Matrix deltaParameterValues = solverResult.getResultVector();
        Matrix parameterSensitivities = solverResult.getParameterSensitivities();
        Matrix parameterErrors = solverResult.getParameterErrorEstimates();

        /*
         * the factors are contained in the rows of the deltaParameterValues.
         * 
         * the corrector and monitor-gains are read and set from/to the model:
         */
        int baseRow = 0;
        if (isVaryMonitorGains()) {
            List<Monitor> monitors = getMachineElementsManager().getActiveMonitors();
            getMachineElementsManager().setSuppressChangedMonitorGainsEvent(true);
            for (int i = 0; i < monitors.size(); i++) {
                Monitor monitor = monitors.get(i);
                double oldValue = monitor.getGain();
                double newValue = oldValue * (1 + (deltaParameterValues.get(baseRow + i, 0)));
                monitor.setGain(newValue);

                double gainError = parameterErrors.get(baseRow + i, 0);
                monitor.setGainError(gainError);
            }
            getMachineElementsManager().setSuppressChangedMonitorGainsEvent(false);
            baseRow += monitors.size();
        }

        if (isVaryCorrectorGains()) {
            List<Corrector> correctors = getMachineElementsManager().getActiveCorrectors();
            getMachineElementsManager().setSuppressChangedCorrectorGainsEvent(true);
            for (int i = 0; i < correctors.size(); i++) {
                Corrector corrector = correctors.get(i);
                double oldValue = corrector.getGain();
                double newValue = oldValue * (1 + (deltaParameterValues.get(baseRow + i, 0)));
                corrector.setGain(newValue);

                double gainError = parameterErrors.get(baseRow + i, 0);
                corrector.setGainError(gainError);
            }
            getMachineElementsManager().setSuppressChangedCorrectorGainsEvent(false);

            baseRow += correctors.size();
        }

        int i = 0;
        for (VariationParameter param : getVariationData().getVariationParameters()) {
            double valueChange = (deltaParameterValues.get(i + baseRow, 0) / this.perturbedColumnFactors.get(i));
            double error = parameterErrors.get(i + baseRow, 0) / this.perturbedColumnFactors.get(i);
            param.addValueScaled(valueChange);
            param.setError(error);
            param.setSensity(parameterSensitivities.get(i, 0));
            i++;
        }
    }

    @Override
    public Matrix createSensitivityMatrix() throws CalculatorException {
        this.perturbedColumnFactors.clear();

        List<SensitivityMatrixContributor> activeContributors = getActiveContributors();

        if ((getMachineElementsManager().getActiveMonitorsCount() == 0)) {
            throw new CalculatorException("There are no monitors activated. "
                    + "It makes no sense to proceed! - Maybe there is no data loaded?");
        }
        /*
         * first we make sure, that every contributor saves its unperturbed state.
         */
        for (SensitivityMatrixContributor contributor : activeContributors) {
            contributor.initUnperturbed();
        }

        Matrix matrix = createEmptySensityMatrix();

        int row;
        int col = 0;
        /*
         * add the columns for the monitor-gains
         */
        if (isVaryMonitorGains()) {
            row = 0;
            int monitorCount = getMachineElementsManager().getActiveMonitorsCount();
            for (SensitivityMatrixContributor contributor : activeContributors) {
                int rows = contributor.getMatrixRowCount();
                Matrix subMatrix = contributor.calcMonitorSensityMatrix();
                if (subMatrix != null) {
                    matrix.setMatrix(row, row + rows - 1, col, col + monitorCount - 1, subMatrix);
                }
                row += rows;
            }
            col += monitorCount;
        }

        /*
         * columns for the corrector-gains
         */
        if (isVaryCorrectorGains()) {
            row = 0;
            int correctorCount = getMachineElementsManager().getActiveCorrectorsCount();
            for (SensitivityMatrixContributor contributor : activeContributors) {
                int rows = contributor.getMatrixRowCount();
                Matrix subMatrix = contributor.calcCorrectorSensityMatrix();
                if (subMatrix != null) {
                    matrix.setMatrix(row, row + rows - 1, col, col + correctorCount - 1, subMatrix);
                }
                row += rows;
            }
            col += correctorCount;
        }

        /*
         * and finally the columns for the varied parameters
         */
        for (VariationParameter parameter : getVariationData().getVariationParameters()) {
            row = 0;
            parameter.addDelta();
            int contribNumber = 0;
            for (SensitivityMatrixContributor contributor : activeContributors) {
                int rows = contributor.getMatrixRowCount();
                PerturbedColumn perturbedColumn;

                if (contribNumber == 0) {
                    /*
                     * we take the normalization-factors from the first contributor...
                     */
                    perturbedColumn = contributor.calcPerturbedColumn(parameter.getDelta(), null);
                    this.perturbedColumnFactors.add(perturbedColumn.getNormalizationFactor());
                } else {
                    /* ...and use the same for the other contributors */
                    Double normalizationFactor = this.perturbedColumnFactors
                            .get(this.perturbedColumnFactors.size() - 1);
                    perturbedColumn = contributor.calcPerturbedColumn(parameter.getDelta(), normalizationFactor);
                }
                Matrix columnMatrix = perturbedColumn.getColumnMatrix();
                matrix.setMatrix(row, row + rows - 1, col, col, columnMatrix);
                row += rows;
                contribNumber++;
            }
            parameter.undo();
            col += 1;
        }

        /*
         * finally we do a normalization amongst the contributors: We use the (total) matrix of the first one as
         * reference and normalize the others accordingly.
         */
        List<SensitivityMatrixContributorState> activeConfigs = getActiveContributorConfigs();

        int baseRow = 0;
        double baseNorm = 1;
        for (int i = 0; i < activeConfigs.size(); i++) {
            SensitivityMatrixContributorStateImpl config = (SensitivityMatrixContributorStateImpl) activeConfigs.get(i);
            int nRows = config.getContributor().getMatrixRowCount();
            Matrix subMatrix = matrix.getMatrix(baseRow, baseRow + nRows - 1, 0, matrix.getColumnDimension() - 1);
            if (i == 0) {
                baseNorm = subMatrix.normF();
                config.setAutomaticNorm(1.0);
            } else {
                double norm = subMatrix.normF() / baseNorm;
                if (norm < minNorm) {
                    LOGGER.warn("Normalization Factor for contributor is smaller than " + minNorm + ".");
                    norm = 1.0;
                }
                config.setAutomaticNorm(norm);
            }
            subMatrix.timesEquals(config.getTotalFactor());
            matrix.setMatrix(baseRow, baseRow + nRows - 1, 0, matrix.getColumnDimension() - 1, subMatrix);
            baseRow += nRows;
        }
        return matrix;
    }

    /**
     * @return a difference vector including all contributors, also the inactive ones
     */
    @Override
    public Matrix getAllDifferenceVector() {
        return getVector(this.contributorStates, VectorType.DIFF_VECTOR);
    }

    @Override
    public Matrix getActiveDifferenceVector() {
        return getVector(getActiveContributorConfigs(), VectorType.DIFF_VECTOR);
    }

    @Override
    public Matrix getActiveDifferenceVectorErrors() {
        return getVector(getActiveContributorConfigs(), VectorType.DIFF_VECTOR_ERRORS);
    }

    /**
     * combines the vectors of the given type from the different contributors taking into account the correct factors.
     * 
     * @param type the type of the vector
     * @return the combined vector
     */
    private Matrix getVector(List<SensitivityMatrixContributorState> contributorStates, VectorType type) {

        /* calculate the size of the vector */
        int rows = 0;
        for (SensitivityMatrixContributorState config : contributorStates) {
            rows += ((SensitivityMatrixContributorStateImpl) config).getContributor().getMatrixRowCount();
        }

        /* create the vector and fill it */
        Matrix vector = new Matrix(rows, 1);
        int baseRow = 0;
        for (SensitivityMatrixContributorState config : contributorStates) {
            SensitivityMatrixContributor contributor = ((SensitivityMatrixContributorStateImpl) config)
                    .getContributor();
            int rowDiff = contributor.getMatrixRowCount();
            if (rowDiff > 0) {
                vector.setMatrix(baseRow, baseRow + rowDiff - 1, 0, 0, type.getVector(contributor).times(
                        ((SensitivityMatrixContributorStateImpl) config).getTotalFactor()));
            }
            baseRow += rowDiff;
        }

        return vector;
    }

    private enum VectorType {
        DIFF_VECTOR {
            @Override
            public Matrix getVector(SensitivityMatrixContributor contributor) {
                return contributor.getDifferenceVector();
            }
        },
        DIFF_VECTOR_ERRORS {
            @Override
            public Matrix getVector(SensitivityMatrixContributor contributor) {
                return contributor.getDifferenceVectorErrors();
            }
        };

        public abstract Matrix getVector(SensitivityMatrixContributor contributor);
    }

    /**
     * creates an empty matrix, according to our needs. The size depends on if we are varying corrector-gains or
     * monitor-gains or both and if we are using additional variation-parameters.
     * 
     * @return a new matrix of correct size.
     */
    private Matrix createEmptySensityMatrix() {
        List<SensitivityMatrixContributor> activeContributors = getActiveContributors();

        int rows = 0;
        for (SensitivityMatrixContributor contributor : activeContributors) {
            rows += contributor.getMatrixRowCount();
        }

        int cols = 0;
        if (isVaryMonitorGains()) {
            cols += getMachineElementsManager().getActiveMonitorsCount();
        }
        if (isVaryCorrectorGains()) {
            cols += getMachineElementsManager().getActiveCorrectorsCount();
        }
        cols += getVariationData().getVariationParametersCount();

        return new Matrix(rows, cols);
    }

    /**
     * @param variationData the variationData to set
     */
    public void setVariationData(VariationData variationData) {
        this.variationData = variationData;
    }

    /**
     * @return the variationData
     */
    private VariationData getVariationData() {
        return variationData;
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

    public void setMeasurementManager(MeasurementManager measurementManager) {
        measurementManager.addListener(new MeasurementManagerListener() {

            @Override
            public void changedActiveMeasurement(Measurement activeMeasurement) {
                /* nothing to do */
            }

            @Override
            public void addedMeasurement(Measurement newMeasurement) {
                createContributors(newMeasurement);
            }

            @Override
            public void removedMeasurement(Measurement removedMeasurement) {
                removeContributors(removedMeasurement);
            }

        });
    }

    /**
     * @return the active contributors
     */
    @Override
    public List<SensitivityMatrixContributor> getActiveContributors() {
        return getContributors(getActiveContributorConfigs());
    }

    @Override
    public List<SensitivityMatrixContributor> getAllContributors() {
        return getContributors(this.contributorStates);
    }

    /**
     * @return the active contributor-configs
     */
    private List<SensitivityMatrixContributorState> getActiveContributorConfigs() {
        List<SensitivityMatrixContributorState> activeContributorConfigs = new ArrayList<SensitivityMatrixContributorState>();
        for (SensitivityMatrixContributorState config : this.contributorStates) {
            if (config.isActive()) {
                activeContributorConfigs.add(config);
            }
        }
        return activeContributorConfigs;
    }

    private static List<SensitivityMatrixContributor> getContributors(
            List<SensitivityMatrixContributorState> contributorStates) {
        List<SensitivityMatrixContributor> contributors = new ArrayList<SensitivityMatrixContributor>();
        for (SensitivityMatrixContributorState config : contributorStates) {
            contributors.add(((SensitivityMatrixContributorStateImpl) config).getContributor());
        }
        return contributors;
    }

    @Override
    public void setVaryMonitorGains(boolean varyMonitorGains) {
        this.varyMonitorGains = varyMonitorGains;
    }

    @Override
    public boolean isVaryMonitorGains() {
        return varyMonitorGains;
    }

    @Override
    public void setVaryCorrectorGains(boolean varyCorrectorGains) {
        this.varyCorrectorGains = varyCorrectorGains;
    }

    @Override
    public boolean isVaryCorrectorGains() {
        return varyCorrectorGains;
    }

    @Override
    public void setMinNorm(double minNorm) {
        this.minNorm = minNorm;
    }

    @Override
    public double getMinNorm() {
        return minNorm;
    }

    @Override
    public List<SensitivityMatrixContributorState> getContributorConfigs() {
        return this.contributorStates;
    }

    @Override
    public void addContributor(SensitivityMatrixContributor contributor) {
        putContributor(contributor);
        fireChangedContributors();
    }

    @Override
    public void removeContributors(Measurement measurement) {
        /*
         * properly remove all the contributers related to the given measurement
         */
        Iterator<SensitivityMatrixContributorState> iter = this.contributorStates.iterator();
        while (iter.hasNext()) {
            SensitivityMatrixContributorStateImpl config = (SensitivityMatrixContributorStateImpl) iter.next();
            if (measurement.equals(config.getContributor().getMeasurement())) {
                iter.remove();
            }
        }
        fireChangedContributors();
    }

    /**
     * adds the contributor without firing any event
     * 
     * @param contributor
     */
    private void putContributor(SensitivityMatrixContributor contributor) {
        this.contributorStates.add(new SensitivityMatrixContributorStateImpl(contributor));

    }

    private void createContributors(Measurement measurement) {
        List<SensitivityMatrixContributor> contributors = getSensityMatrixContributorFactoryManager()
                .createContributors(measurement);
        for (SensitivityMatrixContributor contributor : contributors) {
            putContributor(contributor);
        }
    }

    /**
     * notifies all listeners, that the amount of contributors has changed.
     */
    private void fireChangedContributors() {
        for (SensitivityMatrixManagerListener listener : this.listeners) {
            listener.changedContributors();
        }
    }

    @Override
    public void addListener(SensitivityMatrixManagerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(SensitivityMatrixManagerListener listener) {
        this.listeners.remove(listener);
    }

    public void setSensityMatrixContributorFactoryManager(
            SensitivityMatrixContributorFactoryManager sensityMatrixContributorFactoryManager) {
        this.sensityMatrixContributorFactoryManager = sensityMatrixContributorFactoryManager;
    }

    private SensitivityMatrixContributorFactoryManager getSensityMatrixContributorFactoryManager() {
        return sensityMatrixContributorFactoryManager;
    }

}
