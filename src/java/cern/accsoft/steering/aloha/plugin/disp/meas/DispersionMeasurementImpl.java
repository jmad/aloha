/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.disp.meas;

import cern.accsoft.steering.aloha.meas.BuiltinMeasurementType;
import cern.accsoft.steering.aloha.meas.GenericMeasurementImpl;
import cern.accsoft.steering.aloha.meas.MeasurementType;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.CombinedDispersionData;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.CombinedDispersionDataImpl;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.DispersionData;

/**
 * the measurement for dispersion data
 * 
 * @author kfuchsbe
 * 
 */
public class DispersionMeasurementImpl extends
		GenericMeasurementImpl<DispersionData> implements DispersionMeasurement {

	private CombinedDispersionData combinedData;

	public DispersionMeasurementImpl(String name, ModelDelegate modelDelegate,
			DispersionData data, CombinedDispersionData combinedData) {
		super(name, modelDelegate, data);
		this.combinedData = combinedData;
		if (combinedData instanceof CombinedDispersionDataImpl) {
			((CombinedDispersionDataImpl) combinedData)
					.setDispersionMeasurement(this);
		}
	}

	@Override
	public MeasurementType getType() {
		return BuiltinMeasurementType.DISPERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement#
	 * getCombinedData()
	 */
	public CombinedDispersionData getCombinedData() {
		return combinedData;
	}

}
