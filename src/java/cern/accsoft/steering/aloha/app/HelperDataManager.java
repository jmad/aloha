/*
 * $Id: WorkingSet.java,v 1.10 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.10 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.app;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.Data;
import cern.accsoft.steering.aloha.meas.data.HelperData;
import cern.accsoft.steering.aloha.meas.data.HelperDataType;

/**
 * This interface is the interface of a class, that keeps track of actually
 * loaded helper-data (which might be used by some or all measurements)
 * 
 * @author kfuchsbe
 * 
 */
public interface HelperDataManager {

	/**
	 * add data to the workingset.
	 * 
	 * @param data
	 */
	public void putData(HelperData data);

	/**
	 * retrieve data of the given type, or null if not available.
	 * 
	 * @param type
	 *            the type of the data to retrieve
	 * @return the data
	 */
	public Data getData(HelperDataType type);

	/**
	 * @return all the {@link HelperDataType}s for which data is available
	 */
	public List<HelperDataType> getDataTypes();

	/**
	 * sets the data of tyhe given type as active one.
	 * 
	 * @param dataType
	 */
	public void setActiveHelperData(HelperDataType dataType);
	
	/**
	 * @return the actually active Helper-Data
	 */
	public Data getActiveHelperData();
	
	/**
	 * adds a listener
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addListener(HelperDataManagerListener listener);

	/**
	 * removes a listener
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(HelperDataManagerListener listener);
}
