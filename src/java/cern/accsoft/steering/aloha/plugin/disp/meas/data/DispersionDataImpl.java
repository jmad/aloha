/*
 * $Id: DispersionDataImpl.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.meas.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.meas.data.AbstractDynamicData;
import cern.accsoft.steering.aloha.meas.data.InconsistentDataException;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.yasp.MonitorValue;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;

/**
 * this is the default class for representing dispersion - data
 * 
 * @author kfuchsbe
 * 
 */
public class DispersionDataImpl extends AbstractDynamicData implements
		DispersionData {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(DispersionDataImpl.class);

	/**
	 * the original reading-data, which is read e.g. from a
	 * yasp-dispersion-file.
	 */
	private ReadingData readingData;

	/*
	 * the cachees for the calculated values
	 */
	private List<Double> valuesH = new ArrayList<Double>();
	private List<Double> valuesV = new ArrayList<Double>();
	private List<Double> rmsH = new ArrayList<Double>();
	private List<Double> rmsV = new ArrayList<Double>();
	private List<Double> rms = new ArrayList<Double>();
	private List<Boolean> validityH = new ArrayList<Boolean>();
	private List<Boolean> validityV = new ArrayList<Boolean>();
	private List<Boolean> validity = new ArrayList<Boolean>();

	/**
	 * generic init-method.
	 * 
	 * @throws InconsistentDataException
	 */
	public void init() throws InconsistentDataException {
		calc();
	}

	@Override
	public List<Double> getRms(Plane plane) {
		ensureUpToDate();
		if (Plane.HORIZONTAL.equals(plane)) {
			return this.rmsH;
		} else if (Plane.VERTICAL.equals(plane)) {
			return this.rmsV;
		} else {
			logger.warn("unknown plane '" + plane + "'!");
			return null;
		}
	}

	@Override
	public List<Double> getValues(Plane plane) {
		ensureUpToDate();
		if (Plane.HORIZONTAL.equals(plane)) {
			return this.valuesH;
		} else if (Plane.VERTICAL.equals(plane)) {
			return this.valuesV;
		} else {
			logger.warn("unknown plane '" + plane + "'!");
			return null;
		}
	}

	@Override
	public List<Boolean> getValidity(Plane plane) {
		ensureUpToDate();
		if (Plane.HORIZONTAL.equals(plane)) {
			return this.validityH;
		} else if (Plane.VERTICAL.equals(plane)) {
			return this.validityV;
		} else {
			logger.warn("unknown plane '" + plane + "'!");
			return null;
		}
	}

	@Override
	public List<Boolean> getValidity() {
		ensureUpToDate();
		return this.validity;
	}

	@Override
	public List<Double> getRms() {
		ensureUpToDate();
		return this.rms;
	}

	@Override
	public void calc() throws InconsistentDataException {
		valuesH.clear();
		valuesV.clear();
		rmsH.clear();
		rmsV.clear();
		rms.clear();
		validityH.clear();
		validityV.clear();
		validity.clear();
		for (Monitor monitor : getActiveMonitors()) {
			MonitorValue monitorValue = getReadingData().getMonitorValue(
					monitor.getKey());
			if (monitorValue == null) {
				logger.warn("No monitor value for monitor with key '"
						+ monitor.getKey()
						+ "' could be found in dispersion data!");
				/* we use a new (dummy) status. */
				monitorValue = new MonitorValue();
				monitorValue.status = -1;
				monitorValue.setBeamPosition(0);
				monitorValue.rms = 0;
			}

			if (Plane.HORIZONTAL.equals(monitor.getPlane())) {
				valuesH.add(monitorValue.getBeamPosition());
				rmsH.add(monitorValue.rms);
				rms.add(monitorValue.rms);
				validityH.add(monitorValue.isOk());
				validity.add(monitor.isOk());
			} else if (Plane.VERTICAL.equals(monitor.getPlane())) {
				valuesV.add(monitorValue.getBeamPosition());
				rmsV.add(monitorValue.rms);
				rms.add(monitorValue.rms);
				validityV.add(monitorValue.isOk());
				validity.add(monitor.isOk());
			} else {
				throw new InconsistentDataException("Monitor with key '"
						+ monitor.getKey() + "' has an unknown plane '"
						+ monitor.getPlane() + "'!");
			}
		}
	}

	/**
	 * @param readingData
	 *            the readingData to set
	 */
	public void setReadingData(ReadingData readingData) {
		this.readingData = readingData;
	}

	/**
	 * @return the readingData
	 */
	public ReadingData getReadingData() {
		return readingData;
	}

}
