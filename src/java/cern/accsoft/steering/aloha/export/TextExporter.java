/*
 * $Id: TextExporter.java,v 1.1 2009-02-25 18:48:43 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:43 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.export;

/**
 * a generic interface to export to a text.
 * 
 * @author kfuchsbe
 * 
 */
public interface TextExporter {

	/**
	 * sets the maximum fraction digits of exported doubles
	 * 
	 * @param digits
	 *            the maximum number of fraction Digits
	 */
	public void setMaxFractionDigits(int digits);

	/**
	 * @return the maximum fraction digits for double values
	 */
	public int getMaxFractionDigits();

	/**
	 * creates the text to export and returns it
	 * 
	 * @return the text which then can be used further
	 */
	public String createExportText();
}
