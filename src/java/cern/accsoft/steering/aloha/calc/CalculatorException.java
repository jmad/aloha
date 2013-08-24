package cern.accsoft.steering.aloha.calc;

import cern.accsoft.steering.aloha.app.AppException;

public class CalculatorException extends AppException {


	public CalculatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public CalculatorException(String message) {
		super(message);
	}

	public CalculatorException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
