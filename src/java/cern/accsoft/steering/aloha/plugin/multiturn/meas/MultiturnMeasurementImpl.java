/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas;

import cern.accsoft.steering.aloha.meas.GenericMeasurementImpl;
import cern.accsoft.steering.aloha.meas.MeasurementType;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDifferenceData;

/**
 * @author kfuchsbe
 * 
 */
public class MultiturnMeasurementImpl extends
		GenericMeasurementImpl<MultiturnData> implements MultiturnMeasurement {

	private MultiturnDifferenceData diffData;

	public MultiturnMeasurementImpl(String name, ModelDelegate modelDelegate,
			MultiturnData data, MultiturnDifferenceData diffData) {
		super(name, modelDelegate, data);
		this.diffData = diffData;
	}

	@Override
	public MeasurementType getType() {
		return MultiturnMeasurementType.MULTITURN_DATA;
	}

	@Override
	public MultiturnDifferenceData getDiffData() {
		return this.diffData;
	}

}
