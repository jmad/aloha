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

import cern.accsoft.gui.beans.spi.SplashScreen;
import cern.accsoft.steering.aloha.app.AlohaCommandLineParser;
import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.gui.manage.SplashFactory;
import cern.accsoft.steering.jmad.util.JMadPreferences;
import org.apache.log4j.BasicConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * this is the class which starts the aloha application.
 * 
 * @author kfuchsbe
 * 
 */
public class Aloha {
	private final static String BEAN_NAME_ALOHA_GUI = "alohaGui";
	private final static String BEAN_NAME_ALOHA_PREFERENCES = "alohaPreferences";
	private final static String BEAN_NAME_JMAD_PREFERENCES = "jmadPreferences";

	private final AnnotationConfigApplicationContext appCtx;

	private Aloha() {
		appCtx = new AnnotationConfigApplicationContext(AlohaSpringConfiguration.class);
		appCtx.registerShutdownHook();
		appCtx.getBean("alohaGui");
	}

	private AlohaGui getGuiBean() {
		return appCtx.getBean(BEAN_NAME_ALOHA_GUI, AlohaGui.class);
	}

	private Preferences getAlohaPreferences() {
		return appCtx.getBean(BEAN_NAME_ALOHA_PREFERENCES, Preferences.class);
	}

	private JMadPreferences getJMadPreferences() {
		return appCtx.getBean(BEAN_NAME_JMAD_PREFERENCES, JMadPreferences.class);
	}

	private void show() {
		getGuiBean().show();
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();

		SplashScreen splashScreen = SplashFactory.getSplashScreen();

		Aloha aloha = new Aloha();

		/* see, if we got some interesting parameters */
		AlohaCommandLineParser.parse(args, aloha.getAlohaPreferences(), aloha.getJMadPreferences());

		aloha.show();
		splashScreen.killSplash();
	}

}
