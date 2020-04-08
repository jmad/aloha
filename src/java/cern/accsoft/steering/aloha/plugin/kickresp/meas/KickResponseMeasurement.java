package cern.accsoft.steering.aloha.plugin.kickresp.meas;

import cern.accsoft.steering.aloha.meas.GenericMeasurement;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CombinedKickResponseData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.KickResponseData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.ModelKickResponseData;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurementImpl;

public interface KickResponseMeasurement extends
		GenericMeasurement<KickResponseData> {

	/**
	 * sets the trajectory measurement to use as stability data
	 * 
	 * @param data
	 */
	public abstract void setStabilityMeasurement(TrajectoryMeasurementImpl data);

	public abstract TrajectoryMeasurementImpl getStabilityMeasurement();

	public abstract CombinedKickResponseData getCombinedData();

	public abstract ModelKickResponseData getModelData();

}