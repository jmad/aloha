/*
 * $Id: AlignmentValueImpl.java,v 1.2 2009-01-19 17:13:40 kfuchsbe Exp $
 * 
 * $Date: 2009-01-19 17:13:40 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.meas.data.align;

/**
 * The implementation of a alignment value
 * 
 * @author kfuchsbe
 * 
 */
public class AlignmentValueImpl implements AlignmentValue {

	/**
	 * the name of the element, which these values correspond to
	 */
	private String elementName;

	/**
	 * the type of this alignment-value
	 */
	private AlignmentValueType type;

	/** The error towards left of the beam */
	double deltaX = 0.0;

	/** The error upwards */
	double deltaY = 0.0;

	/** the longitudinal position of the point */
	double s = 0.0;

	/** the error in beam direction */
	double deltaS = 0.0;

	/** the error of angle in */
	double deltaTilt = 0.0;

	/**
	 * the constructor, which needs the name of the element and the type of this
	 * value
	 * 
	 * @param elementName
	 *            the name of the element
	 */
	public AlignmentValueImpl(String elementName, AlignmentValueType type) {
		this.elementName = elementName;
		this.type = type;
	}

	@Override
	public final double getDeltaX() {
		return deltaX;
	}

	/**
	 * @param deltaX
	 *            the deltaX to set
	 */
	public final void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	@Override
	public final double getDeltaY() {
		return deltaY;
	}

	/**
	 * @param deltaY
	 *            the deltaY to set
	 */
	public final void setDeltaY(double deltaY) {
		this.deltaY = deltaY;
	}

	@Override
	public final double getS() {
		return s;
	}

	/**
	 * @param s
	 *            the s to set
	 */
	public final void setS(Double s) {
		this.s = s;
	}

	@Override
	public String getElementName() {
		return this.elementName;
	}

	@Override
	public AlignmentValueType getType() {
		return this.type;
	}

	/**
	 * @param deltaS
	 *            the deltaS to set
	 */
	public void setDeltaS(double deltaS) {
		this.deltaS = deltaS;
	}

	/**
	 * @return the deltaS
	 */
	public double getDeltaS() {
		return deltaS;
	}

	/**
	 * @param deltaTilt
	 *            the deltaTilt to set
	 */
	public void setDeltaTilt(double deltaTilt) {
		this.deltaTilt = deltaTilt;
	}

	/**
	 * @return the deltaTilt
	 */
	public double getDeltaTilt() {
		return deltaTilt;
	}
}
