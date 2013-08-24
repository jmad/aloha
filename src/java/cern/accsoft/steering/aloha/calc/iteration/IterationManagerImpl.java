/**
 * 
 */
package cern.accsoft.steering.aloha.calc.iteration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Jama.Matrix;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;
import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.calc.variation.VariationParameter;

/**
 * collects all the data for iteration entries
 * 
 * @author kaifox
 */
public class IterationManagerImpl implements IterationManager {

    /** The recordet values of all the iterations */
    private List<IterationData> iterations = new ArrayList<IterationData>();

    /** The manager from which to get the sensitivity information */
    private SensitivityMatrixManager sensitivityMatrixManager;

    /** The variation data from which to get the parameter values */
    private VariationData variationData;

    @Override
    public void recordIteration() {
        this.iterations.add(createIterationData());
    }

    @Override
    public List<IterationData> getIterations() {
        return Collections.unmodifiableList(this.iterations);
    }

    @Override
    public void reset() {
        this.iterations.clear();
    }

    /**
     * creates a new entry for the list.
     * 
     * @return the new entry to add to the list
     */
    private final IterationData createIterationData() {
        IterationDataImpl data = new IterationDataImpl(this.iterations.size());
        data.setActiveDifferenceRms(calcRms(sensitivityMatrixManager.getActiveDifferenceVector()));
        data.setAllDifferenceRms(calcRms(sensitivityMatrixManager.getAllDifferenceVector()));
        for (SensitivityMatrixContributor contributor : sensitivityMatrixManager.getAllContributors()) {
            data.setDifferenceRms(contributor, calcRms(contributor.getDifferenceVector()));
        }
        for (VariationParameter parameter : variationData.getVariationParameters()) {
            data.storeValue(parameter);
        }
        return data;
    }

    /**
     * calculates the rms of all the entries in the given matrix.
     * 
     * @param matrix the matrix for which to calc the rms
     * @return the rms
     */
    private static double calcRms(Matrix matrix) {
        return Math.sqrt(matrix.normF() / (matrix.getRowDimension() * matrix.getColumnDimension()));
    }

    public void setSensitivityMatrixManager(SensitivityMatrixManager sensitivityMatrixManager) {
        this.sensitivityMatrixManager = sensitivityMatrixManager;
    }

    public void setVariationData(VariationData variationData) {
        this.variationData = variationData;
    }

    @Override
    public int getLastIterationNumber() {
        return this.iterations.size() - 1;
    }
}
