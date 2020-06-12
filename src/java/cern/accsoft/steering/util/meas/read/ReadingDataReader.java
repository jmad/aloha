/*
 * $Id: ReadingDataReader.java,v 1.2 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ $Revision: 1.2 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.util.meas.read;

import java.io.File;

import cern.accsoft.steering.util.meas.data.yasp.ReadingData;
import cern.accsoft.steering.util.meas.read.filter.ReadSelectionFilter;

/**
 * the general interface for a class that reads {@link ReadingData}
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public interface ReadingDataReader {

    /**
     * reads the data and returns a new instance of an {@link ReadingData} object.
     * 
     * @param file The file from which to read the data
     * @param selection a filter which allows to only load values for certain elements
     * @return the {@link ReadingData}
     * @throws ReaderException if an error occures during reading
     */
    public ReadingData read(File file, ReadSelectionFilter selection) throws ReaderException;

    /**
     * reads the data and returns a new instance of an {@link ReadingData} object. Values for all correctors and
     * monitors are loaded.
     * 
     * @param file the file from which to load the data
     * @return the newly loaded data
     * @throws ReaderException
     */
    public ReadingData read(File file) throws ReaderException;

}
