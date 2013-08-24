/*
 * $Id: StabilityDataReader.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.traj.read;

import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement;
import cern.accsoft.steering.aloha.read.MeasurementReader;

/**
 * this is the interface which must be implemented by a general reader, which
 * reads stability-data from a source.
 * 
 * @author kfuchsbe
 * 
 */
public interface TrajectoryMeasurementReader extends
		MeasurementReader<TrajectoryMeasurement> {
	/* nothing special here */
}
