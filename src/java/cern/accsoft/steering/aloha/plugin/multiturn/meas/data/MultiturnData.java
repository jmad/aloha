/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas.data;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.DynamicData;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.yasp.MeasuredData;

/**
 * The data of a multiturn measurement
 * 
 * @author kfuchsbe
 * 
 */
public interface MultiturnData extends DynamicData,
		MeasuredData<MultiturnDataValue> {

	/**
	 * @return all the datavalues for the given plane
	 */
	public List<MultiturnDataValue> getDataValues(Plane plane);

	/**
	 * @param plane
	 * @return the validity values for the active monitors of the given plane.
	 */
	public List<Boolean> getValidityValues(Plane plane);

	/**
	 * @return the validityValues for both planes
	 */
	public List<Boolean> getValidityValues();
	
	public List<Double> getValues(MultiturnVar column, Plane plane);
	
	public List<Double> getValues(MultiturnVar var);
	
	

}
