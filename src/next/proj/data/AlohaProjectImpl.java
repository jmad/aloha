/*
 * $Id: AlohaProjectImpl.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.data;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * a project-definition for aloha for yasp-data
 * 
 * @author kfuchsbe
 * 
 */
@Root(name = "aloha-project")
public class AlohaProjectImpl implements AlohaProject {

	/** the dispersion-data to load */
	@Element(name = "dispersion-data", required = false)
	private DispersionDataSource dispersionDataSource;

	/** the kick-response data to load */
	@Element(name = "kick-response-data", required = false)
	private KickResponseDataSource kickResponseDataSource;

	/** the model to use */
	@Element(name = "model")
	private ModelDescription modelDescription;

	/** the configs for the monitors */
	@ElementList(name = "monitors")
	private List<MonitorConfig> monitorConfigurations;

	/** the configs for the correctors */
	@ElementList(name = "correctors")
	private List<CorrectorConfig> correctorConfigurations;

	public AlohaProjectImpl() {
		/*
		 * do this here, otherwise the the arraylist - class will be written to
		 * the xml
		 */
		this.monitorConfigurations = new ArrayList<MonitorConfig>();
		this.correctorConfigurations = new ArrayList<CorrectorConfig>();
	}

	/**
	 * @param dispersionDataSource
	 *            the dispersionDataSource to set
	 */
	public void setDispersionDataSource(
			DispersionDataSource dispersionDataSource) {
		this.dispersionDataSource = dispersionDataSource;
	}

	/**
	 * @return the dispersionDataSource
	 */
	public DispersionDataSource getDispersionDataSource() {
		return dispersionDataSource;
	}

	/**
	 * @param kickResponseDataSource
	 *            the kickResponseDataSource to set
	 */
	public void setKickResponseDataSource(
			KickResponseDataSource kickResponseDataSource) {
		this.kickResponseDataSource = kickResponseDataSource;
	}

	/**
	 * @return the kickResponseDataSource
	 */
	public KickResponseDataSource getKickResponseDataSource() {
		return kickResponseDataSource;
	}

	/**
	 * @param modelDescription
	 *            the modelDescription to set
	 */
	public void setModelDescription(ModelDescription modelDescription) {
		this.modelDescription = modelDescription;
	}

	/**
	 * @return the modelDescription
	 */
	public ModelDescription getModelDescription() {
		return modelDescription;
	}

	/**
	 * @param config
	 *            the monitorConfigurations to add
	 */
	public void addMonitorConfiguration(MonitorConfig config) {
		this.monitorConfigurations.add(config);
	}

	/**
	 * @return the monitorConfigurations
	 */
	public List<MonitorConfig> getMonitorConfigurations() {
		return monitorConfigurations;
	}

	/**
	 * @param config
	 *            the correctorConfigurations to add
	 */
	public void addCorrectorConfiguration(CorrectorConfig config) {
		this.correctorConfigurations.add(config);
	}

	/**
	 * @return the correctorConfigurations
	 */
	public List<CorrectorConfig> getCorrectorConfigurations() {
		return correctorConfigurations;
	}
}
