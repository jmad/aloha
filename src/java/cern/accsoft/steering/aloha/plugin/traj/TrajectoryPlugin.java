/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.traj;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.AbstractAlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;
import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import cern.accsoft.steering.aloha.plugin.traj.display.TrajectoryDisplaySet;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.read.yasp.YaspTrajectoryMeasurementReader;
import cern.accsoft.steering.aloha.plugin.traj.sensity.TrajectorySensityMatrixContributor;
import cern.accsoft.steering.aloha.read.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * This plugin provides components to analyze trajectory/orbit data from yasp
 * 
 * @author kfuchsbe
 * 
 */
public class TrajectoryPlugin extends AbstractAlohaPlugin implements
		ReaderProvider, DisplaySetFactory, AlohaBeanFactoryAware,
		SensitivityMatrixContributorFactory {

	/** the readers which this plugin provides */
	private List<Reader> readers = new ArrayList<Reader>();

	/**
	 * the init method, which shall be called automatically by the
	 * {@link AlohaBeanFactory}
	 */
	@InitMethod
	public void init() {
		this.readers.add(getAlohaBeanFactory().create(
				YaspTrajectoryMeasurementReader.class));
	}

	@Override
	public String getName() {
		return "Yasp trajectory/orbit analysis";
	}

	@Override
	public List<Reader> getReaders() {
		return this.readers;
	}

	@Override
	public DisplaySet createDisplaySet(Measurement measurement) {
		if (measurement instanceof TrajectoryMeasurement) {
			TrajectoryDisplaySet displaySet = new TrajectoryDisplaySet(
					(TrajectoryMeasurement) measurement);
			getAlohaBeanFactory().configure(displaySet);
			return displaySet;
		}
		return null;
	}

	@Override
	public List<SensitivityMatrixContributor> createContributors(
			Measurement measurement) {
		List<SensitivityMatrixContributor> contributors = new ArrayList<SensitivityMatrixContributor>();

		if (measurement instanceof TrajectoryMeasurement) {
			TrajectorySensityMatrixContributor contributor = getAlohaBeanFactory()
					.create(TrajectorySensityMatrixContributor.class);
			contributor
					.setTrajectoryMeasurement((TrajectoryMeasurement) measurement);
			contributors.add(contributor);
		}
		return contributors;
	}
}
