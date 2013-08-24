/*
 * $Id: ModelDelegateException.java,v 1.1 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.model;

import cern.accsoft.steering.aloha.app.AppException;

/**
 * @author kfuchsbe
 * 
 */
public class ModelDelegateException extends AppException {
	private static final long serialVersionUID = -7995375022002419181L;

	public ModelDelegateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelDelegateException(String message) {
		super(message);
	}

	public ModelDelegateException(Throwable cause) {
		super(cause);
	}

}
