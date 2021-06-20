package cern.accsoft.steering.aloha.meas;

public interface Measurement {

	/**
	 * @return a name of the measurement
	 */
	String getName();

	/**
	 * @return the type of the measurement
	 */
	MeasurementType getType();

}