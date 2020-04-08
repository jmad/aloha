/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas.data;

import cern.accsoft.steering.util.meas.data.Plane;

import java.util.List;

/**
 * The difference data between measurement and model
 * 
 * @author kfuchsbe
 * 
 */
public interface MultiturnDifferenceData {

	/**
	 * the difference values between measurement-model
	 * 
	 * @param column
	 * @param plane
	 * @return
	 */
	public List<Double> getDiffValues(MultiturnVar column, Plane plane);

	/**
	 * the diefference measurement-model normalized by model values
	 * 
	 * @param column
	 * @param plane
	 * @return
	 */
	public List<Double> getBeatingValues(MultiturnVar column, Plane plane);

}
