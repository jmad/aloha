/*
 * $Id: SplashFactory.java,v 1.2 2009-03-16 16:38:12 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:12 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.manage;

import cern.accsoft.gui.beans.spi.SplashScreen;
import cern.accsoft.steering.aloha.gui.icons.Icon;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

/**
 * This class manages the Splash-screen.
 * 
 * @author kfuchsbe
 * 
 */
public class SplashFactory {

	private final static SplashScreen splashScreen = new SplashScreen(null,
			Icon.SPLASH.getImageIcon().getImage());

	static {
		splashScreen.setText("Aloha");
		splashScreen.setMessage("don't hesitate to dance while waiting!");
	}

	/**
	 * @return the {@link SplashScreen}
	 */
	@CanIgnoreReturnValue
	public static SplashScreen getSplashScreen() {
		return splashScreen;
	}
}
