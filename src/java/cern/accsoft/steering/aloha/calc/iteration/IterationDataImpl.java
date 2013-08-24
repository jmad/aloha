/**
 * 
 */
package cern.accsoft.steering.aloha.calc.iteration;

import java.util.HashMap;
import java.util.Map;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.calc.variation.VariationParameter;

/**
 * The implementation of the data holding info about aloha iterations
 * 
 * @author kfuchsbe
 */
public class IterationDataImpl implements IterationData {

    /** The iteration number of this data */
    private int iterationNumber = 0;

    /** The difference rms including all contributors */
    private double allDifferenceRms = 0;

    /** The difference rms including only active contributors */
    private double activeDifferenceRms = 0;

    /** The diff-rms values per contributor */
    private Map<SensitivityMatrixContributor, Double> contributorDiffRms = new HashMap<SensitivityMatrixContributor, Double>();

    /** The values of the parameters */
    private Map<String, Double> parameterValues = new HashMap<String, Double>();

    /**
     * the constructor, which enforces to give an interationNumber
     * 
     * @param iterationNumber the iteration number of the data
     */
    public IterationDataImpl(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    @Override
    public Double getValue(VariationParameter parameter) {
        return this.parameterValues.get(parameter.getKey());
    }

    @Override
    public void storeValue(VariationParameter parameter) {
        this.parameterValues.put(parameter.getKey(), parameter.getActualOffset());
    }

    @Override
    public int getIterationNumber() {
        return this.iterationNumber;
    }

    @Override
    public double getActiveDifferenceRms() {
        return this.activeDifferenceRms;
    }

    @Override
    public double getAllDifferenceRms() {
        return this.allDifferenceRms;
    }

    @Override
    public Double getDifferenceRms(SensitivityMatrixContributor contributor) {
        return this.contributorDiffRms.get(contributor);
    }

    public void setDifferenceRms(SensitivityMatrixContributor contributor, Double value) {
        this.contributorDiffRms.put(contributor, value);
    }

    public void setAllDifferenceRms(double allDifferenceRms) {
        this.allDifferenceRms = allDifferenceRms;
    }

    public void setActiveDifferenceRms(double activeDifferenceRms) {
        this.activeDifferenceRms = activeDifferenceRms;
    }

}
