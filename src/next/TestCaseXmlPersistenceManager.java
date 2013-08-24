/*
 * $Id: TestCaseXmlPersistenceManager.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.persist.xml;

import org.junit.After;
import org.junit.Test;

import cern.accsoft.steering.aloha.proj.data.AlohaProject;
import cern.accsoft.steering.aloha.proj.data.AlohaProjectImpl;
import cern.accsoft.steering.aloha.proj.data.CorrectorConfig;
import cern.accsoft.steering.aloha.proj.data.MonitorConfig;
import cern.accsoft.steering.aloha.proj.data.jmad.JMadModelDescription;
import cern.accsoft.steering.aloha.proj.data.yasp.YaspDispersionDataSource;
import cern.accsoft.steering.aloha.proj.data.yasp.YaspKickResponseDataSource;
import cern.accsoft.steering.aloha.proj.persist.PersistenceManager;
import cern.accsoft.steering.jmad.model.ModelCategory;
import cern.accsoft.steering.jmad.util.LoggedTestCase;
import cern.accsoft.steering.util.TestFile;

/**
 * test-case for the xml-persistence manager
 * 
 * @author kfuchsbe
 * 
 */
public class TestCaseXmlPersistenceManager extends LoggedTestCase {

	private TestFile testFile = null;

	@After
	public void deleteTestFile() {
		if (testFile != null) {
			testFile.delete();
		}
	}

	/** the persistenceManager, which we want to test */
	private PersistenceManager persistenceManager = new XmlPersistenceManager();

	@Test
	public void testSaveProject() {
		AlohaProject project = new AlohaProjectImpl();

		YaspDispersionDataSource dispersionSource = new YaspDispersionDataSource();
		dispersionSource.setFileName("./test.file.name.abc");
		project.setDispersionDataSource(dispersionSource);

		YaspKickResponseDataSource kickResponseSource = new YaspKickResponseDataSource();
		kickResponseSource.setDirName("./ttest.dir");
		project.setKickResponseDataSource(kickResponseSource);

		JMadModelDescription modelDescription = new JMadModelDescription();
		modelDescription.setModelId(ModelCategory.TI8);
		modelDescription.setSequenceName("ti8lhcb2");
		modelDescription.setRangeName("ALL-B1");
		project.setModelDescription(modelDescription);

		CorrectorConfig corConf = new CorrectorConfig();
		corConf.setKey("ABC.CDE.B1.V");
		project.addCorrectorConfiguration(corConf);

		corConf = new CorrectorConfig();
		corConf.setKey("ABC.CDE.B1.H");
		corConf.setActive(false);
		corConf.setGain(2.1);
		project.addCorrectorConfiguration(corConf);

		MonitorConfig monConf = new MonitorConfig();
		monConf.setKey("CDE.EFG.B2.V");
		project.addMonitorConfiguration(monConf);

		monConf.setKey("CDE.EFG.B2.H");
		monConf.setActive(false);
		monConf.setGain(2.5);
		project.addMonitorConfiguration(monConf);

		testFile = new TestFile("test-project.aloha.xml");
		persistenceManager.saveProject(project, testFile.getFile());

		//
		// TODO: Is not a real test yet, tests nothing :-( should reread the
		// thing and compare.
		//
	}
}
