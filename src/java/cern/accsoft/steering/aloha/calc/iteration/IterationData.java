package cern.accsoft.steering.aloha.calc.iteration;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.calc.variation.VariationParameter;

/**
 * This interface represents data of one fit iteration.
 * 
 * @author kfuchsbe
 */
public interface IterationData {

    /**
     * @return the iteration number for this data
     */
    public int getIterationNumber();

    /**
     * @return the rms of the difference vector, including all contributors (active and inactive ones)
     */
    public double getAllDifferenceRms();

    /**
     * @return the rms of the difference vector, including only the active contributors
     */
    public double getActiveDifferenceRms();

    /**
     * @param contributor the contributor for which to get the rms
     * @return the rms of the difference vector, only for this contributor
     */
    public Double getDifferenceRms(SensitivityMatrixContributor contributor);

    /**
     * @param parameter
     * @return the value of the given parameter at the time of this iteration
     */
    public Double getValue(VariationParameter parameter);

    /**
     * stores the actual value of the parameter.
     * 
     * @param parameter the parameter whose value to store.
     */
    public void storeValue(VariationParameter parameter);
}
