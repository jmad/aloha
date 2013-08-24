/*
 * $Id: DynamicDataListener.java,v 1.1 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.meas.data;

import cern.accsoft.steering.aloha.model.data.ModelOpticsData;

/**
 * this interface represents a listener to a {@link ModelOpticsData}
 * 
 * @author kfuchsbe
 * 
 */
public interface DynamicDataListener {

	/** fired, when the data changed */
	public void becameDirty();
}
