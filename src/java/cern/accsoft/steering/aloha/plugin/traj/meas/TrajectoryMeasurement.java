package cern.accsoft.steering.aloha.plugin.traj.meas;

import cern.accsoft.steering.aloha.meas.GenericMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.CombinedTrajectoryData;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;

public interface TrajectoryMeasurement extends
		GenericMeasurement<TrajectoryData> {

	public abstract CombinedTrajectoryData getCombinedData();

}