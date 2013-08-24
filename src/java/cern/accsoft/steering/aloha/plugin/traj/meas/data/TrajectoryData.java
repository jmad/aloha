/*
 * $Id: StabilityData.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.traj.meas.data;

import java.util.List;

import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.meas.data.DynamicData;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * 
 * 
 * This is the general interface for storing noise-data
 * 
 * @author kfuchsbe
 * 
 */
public interface TrajectoryData extends DynamicData {

	/**
	 * the mean position value for the given monitor
	 * 
	 * @param monitor
	 *            the monitor for which to retrieve the value
	 * @return the mean position value
	 */
	public double getMeanValue(Monitor monitor);

	/**
	 * @param monitor
	 *            the monitor for which to return the noise
	 * @return the noise for the given monitor
	 */
	public double getRmsValue(Monitor monitor);

	/**
	 * get all the position mean values for the active monitors
	 * 
	 * @return the position values
	 */
	public List<Double> getMeanValues();

	/**
	 * all the mean values for monitors of the given plane
	 * 
	 * @param plane
	 *            the plane for which to get the mean values
	 * @return the mean values
	 */
	public List<Double> getMeanValues(Plane plane);

	/**
	 * get the noise-values for the active monitors
	 * 
	 * @return all nois-values for the active monitors
	 */
	public List<Double> getRmsValues();

	/**
	 * the monitor rms-values for the given plane
	 * 
	 * @param plane
	 *            the plane for which to retrieve the rms-values
	 * @return the rms values
	 */
	public List<Double> getRmsValues(Plane plane);

	/**
	 * @return the average noise over all the active monitors
	 */
	public double getAverageRms();

	/**
	 * @return a list of booleans, indicating if the data for the given monitor
	 *         is valid, or not.
	 */
	public List<Boolean> getValidityValues();

	/**
	 * @param plane
	 * @return the validity values for all active monitors in the given plane
	 */
	public List<Boolean> getValidityValues(Plane plane);

}
