package cern.accsoft.steering.aloha.meas;

import cern.accsoft.steering.aloha.app.AppException;

public class MeasurementException extends AppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MeasurementException(String message) {
		super(message);
	}

	public MeasurementException(Throwable cause) {
		super(cause);
	}

	public MeasurementException(String message, Throwable cause) {
		super(message, cause);
	}

}
