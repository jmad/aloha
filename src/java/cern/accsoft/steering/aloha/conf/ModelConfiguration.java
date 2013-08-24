/*
 * $Id: ModelConfiguration.java,v 1.1 2008-11-18 08:00:08 kfuchsbe Exp $
 * 
 * $Date: 2008-11-18 08:00:08 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.conf;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class defines the configuration of a model, which can be persisted via xml.
 * 
 * @author kfuchsbe
 *
 */
@XmlRootElement
public class ModelConfiguration {
		
	/** the name of the sequence */
	private String sequenceName;
	
	/** the name of the Range */
	private String rangeName;

	/**
	 * @param sequenceName the sequenceName to set
	 */
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	/**
	 * @return the sequenceName
	 */
	public String getSequenceName() {
		return sequenceName;
	}

	/**
	 * @param rangeName the rangeName to set
	 */
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	/**
	 * @return the rangeName
	 */
	public String getRangeName() {
		return rangeName;
	}
	
}
