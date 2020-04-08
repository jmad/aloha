/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.meas.Measurement;

import java.util.List;

/**
 * @author kfuchsbe
 * 
 */
public interface SensitivityMatrixContributorFactory extends AlohaPlugin {

	/**
	 * creates one or more {@link SensitivityMatrixContributor}s for the given
	 * measurement
	 * 
	 * @param measurement
	 *            the measurement for which to create the
	 *            {@link SensitivityMatrixContributor}s
	 * @return a list of {@link SensitivityMatrixContributor}s
	 */
	public List<SensitivityMatrixContributor> createContributors(
			Measurement measurement);

}
