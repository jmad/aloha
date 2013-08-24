package cern.accsoft.steering.aloha.meas.data;

import cern.accsoft.steering.aloha.meas.MeasurementException;

public class InconsistentDataException extends
		MeasurementException {
	private static final long serialVersionUID = -2085409419448575089L;

	public InconsistentDataException(String message) {
		super(message);
	}

	public InconsistentDataException(Throwable cause) {
		super(cause);
	}

	public InconsistentDataException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
