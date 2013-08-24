/*
 * $Id: ProjectManager.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj;

import java.io.File;

import cern.accsoft.steering.aloha.proj.data.AlohaProject;

/**
 * this is the interface of a class, which keeps track of the actual project.
 * 
 * @author kfuchsbe
 * 
 */
public interface ProjectManager {

	/** a string, which characterizes the actual project */
	public String getActiveDisplayName();

	/**
	 * @return the actual project-configuration
	 */
	public AlohaProject getActiveProject();

	/**
	 * saves the active project to a file
	 */
	public void save(File file);

	/**
	 * @param file
	 *            the file from which to load the project
	 */
	public void load(File file);

}
