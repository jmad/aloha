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

import cern.accsoft.steering.aloha.gui.icons.Icon;
import cern.accsoft.steering.util.gui.DefaultAccsoftGui;

import javax.swing.*;

/**
 * This is the extension to our default accsoft gui, which provides the correct
 * window-listener
 * 
 * @author kfuchsbe
 * 
 */
public class AlohaGui extends DefaultAccsoftGui {
	protected ImageIcon getImageIcon() {
		return Icon.MAIN.getImageIcon();
	}
}
