/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas.data;

import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.meas.data.AbstractDynamicData;
import cern.accsoft.steering.aloha.meas.data.InconsistentDataException;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kfuchsbe
 * 
 */
public class MultiturnDataImpl extends AbstractDynamicData implements
		MultiturnData {

	/** all the dataValues as parsed from the file. */
	private Map<Plane, Map<String, MultiturnDataValue>> dataValues = new HashMap<Plane, Map<String, MultiturnDataValue>>();

	public void addDataValues(List<MultiturnDataValue> dataValues, Plane plane) {
		Map<String, MultiturnDataValue> planeValues = new LinkedHashMap<String, MultiturnDataValue>();
		for (MultiturnDataValue dataValue : dataValues) {
			planeValues.put(dataValue.getKey(), dataValue);
		}
		this.dataValues.put(plane, planeValues);
	}

	@Override
	public List<MultiturnDataValue> getDataValues(Plane plane) {
		return new ArrayList<MultiturnDataValue>(this.dataValues.get(plane)
				.values());
	}

	@Override
	protected void calc() throws InconsistentDataException {
		/* Nothing for the moment. Maybe later to speed up a bit .. */
	}

	/**
	 * returns the values for the active monitors for the given column and plane
	 * 
	 * NOTE: May be <code>null</code>!
	 * 
	 * @param column
	 * @param plane
	 * @return
	 */
	@Override
	public List<Double> getValues(MultiturnVar column, Plane plane) {
		List<Double> values = new ArrayList<Double>();

		for (MultiturnDataValue dataValue : getActiveDataValues(plane)) {
			if (dataValue != null) {
				values.add(dataValue.getValue(column));
			} else {
				/* validity must be false in this case! */
				values.add(0.0);
			}
		}
		return values;
	}

	@Override
	public List<Boolean> getValidityValues(Plane plane) {
		List<Boolean> values = new ArrayList<Boolean>();

		for (MultiturnDataValue dataValue : getActiveDataValues(plane)) {
			if (dataValue != null) {
				values.add(Status.OK.equals(dataValue.getStatus()));
			} else {
				values.add(false);
			}
		}
		return values;
	}

	/**
	 * NOTE the returned list might contain null values!
	 * 
	 * @param plane
	 * @return the dataValues for the active correctors and the active plane
	 */
	private List<MultiturnDataValue> getActiveDataValues(Plane plane) {
		List<Monitor> monitors = getMachineElementsManager().getActiveMonitors(
				plane);
		Map<String, MultiturnDataValue> planeValues = this.dataValues
				.get(plane);
		if (planeValues == null) {
			return ListUtil.createDefaultValueList(monitors.size(), null);
		}

		List<MultiturnDataValue> values = new ArrayList<MultiturnDataValue>();
		for (Monitor monitor : monitors) {
			MultiturnDataValue dataValue = planeValues.get(monitor.getKey());
			values.add(dataValue);
		}
		return values;
	}

	@Override
	public MultiturnDataValue getMonitorValue(String key) {
		for (Map<String, MultiturnDataValue> values : this.dataValues.values()) {
			MultiturnDataValue value = values.get(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	@Override
	public Collection<MultiturnDataValue> getMonitorValues() {
		List<MultiturnDataValue> list = new ArrayList<MultiturnDataValue>();
		for (Plane plane : Plane.values()) {
			List<MultiturnDataValue> planeValues = getDataValues(plane);
			if (planeValues != null) {
				list.addAll(planeValues);
			}
		}
		return list;
	}

	@Override
	public List<Boolean> getValidityValues() {
		List<Boolean> validityValues = new ArrayList<Boolean>(
				getValidityValues(Plane.HORIZONTAL));
		validityValues.addAll(getValidityValues(Plane.VERTICAL));
		return validityValues;
	}

	@Override
	public List<Double> getValues(MultiturnVar var) {
		List<Double> values = new ArrayList<Double>(getValues(var,
				Plane.HORIZONTAL));
		values.addAll(getValues(var, Plane.VERTICAL));
		return values;
	}
}
