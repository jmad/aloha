/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.trim.meas;

import cern.accsoft.steering.aloha.meas.BuiltinMeasurementType;
import cern.accsoft.steering.aloha.meas.GenericMeasurementImpl;
import cern.accsoft.steering.aloha.meas.MeasurementType;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.trim.meas.data.TrimData;

/**
 * @author kfuchsbe
 * 
 */
public class TrimMeasurementImpl extends GenericMeasurementImpl<TrimData>
		implements TrimMeasurement {

	public TrimMeasurementImpl(String name, ModelDelegate modelDelegate,
			TrimData data) {
		super(name, modelDelegate, data);
	}

	@Override
	public MeasurementType getType() {
		return BuiltinMeasurementType.TRIM;
	}

}
