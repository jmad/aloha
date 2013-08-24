/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.xxx;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement;
import cern.accsoft.steering.aloha.plugin.xxx.sensitivity.Ti8CollimatorConstraintsContributor;

/**
 * XXX THIS IS VERY UGLY AND TEMPORARY:
 * <p>
 * A plugin, that a contrinbutor containing collimator phase constraints for
 * Ti8.
 * 
 * 
 * @author kaifox
 * 
 */
public class XXXPlugin implements SensitivityMatrixContributorFactory {

	@Override
	public List<SensitivityMatrixContributor> createContributors(
			Measurement measurement) {
		List<SensitivityMatrixContributor> contributors = new ArrayList<SensitivityMatrixContributor>();
		if (measurement instanceof DispersionMeasurement) {
			if (((DispersionMeasurement) measurement).getModelDelegate()
					.getJMadModel().getModelDefinition().getName().contains(
							"ti8")) {
				contributors.add(new Ti8CollimatorConstraintsContributor(
						(DispersionMeasurement) measurement));
			}
		}
		return contributors;
	}

	@Override
	public String getName() {
		return "XXX";
	}

}
