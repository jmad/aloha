/*
 * $Id: TrimDataImpl.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.trim.meas.data;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.util.meas.data.yasp.CorrectorValue;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;

/**
 * This class is the default implementation for trim data read from a file.
 * 
 * @author kfuchsbe
 * 
 */
public class TrimDataImpl implements TrimData {

	/** the basic data, which e.g. is read from a file. */
	private ReadingData readingData;

	/** the calculated trim-values */
	private List<TrimValue> trimValues = new ArrayList<TrimValue>();

	/** the conversion factor for the correct conversion to model units */
	private double toModelConversionFactor = 1.0;

	/**
	 * initialize the data
	 */
	public void init() {
		calc();
	}

	/**
	 * calculates the trim values out of the read data. (In principle only
	 * conversion to model-units)
	 */
	private void calc() {
		this.trimValues.clear();
		for (CorrectorValue correctorValue : readingData.getCorrectorValues()) {
			TrimValue value = new TrimValueImpl(correctorValue.getName(),
					correctorValue.getPlane());
			value.setValue(toModel(correctorValue.kick));
			this.trimValues.add(value);
		}
	}

	/**
	 * converts the given value (length) to correct model-units.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 */
	private final double toModel(double value) {
		return this.getToModelConversionFactor() * value;
	}

	@Override
	public List<TrimValue> getTrimValues() {
		return this.trimValues;
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

	/**
	 * @param toModelConversionFactor
	 *            the toModelConversionFactor to set
	 */
	public void setToModelConversionFactor(double toModelConversionFactor) {
		this.toModelConversionFactor = toModelConversionFactor;
	}

	/**
	 * @return the toModelConversionFactor
	 */
	public double getToModelConversionFactor() {
		return toModelConversionFactor;
	}
}
