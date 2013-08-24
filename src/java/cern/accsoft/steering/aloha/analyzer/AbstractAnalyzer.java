/*
 * $Id: AbstractAnalyzer.java,v 1.6 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ 
 * $Revision: 1.6 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.analyzer;

import cern.accsoft.steering.aloha.bean.aware.ChartFactoryAware;
import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.meas.Measurement;

/**
 * This class implements the basic functionality for an analyzer in Aloha. It
 * allows to set from which the data then is derived.
 * 
 * @author kfuchsbe
 * 
 */
public abstract class AbstractAnalyzer<T extends Measurement> implements
		GenericAnalyzer<T>, ChartFactoryAware {

	/** the factory, which helps to create the charts */
	private ChartFactory chartFactory;

	/** the measurement, on which this analyzer is working on */
	private T measurement;

	/**
	 * @param chartFactory
	 *            the chartFactory to set
	 */
	@Override
	public void setChartFactory(ChartFactory chartFactory) {
		this.chartFactory = chartFactory;
	}

	/**
	 * @return the chartFactory
	 */
	protected ChartFactory getChartFactory() {
		return chartFactory;
	}

	@Override
	public final void setMeasurement(T measurement) {
		this.measurement = measurement;
	}

	/**
	 * @return the measurement
	 */
	protected final T getMeasurement() {
		return this.measurement;
	}

}
