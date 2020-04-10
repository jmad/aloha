/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn;

import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.AbstractAlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;
import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import cern.accsoft.steering.aloha.plugin.multiturn.display.MultiturnDisplaySet;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.MultiturnMeasurement;
import cern.accsoft.steering.aloha.plugin.multiturn.read.MultiturnMeasurementReaderImpl;
import cern.accsoft.steering.aloha.plugin.multiturn.sensitivity.MultiturnBetaSensitivityMatrixContributor;
import cern.accsoft.steering.aloha.read.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * The plugin for aloha that handles multiturn data
 * 
 * @author kfuchsbe
 * 
 */
public class MultiturnPlugin extends AbstractAlohaPlugin implements
		ReaderProvider, DisplaySetFactory, SensitivityMatrixContributorFactory {

	/** all the provided readers */
	private List<Reader> readers = new ArrayList<Reader>();

	@Override
	public String getName() {
		return "Multiturn data analysis";
	}

	@InitMethod
	public void init() {
		this.readers.add(getAlohaBeanFactory().create(
				MultiturnMeasurementReaderImpl.class));
	}

	@Override
	public List<Reader> getReaders() {
		return this.readers;
	}

	@Override
	public DisplaySet createDisplaySet(Measurement measurement) {
		if (measurement instanceof MultiturnMeasurement) {
			MultiturnDisplaySet displaySet = new MultiturnDisplaySet(
					(MultiturnMeasurement) measurement);
			getAlohaBeanFactory().configure(displaySet);
			return displaySet;
		}
		return null;
	}

	@Override
	public List<SensitivityMatrixContributor> createContributors(
			Measurement measurement) {
		List<SensitivityMatrixContributor> contributors = new ArrayList<SensitivityMatrixContributor>();
		if (measurement instanceof MultiturnMeasurement) {
			SensitivityMatrixContributor contributor = new MultiturnBetaSensitivityMatrixContributor(
					(MultiturnMeasurement) measurement);
			getAlohaBeanFactory().configure(contributor);
			contributors.add(contributor);
		}
		return contributors;
	}

}
