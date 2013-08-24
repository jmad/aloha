/**
 * 
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;

/**
 * the implementation of the sensityMatrixContributorFactory
 * 
 * @author kfuchsbe
 * 
 */
public class SensitivityMatrixContributorFactoryManagerImpl implements
		SensitivityMatrixContributorFactoryManager {

	/** The logger for the class */
	private final static Logger logger = Logger
			.getLogger(SensitivityMatrixContributorFactoryManagerImpl.class);

	/** all the available factories */
	private List<SensitivityMatrixContributorFactory> factories = new ArrayList<SensitivityMatrixContributorFactory>();

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

		List<SensitivityMatrixContributor> contributors = new ArrayList<SensitivityMatrixContributor>();
		if (measurement == null) {
			logger
					.warn("No measurement given, no contributor will be created.");
			return contributors;
		}

		for (SensitivityMatrixContributorFactory factory : this.factories) {
			contributors.addAll(factory.createContributors(measurement));
		}
		return contributors;
	}

}
