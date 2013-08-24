/*
 * $Id: DynamicData.java,v 1.1 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.meas.data;

/**
 * this is the general interface of a data, that may change
 * 
 * @author kfuchsbe
 * 
 */
public interface DynamicData extends Data {

	/**
	 * @param listener
	 *            the listener to add
	 */
	public void addListener(DynamicDataListener listener);

	/**
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(DynamicDataListener listener);
}
