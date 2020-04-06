/**
 *
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * the implementation of the sensityMatrixContributorFactory
 *
 * @author kfuchsbe
 *
 */
public class SensitivityMatrixContributorFactoryManagerImpl implements
        SensitivityMatrixContributorFactoryManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(SensitivityMatrixContributorFactoryManagerImpl.class);

    /** all the available factories */
    private List<SensitivityMatrixContributorFactory> factories = new ArrayList<>();

    @Override
    public void addFactory(SensitivityMatrixContributorFactory factory) {
        this.factories.add(factory);
    }

    /**
     * creates a correct {@link SensitivityMatrixContributor}s, collected from all
     * the {@link SensitivityMatrixContributorFactory}s
     *
     * @param measurement
     */
    @Override
    public List<SensitivityMatrixContributor> createContributors(
            Measurement measurement) {

        List<SensitivityMatrixContributor> contributors = new ArrayList<>();
        if (measurement == null) {
            LOGGER.warn("No measurement given, no contributor will be created.");
            return contributors;
        }

        for (SensitivityMatrixContributorFactory factory : this.factories) {
            contributors.addAll(factory.createContributors(measurement));
        }
        return contributors;
    }

}
