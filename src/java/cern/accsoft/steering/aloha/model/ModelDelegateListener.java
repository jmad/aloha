/*
 * $Id: ModelDelegateListener.java,v 1.1 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.model;

/**
 * @author kfuchsbe
 * 
 */
public interface ModelDelegateListener {

	/**
	 * fired, when the model changed. This means in most times, that all current
	 * data is invalid.
	 */
	public void becameDirty();
}
