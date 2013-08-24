/*
 * $Id: TrimData.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.trim.meas.data;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.Data;

/**
 * This interface represents trim data, which can be applied to the model.
 * 
 * @author kfuchsbe
 *  
 */
public interface TrimData extends Data {

	/**
	 * returns the kicks (in rad) which were applied to the given corrector in
	 * the given plane during the measurement.
	 * 
	 * This always contains all entries of the files! (e.g. in yasp-files this
	 * might be correctors or bending magnets.) -> so it has to be checked and
	 * the user has to be warned, if not all of them can be applied.
	 * 
	 * @return all available corrector-values
	 */
	public List<TrimValue> getTrimValues();
}
