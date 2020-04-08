/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.traj.meas.data;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.meas.data.AbstractDynamicData;
import cern.accsoft.steering.aloha.meas.data.DynamicDataListener;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement;
import cern.accsoft.steering.jmad.util.MatrixUtil;
import cern.accsoft.steering.util.meas.data.Plane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kfuchsbe
 * 
 */
public class CombinedTrajectoryDataImpl extends AbstractDynamicData implements
		CombinedTrajectoryData, NoiseWeighterAware {

	/** the map which contains the calculated values */
	private Map<String, List<Double>> valuesMap = new HashMap<String, List<Double>>();

	/** the measurement used for the calculations */
	private TrajectoryMeasurement measurement;

	/** the nois-weighter injected by {@link AlohaBeanFactory} */
	private NoiseWeighter noiseWeighter;

	/**
	 * the listener, which we add to both, the model data and the measurement
	 * data, in order to get notified about changes.
	 */
	private DynamicDataListener dataListener = new DynamicDataListener() {
		@Override
		public void becameDirty() {
			setDirty(true);
		}
	};

	/**
	 * the setter for the measurement-object.
	 * 
	 * @param measurement
	 */
	public void setMeasurement(TrajectoryMeasurement measurement) {
		this.measurement = measurement;
		this.measurement.getData().addListener(this.dataListener);
		this.measurement.getModelDelegate().getModelOpticsData().addListener(
				this.dataListener);
	}

	/**
	 * this enum just defines prefixes for the hashmap-keys
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private enum KeyPrefix {
		NOISY_DIFFERENCE, NORMALIZED_DIFFERENCE, NORMALIZED_RMS;
	}

	@Override
	protected void calc() {
		valuesMap.clear();

		ModelOpticsData modelOpticsData = getMeasurement().getModelDelegate()
				.getModelOpticsData();

		for (Plane plane : Plane.values()) {
			List<Double> diffData = new ArrayList<Double>();
			List<Double> normalizeDiffData = new ArrayList<Double>();
			List<Double> normalizedRmsData = new ArrayList<Double>();

			TrajectoryData dispersionData = getMeasurement().getData();

			if (dispersionData != null) {
				List<Double> posValuesMeas = dispersionData
						.getMeanValues(plane);
				List<Double> posRmsMeas = dispersionData.getRmsValues(plane);
				List<Double> posValuesModel = modelOpticsData
						.getMonitorPos(plane);
				List<Double> betas = modelOpticsData.getMonitorBetas(plane);

				for (int i = 0; i < posValuesMeas.size(); i++) {
					double value = (posValuesMeas.get(i) - posValuesModel
							.get(i));
					double sqrtBeta = Math.sqrt(betas.get(i));
					diffData.add(getNoiseWeighter().calcNoisyValue(value,
							posRmsMeas.get(i)));
					normalizeDiffData.add(value / sqrtBeta);
					normalizedRmsData.add(posRmsMeas.get(i) / sqrtBeta);
				}
			}
			this.valuesMap.put(createKey(KeyPrefix.NOISY_DIFFERENCE, plane),
					diffData);
			this.valuesMap.put(
					createKey(KeyPrefix.NORMALIZED_DIFFERENCE, plane),
					normalizeDiffData);
			this.valuesMap.put(createKey(KeyPrefix.NORMALIZED_RMS, plane),
					normalizedRmsData);
		}
	}

	@Override
	public List<Double> getMonitorNormalizedPosDiff(Plane plane) {
		ensureUpToDate();
		return getValues(KeyPrefix.NORMALIZED_DIFFERENCE, plane);
	}

	@Override
	public List<Double> getMonitorNormalizedPosRms(Plane plane) {
		ensureUpToDate();
		return getValues(KeyPrefix.NORMALIZED_RMS, plane);
	}

	@Override
	public Matrix getNoisyDifferenceVector() {
		ensureUpToDate();

		List<Double> values = new ArrayList<Double>();
		values.addAll(getNoisyMonitorPosDiff(Plane.HORIZONTAL));
		values.addAll(getNoisyMonitorPosDiff(Plane.VERTICAL));
		return MatrixUtil.createVector(values);
	}

	@Override
	public List<Double> getNoisyMonitorPosDiff(Plane plane) {
		ensureUpToDate();
		return getValues(KeyPrefix.NOISY_DIFFERENCE, plane);
	}

	private List<Double> getValues(KeyPrefix prefix, Plane plane) {
		return this.valuesMap.get(createKey(prefix, plane));
	}

	/**
	 * create a key for the hashmap
	 * 
	 * @param prefix
	 *            the prefix to use
	 * @param plane
	 *            the plane
	 * @return the key
	 */
	private String createKey(KeyPrefix prefix, Plane plane) {
		return prefix + "-" + plane;
	}

	private TrajectoryMeasurement getMeasurement() {
		return measurement;
	}

	@Override
	public void setNoiseWeighter(NoiseWeighter noiseWeighter) {
		this.noiseWeighter = noiseWeighter;
	}

	private NoiseWeighter getNoiseWeighter() {
		return this.noiseWeighter;
	}

}
