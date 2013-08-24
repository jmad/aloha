package cern.accsoft.steering.aloha.plugin.traj.meas.data;

import java.util.List;

import Jama.Matrix;
import cern.accsoft.steering.aloha.meas.data.DynamicData;
import cern.accsoft.steering.util.meas.data.Plane;

public interface CombinedTrajectoryData extends DynamicData {

	/**
	 * returns the difference in pos measurement-model for each monitor,
	 * normalized over sqrt(beta)
	 * 
	 * @param plane
	 *            the plane for which to get the data
	 * @return the data
	 */
	public List<Double> getMonitorNormalizedPosDiff(Plane plane);

	/**
	 * returns the normalized rms of the measured dispersion values for each
	 * monitor
	 * 
	 * @param plane
	 *            the plane for which to get the data
	 * @return the data
	 */
	public List<Double> getMonitorNormalizedPosRms(Plane plane);

	/**
	 * @param plane
	 * @return the difference in pos meas-model for each monitor in the given
	 *         plane
	 */
	public List<Double> getNoisyMonitorPosDiff(Plane plane);

	/**
	 * @return the difference-vector between meas-model
	 */
	public Matrix getNoisyDifferenceVector();
}
