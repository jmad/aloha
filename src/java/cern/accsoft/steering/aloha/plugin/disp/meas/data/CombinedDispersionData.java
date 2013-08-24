/*
 * $Id: CombinedDispersionData.java,v 1.3 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.3 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.meas.data;

import java.util.List;

import Jama.Matrix;
import cern.accsoft.steering.aloha.meas.data.DynamicData;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * this interface provides data, which is combined from model and measurement
 * 
 * @author kfuchsbe
 * 
 */
public interface CombinedDispersionData extends DynamicData {

	/**
	 * returns the difference in dispersion measurement-model for each monitor,
	 * normalized over sqrt(beta)
	 * 
	 * @param plane
	 *            the plane for which to get the data
	 * @return the data
	 */
	public List<Double> getMonitorNormalizedDispersionDiff(Plane plane);

	/**
	 * returns the normalized rms of the measured dispersion values for each
	 * monitor
	 * 
	 * @param plane
	 *            the plane for which to get the data
	 * @return the data
	 */
	public List<Double> getMonitorNormalizedDispersionRms(Plane plane);

	/**
	 * @param plane
	 * @return the difference in dispersion meas-model for each monitor in the
	 *         given plane
	 */
	public List<Double> getNoisyMonitorDispersionDiff(Plane plane);

	/**
	 * @return the difference-vector between meas-model
	 */
	public Matrix getNoisyDifferenceVector();
}
