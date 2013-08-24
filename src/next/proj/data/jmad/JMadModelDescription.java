/*
 * $Id: JMadModelDescription.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.data.jmad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cern.accsoft.steering.aloha.proj.data.ModelDescription;
import cern.accsoft.steering.jmad.model.ModelCategory;

/**
 * the description of an madx-model
 * 
 * @author kfuchsbe
 * 
 */
@Root(name = "jmad-model")
public class JMadModelDescription implements ModelDescription {

	/** the id of the jmad-model */
	@Element(name = "model-id")
	private ModelCategory modelId;

	/** the name of the model-sequence */
	@Element(name = "sequence-name")
	private String sequenceName;

	/** the name of the range to use */
	@Element(name = "range-name")
	private String rangeName;

	/**
	 * @param modelId
	 *            the modelId to set
	 */
	public void setModelId(ModelCategory modelId) {
		this.modelId = modelId;
	}

	/**
	 * @return the modelId
	 */
	public ModelCategory getModelId() {
		return modelId;
	}

	/**
	 * @param sequenceName
	 *            the sequenceName to set
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
	 * @param rangeName
	 *            the rangeName to set
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
