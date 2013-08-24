/*
 * $Id: SolverException.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.solve;

import cern.accsoft.steering.aloha.app.AppException;

/**
 * @author kfuchsbe
 * 
 */
public class SolverException extends AppException {
	private static final long serialVersionUID = -829846974612259916L;

	public SolverException(String message, Throwable cause) {
		super(message, cause);
	}

	public SolverException(String message) {
		super(message);
	}

	public SolverException(Throwable cause) {
		super(cause);
	}

}
