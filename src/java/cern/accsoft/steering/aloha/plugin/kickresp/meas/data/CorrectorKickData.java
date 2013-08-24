/*
 * $Id: CorrectorKickData.java,v 1.1 2008-12-02 20:57:43 kfuchsbe Exp $
 * 
 * $Date: 2008-12-02 20:57:43 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import cern.accsoft.steering.util.meas.data.yasp.ReadingData;


/**
 * this is the interface for a dataset, which is specially dedicated for a
 * kick-response.
 * 
 * @author kfuchsbe
 * 
 */
public interface CorrectorKickData extends ReadingData {

	/**
	 * @return the key of the corrector, which was kicked.
	 */
	public String getCorrectorKey();

	/**
	 * @return the name of the corrector, which was kicked.
	 */
	public String getCorrectorName();

	/**
	 * determines if the file corresponds to a vertical active Corrector
	 * 
	 * @return true if vertical corrector
	 */
	public boolean isVertical();

	/**
	 * determines if the file corresponds to a horizontal active Corrector
	 * 
	 * @return true if horizontal corrector
	 */
	public boolean isHorizontal();

	/**
	 * determines if the file corresponds to a 'plus' measurement.
	 * 
	 * @return true, if 'plus' measurement.
	 */
	public boolean isPlus();

	/**
	 * determines if the file corresponds to a 'minus' measurement.
	 * 
	 * @return true, if 'minus' measurement.
	 */
	public boolean isMinus();
	
}
