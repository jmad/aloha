/*
 * $Id: ProjectManagerImpl.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
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
import cern.accsoft.steering.aloha.proj.data.AlohaProjectImpl;
import cern.accsoft.steering.aloha.proj.persist.PersistenceManager;

/**
 * this class keeps track of the actual project.
 * 
 * @author kfuchsbe
 * 
 */
public class ProjectManagerImpl implements ProjectManager {

	/** the actual file name from which the project is loaded/saved */
	private File file = null;

	/** the actual project */
	private AlohaProject project = new AlohaProjectImpl();

	/** the persistence-manager we use to save and load the data */
	private PersistenceManager persistenceManager;

	@Override
	public AlohaProject getActiveProject() {
		return this.project;
	}

	@Override
	public void save(File file) {
		this.file = file;
		getPersistenceManager().saveProject(this.project, this.file);
	}

	@Override
	public void load(File file) {
		this.file = file;
		this.project = getPersistenceManager().loadProject(this.file);
	}

	/**
	 * @param persistenceManager
	 *            the persistenceManager to set
	 */
	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	/**
	 * @return the persistenceManager
	 */
	private PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	@Override
	public String getActiveDisplayName() {
		if (this.file == null) {
			return null;
		}
		return this.file.getAbsolutePath();
	}

}
