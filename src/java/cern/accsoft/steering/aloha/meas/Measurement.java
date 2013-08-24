package cern.accsoft.steering.aloha.meas;

public interface Measurement {

	/**
	 * @return a name of the measurement
	 */
	public abstract String getName();

	/**
	 * @return the type of the measurement
	 */
	public abstract MeasurementType getType();

}