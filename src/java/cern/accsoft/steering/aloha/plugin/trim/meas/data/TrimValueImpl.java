/*
 * $Id: TrimValueImpl.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.trim.meas.data;

import cern.accsoft.steering.util.meas.data.Plane;

/**
 * the data-object for trim-values
 * 
 * @author kfuchsbe
 * 
 */
public class TrimValueImpl implements TrimValue {

	/** the name of the element */
	private String elementName;

	/** which plane to trim */
	private Plane plane;

	/** the actual value of the trim */
	private double value = 0;

	/**
	 * the default constructor, which requires fields for the immutable fields
	 * 
	 * @param elementName
	 *            the name of the element to trim
	 * @param plane
	 *            the plane which to trim
	 */
	public TrimValueImpl(String elementName, Plane plane) {
		this.elementName = elementName;
		this.plane = plane;
	}

	//
	// getters and setters
	//

	/**
	 * @return the elementName
	 */
	public final String getElementName() {
		return elementName;
	}

	/**
	 * @return the plane
	 */
	public final Plane getPlane() {
		return plane;
	}

	/**
	 * @return the value
	 */
	public final double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(double value) {
		this.value = value;
	}

}
