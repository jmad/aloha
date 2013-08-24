/**
 * 
 */
package cern.accsoft.steering.aloha.calc.iteration;

import java.util.List;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;
import cern.accsoft.steering.aloha.calc.variation.VariationData;

/**
 * keeps track of all actually calculated iterations and stores some statistics values.
 * 
 * @author kaifox
 */
public interface IterationManager {

    /**
     * @return all the iterations
     */
    public List<IterationData> getIterations();

    /**
     * clears all the iterations
     */
    public void reset();

    /**
     * adds a new iteration entry from the actual state of the given sensitivity matrix manager
     */
    public void recordIteration();

    /**
     * @return the number of the last iterations. This can be -1, if no iteration was recorded.
     */
    public int getLastIterationNumber();

}
