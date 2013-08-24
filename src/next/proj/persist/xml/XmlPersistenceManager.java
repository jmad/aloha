/*
 * $Id: XmlPersistenceManager.java,v 1.1 2008-12-19 13:55:29 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:29 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.persist.xml;

import java.io.File;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cern.accsoft.steering.aloha.proj.data.AlohaProject;
import cern.accsoft.steering.aloha.proj.persist.PersistenceManager;

/**
 * the implementation ot a persistence-manager using xml-files.
 * 
 * @author kfuchsbe
 * 
 */
public class XmlPersistenceManager implements PersistenceManager {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(XmlPersistenceManager.class);

	@Override
	public void saveProject(AlohaProject project, File file) {
		Serializer serializer = new Persister();
		try {
			serializer.write(project, file);
		} catch (Exception e) {
			logger.error("Could not write project to file '"
					+ file.getAbsolutePath() + "'.", e);
		}
	}

	@Override
	public AlohaProject loadProject(File file) {
		Serializer serializer = new Persister();

		AlohaProject project = null;
		try {
			project = serializer.read(AlohaProject.class, file);
		} catch (Exception e) {
			logger.error("Error while loading project from file '"
					+ file.getAbsolutePath() + "'.", e);
		}

		return project;
	}

}
