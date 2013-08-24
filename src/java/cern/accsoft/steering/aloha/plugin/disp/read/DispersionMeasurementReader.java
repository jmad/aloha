/*
 * $Id: DispersionDataReader.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.read;

import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement;
import cern.accsoft.steering.aloha.read.MeasurementReader;

/**
 * this is the general interface for a class, that reads dispersion-data.
 * 
 * @author kfuchsbe
 * 
 */
public interface DispersionMeasurementReader extends
		MeasurementReader<DispersionMeasurement> {
	/* nothing special here */
}
