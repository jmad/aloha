/*
 * $Id: DispersionData.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.meas.data;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.DynamicData;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * this is the general interface for representing DispersionData
 * 
 * @author kfuchsbe
 * 
 */
public interface DispersionData extends DynamicData {

	/**
	 * returns the dispersion-values for each active monitor of the given plane.
	 * 
	 * @param plane
	 *            the plane for which to retrieve the dispersionData
	 * @return the dispersion-values.
	 */
	public List<Double> getValues(Plane plane);

	/**
	 * returns the dispersion-rms values for all active bpms.
	 * 
	 * @param plane
	 *            the Plane for which to retrieve the rms-values
	 * @return the rms-values for each bpm in the active plane.
	 */
	public List<Double> getRms(Plane plane);

	/**
	 * returns validity values for each monitor of the given plane
	 * 
	 * @param plane
	 *            the plane for which to get the validity
	 * @return boolean value for each active monitor, true if the value is
	 *         valid, false if not.
	 */
	public List<Boolean> getValidity(Plane plane);

	/**
	 * @return the validity-values for both planes.
	 */
	public List<Boolean> getValidity();

	/**
	 * @return the rms-data for both planes
	 */
	public List<Double> getRms();
}
