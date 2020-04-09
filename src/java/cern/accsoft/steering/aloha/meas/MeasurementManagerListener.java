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
	default void addedMeasurement(Measurement newMeasurement) {

	}

	/**
	 * fired, when a measurement was removed.
	 * 
	 * @param removedMeasurement
	 */
	default void removedMeasurement(Measurement removedMeasurement) {

	}

	/**
	 * fired, when the active measurement changed.
	 * 
	 * @param activeMeasurement
	 *            the newly active measurement (can be null, if the last one is
	 *            removed!)
	 */
	default void changedActiveMeasurement(Measurement activeMeasurement) {

	}

}
