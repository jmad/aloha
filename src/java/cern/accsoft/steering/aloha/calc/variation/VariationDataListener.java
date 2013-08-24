/*
 * $Id: VariationDataListener.java,v 1.1 2009-01-19 17:13:40 kfuchsbe Exp $
 * 
 * $Date: 2009-01-19 17:13:40 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.variation;

/**
 * @author kfuchsbe
 * 
 */
public interface VariationDataListener {

	/**
	 * fired, when the amount of variation-parameters changed.
	 */
	public void changedVariationParameters();
}
