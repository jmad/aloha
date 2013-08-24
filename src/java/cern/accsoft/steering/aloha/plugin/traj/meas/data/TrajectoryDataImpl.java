/*
 * $Id: StabilityDataImpl.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.traj.meas.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.meas.data.AbstractDynamicData;
import cern.accsoft.steering.aloha.meas.data.InconsistentDataException;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * @author kfuchsbe
 * 
 */
public class TrajectoryDataImpl extends AbstractDynamicData
		implements TrajectoryData {

	/** all the available position values */
	private HashMap<String, Double> meanValues = new HashMap<String, Double>();

	/** all available noise-values for the monitors */
	private HashMap<String, Double> noiseValues = new HashMap<String, Double>();

	/**
	 * determines if the noise-value for a monitor is a real calculated one
	 * (true) or just set to 0, because it could not be correctly determined.
	 */
	private HashMap<String, Boolean> noiseValid = new HashMap<String, Boolean>();

	@Override
	protected void calc() throws InconsistentDataException {
		/*
		 * nothing to do. The lists are combined on demand ... maybe some
		 * optimisation possible
		 */
	}

	/**
	 * adds the noise - value to the data
	 * 
	 * @param monitorKey
	 *            the key of the monitor, which this noise-value refers to
	 * @param noise
	 *            the noise-value
	 * @param valid
	 *            true, if it is a correctly calculated value, false otherwise
	 */
	public void add(String monitorKey, double pos, double noise, boolean valid) {
		meanValues.put(monitorKey, pos);
		noiseValues.put(monitorKey, noise);
		noiseValid.put(monitorKey, valid);
	}

	/**
	 * @param monitors
	 *            the monitors for which to calculate the rms
	 * @return the average rms over the given monitors
	 */
	public double getAverageRms(List<Monitor> monitors) {
		double sum = 0.0;
		int count = 0;
		for (Monitor monitor : monitors) {
			if (noiseValid.get(monitor.toString()) != null) {
				sum += getRmsValue(monitor);
				count += 1;
			}
		}
		if (count > 0) {
			return sum / count;
		} else {
			return 0;
		}
	}

	/**
	 * @param monitors
	 *            the monitors for which to return the rms-values
	 * @return the rms-values
	 */
	private List<Double> getRmsValues(List<Monitor> monitors) {
		return getMonitorValues(this.noiseValues, monitors);
	}

	private List<Double> getMeanValues(List<Monitor> monitors) {
		return getMonitorValues(this.meanValues, monitors);
	}

	//
	// methods of interface TrajectoryData
	//

	@Override
	public double getAverageRms() {
		return getAverageRms(getActiveMonitors());
	}

	@Override
	public double getMeanValue(Monitor monitor) {
		return getMonitorValue(this.meanValues, monitor);
	}

	@Override
	public double getRmsValue(Monitor monitor) {
		return getMonitorValue(this.noiseValues, monitor);
	}

	/**
	 * extracts the double value from a map and returns it. If the entry for the
	 * given monitor is null then it returns zero for the monitor
	 * 
	 * @param values
	 *            the map from which to extract the values
	 * @param monitor
	 *            the monitor for which to get the value
	 * @return the value
	 */
	private double getMonitorValue(Map<String, Double> values, Monitor monitor) {
		Double noise = values.get(monitor.toString());
		if (noise != null) {
			return noise;
		} else {
			return 0;
		}
	}

	/**
	 * retrieves the values for many monitors
	 * 
	 * @param values
	 * @param monitors
	 * @return
	 */
	private List<Double> getMonitorValues(Map<String, Double> values,
			List<Monitor> monitors) {
		ArrayList<Double> retValues = new ArrayList<Double>();
		for (Monitor monitor : monitors) {
			retValues.add(getMonitorValue(values, monitor));
		}
		return retValues;
	}

	private List<Boolean> getValidityValues(List<Monitor> monitors) {
		List<Boolean> values = new ArrayList<Boolean>();
		for (Monitor monitor : monitors) {
			Boolean value = this.noiseValid.get(monitor.getKey());
			if (value == null) {
				values.add(false);
			} else {
				values.add(value);
			}
		}
		return values;
	}

	@Override
	public List<Double> getRmsValues() {
		return getRmsValues(getActiveMonitors());
	}

	@Override
	public List<Double> getMeanValues() {
		return getMeanValues(getActiveMonitors());
	}

	@Override
	public List<Boolean> getValidityValues() {
		return getValidityValues(getActiveMonitors());
	}

	@Override
	public List<Double> getMeanValues(Plane plane) {
		return getMeanValues(getMachineElementsManager().getActiveMonitors(
				plane));
	}

	@Override
	public List<Double> getRmsValues(Plane plane) {
		return getRmsValues(getMachineElementsManager()
				.getActiveMonitors(plane));
	}

	@Override
	public List<Boolean> getValidityValues(Plane plane) {
		return getValidityValues(getMachineElementsManager().getActiveMonitors(
				plane));
	}

}
