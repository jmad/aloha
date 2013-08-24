/*
 * $Id: AlohaGui.java,v 1.4 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ 
 * $Revision: 1.4 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha;

import javax.swing.ImageIcon;

import cern.accsoft.steering.aloha.gui.icons.Icon;
import cern.accsoft.steering.jmad.gui.JMadGui;

/**
 * This is the extension to our default accsoft gui, which provides the correct
 * window-listener
 * 
 * @author kfuchsbe
 * 
 */
public class AlohaGui extends JMadGui {

	/*
	 * since for the moment the only special thing we have to do in here is to
	 * cleanup the madx-model, we just extend the JMadGui, since it implements
	 * this task out of the box.
	 */

	@Override
	protected ImageIcon getImageIcon() {
		return Icon.MAIN.getImageIcon();
	}

}
