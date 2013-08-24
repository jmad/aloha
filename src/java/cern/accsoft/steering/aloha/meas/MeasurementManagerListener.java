/**
 * 
 */
package cern.accsoft.steering.aloha.meas;

/**
 * This is the interface of a listener to a MeasurementsManager
 * 
 * @author kfuchsbe
 * 
 */
public interface MeasurementManagerListener {

	/**
	 * fired, when a measurement was added.
	 */
	public void addedMeasurement(Measurement newMeasurement);

	/**
	 * fired, when a measurement was removed.
	 * 
	 * @param removedMeasurement
	 */
	public void removedMeasurement(Measurement removedMeasurement);

	/**
	 * fired, when the active measurement changed.
	 * 
	 * @param measurement
	 *            the newly active measurement (can be null, if the last one is
	 *            removed!)
	 */
	public void changedActiveMeasurement(Measurement activeMeasurement);

}
