package cern.accsoft.steering.aloha.plugin.disp.meas;

import cern.accsoft.steering.aloha.meas.GenericMeasurement;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.CombinedDispersionData;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.DispersionData;

public interface DispersionMeasurement extends
		GenericMeasurement<DispersionData> {

	public abstract CombinedDispersionData getCombinedData();

}