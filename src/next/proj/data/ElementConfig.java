/*
 * $Id: ElementConfig.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * the configuration of an element to store in xml
 * 
 * @author kfuchsbe
 * 
 */
@Root(name = "element")
public class ElementConfig {

	@Attribute
	private String key;

	/** true if the element is active, false if not */
	@Attribute
	private boolean active = true;

	/** the gain factor of the element */
	@Attribute
	private double gain = 1.0;

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param gain
	 *            the gain to set
	 */
	public void setGain(double gain) {
		this.gain = gain;
	}

	/**
	 * @return the gain
	 */
	public double getGain() {
		return gain;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
}
