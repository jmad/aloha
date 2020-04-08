/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.traj.meas;

import cern.accsoft.steering.aloha.meas.BuiltinMeasurementType;
import cern.accsoft.steering.aloha.meas.GenericMeasurementImpl;
import cern.accsoft.steering.aloha.meas.MeasurementType;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.CombinedTrajectoryData;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;

/**
 * @author kfuchsbe
 * 
 */
public class TrajectoryMeasurementImpl extends
		GenericMeasurementImpl<TrajectoryData> implements TrajectoryMeasurement {

	private CombinedTrajectoryData combinedData;

	public TrajectoryMeasurementImpl(String name, ModelDelegate modelDelegate,
			TrajectoryData data, CombinedTrajectoryData combinedData) {
		super(name, modelDelegate, data);
		this.combinedData = combinedData;
	}

	@Override
	public MeasurementType getType() {
		return BuiltinMeasurementType.TRAJECTORY;
	}

	/* (non-Javadoc)
	 * @see cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement#getCombinedData()
	 */
	public CombinedTrajectoryData getCombinedData() {
		return combinedData;
	}

}
