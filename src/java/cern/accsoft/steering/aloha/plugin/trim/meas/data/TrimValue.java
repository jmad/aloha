package cern.accsoft.steering.aloha.plugin.trim.meas.data;

import cern.accsoft.steering.util.meas.data.Plane;

public interface TrimValue {

	/**
	 * @return the elementName
	 */
	public abstract String getElementName();

	/**
	 * @return the plane
	 */
	public abstract Plane getPlane();

	/**
	 * @return the value
	 */
	public abstract double getValue();

	/**
	 * @param value
	 *            the value to set
	 */
	public abstract void setValue(double value);

}