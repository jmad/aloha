/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas.data;

import java.util.List;

import cern.accsoft.steering.aloha.plugin.multiturn.meas.MultiturnMeasurement;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * @author kfuchsbe
 * 
 */
public class MultiturnDifferenceDataImpl implements MultiturnDifferenceData {

	private MultiturnMeasurement measurement;

	@Override
	public List<Double> getBeatingValues(MultiturnVar column, Plane plane) {
		List<Double> diff = getDiffValues(column, plane);
		if ((MultiturnVar.BETA.equals(column) || (MultiturnVar.BETA_ERROR
				.equals(column)))) {
			return ListUtil.divide(diff, getMeasurement().getModelDelegate()
					.getModelOpticsData().getMonitorBetas(plane));
		} else {
			return ListUtil.createDefaultValueList(getMeasurement()
					.getMachineElementsManager().getActiveMonitorsCount(), 0.0);
		}
	}

	@Override
	public List<Double> getDiffValues(MultiturnVar column, Plane plane) {
		List<Double> meas = getMeasurement().getData().getValues(column, plane);
		if (MultiturnVar.BETA.equals(column)) {
			return ListUtil.diff(meas, getMeasurement().getModelDelegate()
					.getModelOpticsData().getMonitorBetas(plane));

		} else if (MultiturnVar.BETA_ERROR.equals(column)) {
			return meas;
		} else {
			return ListUtil.createDefaultValueList(getMeasurement()
					.getMachineElementsManager().getActiveMonitorsCount(), 0.0);
		}

	}

	public void setMeasurement(MultiturnMeasurement measurement) {
		this.measurement = measurement;
	}

	private MultiturnMeasurement getMeasurement() {
		return measurement;
	}

}
