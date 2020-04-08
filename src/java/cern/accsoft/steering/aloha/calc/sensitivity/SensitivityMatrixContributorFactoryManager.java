/**
 * 
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;

import java.util.List;

/**
 * A class which knows about all available SensityMatrixContributorFactories
 * 
 * @author kfuchsbe
 */
public interface SensitivityMatrixContributorFactoryManager {

    /**
     * adds a {@link SensitivityMatrixContributorFactory} to the list of availabe factories
     * 
     * @param factory the factory to add
     */
    public void addFactory(SensitivityMatrixContributorFactory factory);

    /**
     * creates a list of {@link SensitivityMatrixContributor}s collected from all available
     * {@link SensitivityMatrixContributorFactory}s. 
     * 
     * @param measurement
     * @return all the contributors for the given measurement, that can be created by any of the available factories
     */
    public List<SensitivityMatrixContributor> createContributors(Measurement measurement);

}
