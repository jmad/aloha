/*
 * $Id: CsvReaderException.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.read.csv;

import cern.accsoft.steering.util.meas.read.ReaderException;

/**
 * @author kfuchsbe
 * 
 */
public class CsvReaderException extends ReaderException {
	private static final long serialVersionUID = 5982283429727937771L;

	public CsvReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public CsvReaderException(String message) {
		super(message);
	}

	public CsvReaderException(Throwable cause) {
		super(cause);
	}

}
