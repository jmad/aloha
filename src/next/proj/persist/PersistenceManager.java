/*
 * $Id: PersistenceManager.java,v 1.1 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.persist;

import java.io.File;

import cern.accsoft.steering.aloha.proj.data.AlohaProject;

/**
 * this is the general interface of a persistence-manager for aloha.
 * 
 * @author kfuchsbe
 * 
 */
public interface PersistenceManager {

	/**
	 * saves the project to a file
	 * 
	 * @param project
	 */
	public void saveProject(AlohaProject project, File file);

	/**
	 * loads the project from a file
	 * 
	 * @param file
	 *            the file from which to load the project
	 * @return the loaded project-definition
	 */
	public AlohaProject loadProject(File file);
}
