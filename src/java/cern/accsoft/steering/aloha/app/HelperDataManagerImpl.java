package cern.accsoft.steering.aloha.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.accsoft.steering.aloha.meas.data.Data;
import cern.accsoft.steering.aloha.meas.data.HelperData;
import cern.accsoft.steering.aloha.meas.data.HelperDataType;

public class HelperDataManagerImpl implements HelperDataManager {

	/** All the listeners to the HelperDataManager */
	private ArrayList<HelperDataManagerListener> listeners = new ArrayList<HelperDataManagerListener>();

	/** various global data, (trim, alignment ...) */
	private Map<HelperDataType, HelperData> datas = new HashMap<HelperDataType, HelperData>();

	/** just a list of the available datatypes, to always get the same order. */
	private List<HelperDataType> dataTypes = new ArrayList<HelperDataType>();

	/** the dataType of the active HelperData */
	private HelperDataType activeDataType = null;

	/*
	 * method for listener - handling
	 */

	public void addListener(HelperDataManagerListener listener) {
		listeners.add(listener);
	}

	public void removeListener(HelperDataManagerListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Data getData(HelperDataType type) {
		return this.datas.get(type);
	}

	@Override
	public void putData(HelperData data) {
		this.datas.put(data.getType(), data);
		this.dataTypes.remove(data.getType());
		this.dataTypes.add(data.getType());
		firePutData(data.getType());
	}

	/**
	 * notifies all listeners, that new data was added
	 * 
	 * @param type
	 *            the type of the newly added data
	 */
	private void firePutData(HelperDataType dataType) {
		for (HelperDataManagerListener listener : listeners) {
			listener.putData(dataType);
		}
	}

	@Override
	public List<HelperDataType> getDataTypes() {
		return this.dataTypes;
	}

	@Override
	public Data getActiveHelperData() {
		return getData(this.activeDataType);
	}

	@Override
	public void setActiveHelperData(HelperDataType dataType) {
		this.activeDataType = dataType;
	}

}
