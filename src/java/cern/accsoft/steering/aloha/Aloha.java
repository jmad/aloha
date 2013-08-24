/*
 * $Id: Aloha.java,v 1.7 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.7 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cern.accsoft.steering.aloha.app.AlohaCommandLineParser;
import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.gui.manage.SplashFactory;
import cern.accsoft.steering.jmad.util.JMadPreferences;

/**
 * this is the class which starts the aloha application.
 * 
 * @author kfuchsbe
 * 
 */
public class Aloha {
	/** the name of the gui bean */
	private final static String BEAN_NAME_ALOHA_GUI = "alohaGui";

	/** the name for the preferences bean */
	private final static String BEAN_NAME_ALOHA_PREFERENCES = "alohaPreferences";

	/** the name for the jmad preferences bean */
	private final static String BEAN_NAME_JMAD_PREFERENCES = "jmadPreferences";

	/** The application-context */
	private ApplicationContext appCtx;

	private void init() {
		/* show the Splash - screen */
		SplashFactory.getSplashScreen();

		/* creating the application - context. */
		this.appCtx = new ClassPathXmlApplicationContext(
				new String[] { "app-ctx-aloha.xml" });

	}

	private AlohaGui getGuiBean() {
		if (this.appCtx == null) {
			return null;
		}
		return (AlohaGui) this.appCtx.getBean(BEAN_NAME_ALOHA_GUI);
	}

	private Preferences getAlohaPreferences() {
		return (Preferences) this.appCtx.getBean(BEAN_NAME_ALOHA_PREFERENCES);
	}

	private JMadPreferences getJMadPreferences() {
		return (JMadPreferences) this.appCtx
				.getBean(BEAN_NAME_JMAD_PREFERENCES);
	}

	/**
	 * the main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/* configure the log4j - system */
		BasicConfigurator.configure();

		Aloha aloha = new Aloha();
		aloha.init();

		/* see, if we got some interesting parameters */
		AlohaCommandLineParser.parse(args, aloha.getAlohaPreferences(), aloha
				.getJMadPreferences());

		aloha.show();

	}

	private void show() {
		AlohaGui alohaGui = getGuiBean();
		if (alohaGui != null) {
			alohaGui.show();
		}
	}

}
