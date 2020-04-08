/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas;

import cern.accsoft.steering.aloha.meas.BuiltinMeasurementType;
import cern.accsoft.steering.aloha.meas.GenericMeasurementImpl;
import cern.accsoft.steering.aloha.meas.MeasurementType;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CombinedKickResponseData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.KickResponseData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.ModelKickResponseData;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurementImpl;

import java.lang.ref.WeakReference;

/**
 * @author kfuchsbe
 * 
 */
public class KickResponseMeasurementImpl extends
		GenericMeasurementImpl<KickResponseData> implements KickResponseMeasurement {

	private CombinedKickResponseData combinedData;
	private ModelKickResponseData modelData;

	/**
	 * the trajectory data to use for stability calculations (error bars)
	 */
	private WeakReference<TrajectoryMeasurementImpl> stabilityMeasurement;

	public KickResponseMeasurementImpl(String name, ModelDelegate modelDelegate,
			KickResponseData data, CombinedKickResponseData combinedData,
			ModelKickResponseData modelData) {
		super(name, modelDelegate, data);
		this.combinedData = combinedData;
		this.modelData = modelData;
	}

	/* (non-Javadoc)
	 * @see cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement#setStabilityMeasurement(cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement)
	 */
	public void setStabilityMeasurement(TrajectoryMeasurementImpl data) {
		this.stabilityMeasurement = new WeakReference<TrajectoryMeasurementImpl>(
				data);
	}

	/* (non-Javadoc)
	 * @see cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement#getStabilityMeasurement()
	 */
	public TrajectoryMeasurementImpl getStabilityMeasurement() {
		if (this.stabilityMeasurement == null) {
			return null;
		}
		return this.stabilityMeasurement.get();
	}

	/* (non-Javadoc)
	 * @see cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement#getType()
	 */
	@Override
	public final MeasurementType getType() {
		return BuiltinMeasurementType.KICK_RESPONSE;
	}

	/* (non-Javadoc)
	 * @see cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement#getCombinedData()
	 */
	public CombinedKickResponseData getCombinedData() {
		return combinedData;
	}

	/* (non-Javadoc)
	 * @see cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement#getModelData()
	 */
	public ModelKickResponseData getModelData() {
		return modelData;
	}

}
