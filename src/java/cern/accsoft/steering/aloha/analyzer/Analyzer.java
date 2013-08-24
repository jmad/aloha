/*
 * $Id: Analyzer.java,v 1.5 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.5 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.analyzer;

import cern.jdve.viewer.DVView;

/**
 * This interface defines the methods for a general Analyzer to be plugged in
 * into Aloha
 * 
 * @author kfuchsbe
 * 
 */
public interface Analyzer {

	/**
	 * @return the dataviews which can be displayed in a dataviewerPanel.
	 */
	public DVView getDVView();

	/**
	 * displays the new data in the views
	 */
	public void refresh();

}
