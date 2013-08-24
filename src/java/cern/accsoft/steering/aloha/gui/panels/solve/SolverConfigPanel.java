/*
 * $Id: SolverConfigPanel.java,v 1.1 2009-01-15 11:46:25 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:25 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.panels.solve;

import javax.swing.JPanel;

/**
 * @author kfuchsbe
 * 
 */
public abstract class SolverConfigPanel extends JPanel {
	private static final long serialVersionUID = 3826668886302199308L;

	/** apply the values to the solver */
	public abstract void apply();

}
