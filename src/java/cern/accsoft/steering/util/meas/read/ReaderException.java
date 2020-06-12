/*
 * $Id: ReaderException.java,v 1.1 2008-12-02 20:57:43 kfuchsbe Exp $
 * 
 * $Date: 2008-12-02 20:57:43 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.util.meas.read;

/**
 * this is the general exception for a reader.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 * 
 */
public class ReaderException extends Exception {
	private static final long serialVersionUID = 1024547356645355638L;

	public ReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReaderException(String message) {
		super(message);
	}

	public ReaderException(Throwable cause) {
		super(cause);
	}

}
