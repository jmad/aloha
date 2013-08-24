package cern.accsoft.steering.aloha.app;

import cern.accsoft.steering.aloha.meas.data.HelperDataType;

public interface HelperDataManagerListener {

	/**
	 * fired when new data was put to the {@link HelperDataManager}
	 * 
	 * @param dataType
	 *            the type of the newly put Data
	 */
	public void putData(HelperDataType dataType);

	/**
	 * fired when the active HelperData changed
	 * 
	 * @param activeDataType
	 *            the type of the new activeData
	 */
	public void changedActiveHelperData(HelperDataType activeDataType);

}
