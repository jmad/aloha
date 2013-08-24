/*
 * $Id: YaspTrimDataReader.java,v 1.1 2009-01-15 11:46:25 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:25 $ $Revision: 1.1 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.read.yasp;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.trim.meas.TrimMeasurement;
import cern.accsoft.steering.aloha.plugin.trim.meas.TrimMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.trim.meas.data.TrimDataImpl;
import cern.accsoft.steering.aloha.read.MeasurementReaderOptions;
import cern.accsoft.steering.aloha.read.TrimMeasurementReader;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.ReadingDataReader;
import cern.accsoft.steering.util.meas.read.yasp.YaspFileReader;
import cern.accsoft.steering.util.meas.yasp.browse.YaspFilters;

/**
 * The reader for trim data out of yasp files.
 * 
 * @author kfuchsbe
 */
public class YaspTrimDataReader implements TrimMeasurementReader, AlohaBeanFactoryAware {

    private ReadingDataReader readingDataReader = new YaspFileReader();

    private AlohaBeanFactory alohaBeanFactory;

    @Override
    public String getDescription() {
        return "Yasp file as trim-settings.";
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public FileFilter getFileFilter() {
        return YaspFilters.TRAJECTORY_FILE_FILTER;
    }

    @Override
    public boolean isHandling(List<File> files) {
        if (files.size() == 1) {
            /*
             * Exactly one file is allowed and this must be a yasp file.
             */
            return YaspFilters.TRAJECTORY_FILE_FILTER.accept(files.get(0));
        } else {
            return false;
        }
    }

    @Override
    public TrimMeasurement read(List<File> files, ModelDelegate modelDelegate, MeasurementReaderOptions options)
            throws ReaderException {
        if (files.size() != 1) {
            throw new ReaderException("Got " + files.size() + " files to read, but can only handle exactly 1 file.");
        }

        File file = files.get(0);
        ReadingData readingData = this.readingDataReader.read(file, null);

        TrimDataImpl trimData = new TrimDataImpl();
        trimData.setToModelConversionFactor(YaspUtil.TO_MODEL_CONVERSION_FACTOR);
        trimData.setReadingData(readingData);
        trimData.init();

        TrimMeasurement measurement = new TrimMeasurementImpl(file.getName(), modelDelegate, trimData);
        this.alohaBeanFactory.configure(measurement);
        return measurement;
    }

    @Override
    public boolean requiresOptions() {
        return false;
    }

    @Override
    public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
        this.alohaBeanFactory = alohaBeanFactory;
    }

}
