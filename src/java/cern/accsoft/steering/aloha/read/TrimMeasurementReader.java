/*
 * $Id: TrimDataReader.java,v 1.1 2009-01-15 11:46:25 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:25 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.read;

import cern.accsoft.steering.aloha.plugin.trim.meas.TrimMeasurement;

/**
 * The interface of a reader for trim data.
 * 
 * @author kfuchsbe
 * 
 */
public interface TrimMeasurementReader extends MeasurementReader<TrimMeasurement> {
	/* nothing special here */
}
