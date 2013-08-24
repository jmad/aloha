/*
 * $Id: YaspDispersionDataReader.java,v 1.1 2008-12-19 13:55:28 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:28 $ $Revision: 1.1 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.read.yasp;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.data.InconsistentDataException;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.CombinedDispersionDataImpl;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.DispersionData;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.DispersionDataImpl;
import cern.accsoft.steering.aloha.plugin.disp.read.DispersionMeasurementReader;
import cern.accsoft.steering.aloha.read.MeasurementReaderOptions;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.ReadingDataReader;
import cern.accsoft.steering.util.meas.read.filter.ReadSelectionFilter;
import cern.accsoft.steering.util.meas.read.yasp.YaspFileReader;
import cern.accsoft.steering.util.meas.read.yasp.YaspReaderException;

/**
 * this is the implementation of a reader for dispersion data out of yasp files
 * 
 * @author kfuchsbe
 */
public class YaspDispersionMeasurementReader implements DispersionMeasurementReader, AlohaBeanFactoryAware,
        MachineElementsManagerAware {

    /** the prefix, which identifies a yasp-dispersion file */
    private final static String FILENAME_PREFIX = "disp";

    /** the delimiter, which is used in yasp dispersion - filenames */
    private final static String FILENAME_TOKEN_DELIMITER = ".";

    /** the factory to create correctly configured beans */
    private AlohaBeanFactory alohaBeanFactory;

    private MachineElementsManager machineElementsManager;

    private ReadingDataReader readingDataReader = new YaspFileReader();

    /**
     * a file filter for dispersion-data
     */
    private FileFilter dispersionDataFileFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            } else {
                return isHandling(f);
            }
        }

        @Override
        public String getDescription() {
            return YaspDispersionMeasurementReader.this.getDescription();
        }
    };

    private DispersionData read(File file, ReadSelectionFilter selection) throws ReaderException {
        ReadingData readingData = this.readingDataReader.read(file, selection);

        /* ensure that the unavailable bpms are deactivated */
        getMachineElementsManager().deactivateUnavailableMonitors(Arrays.asList(new ReadingData[] { readingData }));

        DispersionDataImpl dispersionData = getAlohaBeanFactory().create(DispersionDataImpl.class);
        dispersionData.setReadingData(readingData);
        try {
            dispersionData.init();
        } catch (InconsistentDataException e) {
            throw new YaspReaderException("Error while calculating dispersion-data.", e);
        }
        return dispersionData;

    }

    @Override
    public String getDescription() {
        return "Yasp dispersion measurement.";
    }

    @Override
    public FileFilter getFileFilter() {
        return this.dispersionDataFileFilter;
    }

    @Override
    public boolean isHandling(List<File> files) {
        if (files.size() == 1) {
            return isHandling(files.get(0));
        } else {
            return false;
        }
    }

    private boolean isHandling(File file) {
        return YaspDispersionMeasurementReader.isDispersionFile(file.getName());
    }

    /**
     * determines, a file of the given name is a yasp dispersion-file.
     * 
     * @param fileName the filename to check
     * @return true, if it is a dispersion-file, false if not.
     */
    public final static boolean isDispersionFile(String fileName) {
        if (fileName.toUpperCase().startsWith((FILENAME_PREFIX + FILENAME_TOKEN_DELIMITER).toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getDescription();
    }

    @Override
    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    private MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    @Override
    public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
        this.alohaBeanFactory = alohaBeanFactory;
    }

    private AlohaBeanFactory getAlohaBeanFactory() {
        return alohaBeanFactory;
    }

    @Override
    public DispersionMeasurement read(List<File> files, ModelDelegate modelDelegate, MeasurementReaderOptions options)
            throws ReaderException {

        if (files.size() != 1) {
            throw new ReaderException("Got " + files.size() + " files to read, but can only handle exactly 1 file.");
        }
        File file = files.get(0);

        ReadSelectionFilter selection = modelDelegate.createReadSelectionFilter(options.getBeamNumber());
        DispersionData data = read(file, selection);

        CombinedDispersionDataImpl combinedData = getAlohaBeanFactory().create(CombinedDispersionDataImpl.class);
        DispersionMeasurementImpl measurement = new DispersionMeasurementImpl(file.getName(), modelDelegate, data,
                combinedData);
        measurement.setMachineElementsManager(getMachineElementsManager());
        return measurement;

    }

    @Override
    public boolean requiresOptions() {
        return true;
    }

}
