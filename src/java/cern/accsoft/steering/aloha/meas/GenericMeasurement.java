/**
 * 
 */
package cern.accsoft.steering.aloha.meas;

/**
 * the generic version of a measurement
 * 
 * @author kfuchsbe
 * 
 */
public interface GenericMeasurement<T> extends ModelAwareMeasurement {

	/**
	 * The measured Data
	 * 
	 * @return the data of the measurement
	 */
	public T getData();
}
