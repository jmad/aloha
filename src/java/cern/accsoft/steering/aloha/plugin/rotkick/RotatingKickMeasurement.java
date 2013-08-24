package cern.accsoft.steering.aloha.plugin.rotkick;

import cern.accsoft.steering.aloha.meas.BuiltinMeasurementType;
import cern.accsoft.steering.aloha.meas.GenericMeasurementImpl;
import cern.accsoft.steering.aloha.meas.MeasurementType;
import cern.accsoft.steering.aloha.model.ModelDelegate;

public class RotatingKickMeasurement extends
		GenericMeasurementImpl<RotatingKickData> {

	public RotatingKickMeasurement(String name, ModelDelegate modelDelegate,
			RotatingKickData data, CombinedRotatingKickData combinedData,
			ModelRotatingKickData modelData) {
		super(name, modelDelegate, data);
	}

	@Override
	public MeasurementType getType() {
		return BuiltinMeasurementType.ROTATING_KICK;
	}

}
