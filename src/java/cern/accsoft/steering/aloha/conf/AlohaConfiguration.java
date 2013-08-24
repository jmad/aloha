/*
 * $Id: AlohaConfiguration.java,v 1.1 2008-11-18 08:00:08 kfuchsbe Exp $
 * 
 * $Date: 2008-11-18 08:00:08 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.conf;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the base configuration of aloha, which can be persisted
 * to xml. All filenames are relative to this file!
 * 
 * @author kfuchsbe
 * 
 */
@XmlRootElement
public class AlohaConfiguration {

	/** the folder containing the files which are used as response-data files */
	private String basePath = null;

	/** the filename for the file used as dispersion data */
	// private String dispersionMeasurementFileName = null;
	/** the keys of the active Monitors */
	private List<String> activeMonitorKeys = new ArrayList<String>();

	/** the keys of the active Correctors */
	private List<String> activeCorrectorkeys = new ArrayList<String>();

	/** the configuration of the model */
	private ModelConfiguration modelConfiguration = null;

	/**
	 * @param basePath
	 *            the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * @param activeMonitorKeys
	 *            the activeMonitorKeys to set
	 */
	public void setActiveMonitorKeys(List<String> activeMonitorKeys) {
		this.activeMonitorKeys = activeMonitorKeys;
	}

	/**
	 * @return the activeMonitorKeys
	 */
	public List<String> getActiveMonitorKeys() {
		return activeMonitorKeys;
	}

	/**
	 * @param activeCorrectorkeys
	 *            the activeCorrectorkeys to set
	 */
	public void setActiveCorrectorkeys(List<String> activeCorrectorkeys) {
		this.activeCorrectorkeys = activeCorrectorkeys;
	}

	/**
	 * @return the activeCorrectorkeys
	 */
	public List<String> getActiveCorrectorkeys() {
		return activeCorrectorkeys;
	}

	/**
	 * @param modelConfiguration the modelConfiguration to set
	 */
	public void setModelConfiguration(ModelConfiguration modelConfiguration) {
		this.modelConfiguration = modelConfiguration;
	}

	/**
	 * @return the modelConfiguration
	 */
	public ModelConfiguration getModelConfiguration() {
		return modelConfiguration;
	}
}
