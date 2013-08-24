/*
 * $Id: AlohaProject.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.data;

import java.util.List;

/**
 * the generic interface, which represents data, loaded into the workingSet
 * 
 * @author kfuchsbe
 * 
 */
public interface AlohaProject {

	/**
	 * @param dispersionDataSource
	 *            the dispersionDataSource to set
	 */
	public void setDispersionDataSource(
			DispersionDataSource dispersionDataSource);

	/**
	 * @return the dispersionDataSource
	 */
	public DispersionDataSource getDispersionDataSource();

	/**
	 * @param kickResponseDataSource the kickResponseDataSource to set
	 */
	public void setKickResponseDataSource(KickResponseDataSource kickResponseDataSource);

	/**
	 * @return the kickResponseDataSource
	 */
	public KickResponseDataSource getKickResponseDataSource();

	/**
	 * @param modelDescription the modelDescription to set
	 */
	public void setModelDescription(ModelDescription modelDescription);

	/**
	 * @return the modelDescription
	 */
	public ModelDescription getModelDescription();

	/**
	 * @param config
	 *            the monitorConfigurations to add
	 */
	public void addMonitorConfiguration(MonitorConfig config);

	/**
	 * @return the monitorConfigurations
	 */
	public List<MonitorConfig> getMonitorConfigurations();

	/**
	 * @param config
	 *            the correctorConfigurations to add
	 */
	public void addCorrectorConfiguration(CorrectorConfig config);

	/**
	 * @return the correctorConfigurations
	 */
	public List<CorrectorConfig> getCorrectorConfigurations();
}
