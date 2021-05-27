/*
 * $Id: CorrectorKickDataReader.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ $Revision: 1.2 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.read;

import java.io.File;

import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CorrectorKickData;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.filter.ReadSelectionFilter;

/**
 * this is the interface for a class, that reads corrector-ckick data
 * 
 * @author kfuchsbe
 */
public interface CorrectorKickDataReader {

    /**
     * reads the data and returns a new instance on {@link CorrectorKickData}
     * 
     * @return the {@link CorrectorKickData}
     */
    CorrectorKickData read(File file, ReadSelectionFilter selection, MeasurementReaderOptions options) throws ReaderException;
}
