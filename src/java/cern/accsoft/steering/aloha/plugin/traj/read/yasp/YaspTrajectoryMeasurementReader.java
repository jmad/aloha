package cern.accsoft.steering.aloha.plugin.traj.read.yasp;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.CombinedTrajectoryDataImpl;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryDataImpl;
import cern.accsoft.steering.aloha.plugin.traj.read.TrajectoryMeasurementReader;
import cern.accsoft.steering.aloha.read.MeasurementReaderOptions;
import cern.accsoft.steering.aloha.read.yasp.YaspUtil;
import cern.accsoft.steering.aloha.util.io.NameListException;
import cern.accsoft.steering.util.meas.data.yasp.MonitorValue;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.ReadingDataReader;
import cern.accsoft.steering.util.meas.read.filter.ReadSelectionFilter;
import cern.accsoft.steering.util.meas.read.yasp.YaspFileReader;
import cern.accsoft.steering.util.meas.read.yasp.YaspReaderException;
import cern.accsoft.steering.util.meas.yasp.browse.YaspFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is a reader for the Noise data acquired by yasp.
 * 
 * @author kfuchsbe
 */
public class YaspTrajectoryMeasurementReader implements TrajectoryMeasurementReader, AlohaBeanFactoryAware,
        MachineElementsManagerAware {
    private final static Logger LOGGER = LoggerFactory.getLogger(YaspTrajectoryMeasurementReader.class);

    /** the selected monitors and correctors */
    private ReadSelectionFilter selection = null;

    /** the reader to read the single files */
    private ReadingDataReader readingDataReader = new YaspFileReader();

    /** the factory for all the aloha beans */
    private AlohaBeanFactory alohaBeanFactory;

    /** the {@link MachineElementsManager} injected by {@link AlohaBeanFactory} */
    private MachineElementsManager machineElementsManager;

    /**
     * the file-filter for kick-response data
     */
    private FileFilter stabilityListFileFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            } else if (YaspStabilityList.STABILITY_LIST_FILENAME.equals(f.getName())) {
                return true;
            } else {
                return YaspFilters.TRAJECTORY_FILE_FILTER.accept(f);
            }
        }

        @Override
        public String getDescription() {
            return YaspTrajectoryMeasurementReader.this.getDescription();
        }
    };

    @Override
    public TrajectoryMeasurement read(List<File> files, ModelDelegate modelDelegate, MeasurementReaderOptions options)
            throws ReaderException {

        this.selection = modelDelegate.createReadSelectionFilter(options.getBeamNumber());
        TrajectoryData data = read(files);

        CombinedTrajectoryDataImpl combinedData = getAlohaBeanFactory().create(CombinedTrajectoryDataImpl.class);

        String name = files.get(0).getAbsoluteFile().getParentFile().getName() + "(" + files.size() + " files)";
        TrajectoryMeasurement measurement = new TrajectoryMeasurementImpl(name, modelDelegate, data, combinedData);
        combinedData.setMeasurement(measurement);
        getAlohaBeanFactory().configure(measurement);

        return measurement;
    }

    /* package visibility for testing */
    TrajectoryData read(List<File> files) throws ReaderException {
        if (files.size() < 1) {
            throw new ReaderException("No files to read!");
        }

        if ((files.size() == 1) && (isStabilityList(files.get(0)))) {
            /*
             * if we have one file then it might be a stability-list
             */
            return readStabilityData(readStabilityList(files.get(0)));
        } else {
            return readStabilityData(files);
        }
    }

    /**
     * reads the data from the files and calculates the noise for each monitor.
     * 
     * @param files a list of files to read
     * @return the noiseData
     * @throws ReaderException
     */
    private TrajectoryData readStabilityData(List<File> files) throws ReaderException {
        List<ReadingData> measurementDatas = readStabilityFiles(files);

        if (getMachineElementsManager() != null) {
            /* ensure that the unavailable monitors are deactivated */
            getMachineElementsManager().deactivateUnavailableMonitors(measurementDatas);
        }

        if (!(measurementDatas.size() > 0)) {
            LOGGER.warn("No stability Files to read!?");
            return null;
        }

        TrajectoryDataImpl noiseData = getAlohaBeanFactory().create(TrajectoryDataImpl.class);
        for (MonitorValue activeMonitorValue : measurementDatas.get(0).getMonitorValues()) {

            ArrayList<Double> values = new ArrayList<Double>();
            String monitorKey = activeMonitorValue.getKey();

            for (ReadingData measurementData : measurementDatas) {
                MonitorValue monitorValue = measurementData.getMonitorValue(monitorKey);

                if ((monitorValue != null) && (monitorValue.isOk())) {
                    values.add(monitorValue.getBeamPosition());
                }
            }

            /* the actual calculation */
            double noise = 0;
            double mean = 0;

            double sum = 0;
            for (double value : values) {
                sum += value;
            }
            mean = sum / values.size();

            boolean valid = true;
            if (values.size() > 0) {
                double sum2 = 0;
                for (double value : values) {
                    sum2 += Math.pow(value - mean, 2);
                }

                /* we divide by (N-1)! */
                noise = Math.sqrt(sum2 / (values.size() - 1));
            } else {
                valid = false;
                noise = 0.0;
            }

            noiseData.add(monitorKey, YaspUtil.toModel(mean), YaspUtil.toModel(noise), valid);
        }
        return noiseData;
    }

    /**
     * reads all the given stabilityFiles and returns the measurement-data as a list
     * 
     * @param files the stability-files to read
     * @return the measurement-data as a list
     * @throws ReaderException
     */
    private List<ReadingData> readStabilityFiles(List<File> files) throws ReaderException {
        List<ReadingData> measurementDatas = new ArrayList<ReadingData>();
        for (File file : files) {
            measurementDatas.add(getReadingDataReader().read(file, this.selection));
        }
        return measurementDatas;
    }

    /**
     * parses the stability-list and returns a list of files that have to be parsed.
     * 
     * @return a list of stearingFiles.
     * @throws YaspReaderException
     */
    private List<File> readStabilityList(File stabilityListFile) throws YaspReaderException {
        List<File> files = new ArrayList<File>();
        YaspStabilityList stabilityList = new YaspStabilityList(stabilityListFile);

        try {
            stabilityList.parse();
        } catch (NameListException e) {
            throw new YaspReaderException("Error while parsing Stability-List", e);
        }

        // all stability files must be in the same directory as the
        // stability-list.
        String basePath = stabilityListFile.getAbsoluteFile().getParent();
        for (String name : stabilityList.getNames()) {
            String fileName = basePath + File.separator + name;
            files.add(new File(fileName));
        }
        return files;
    }

    /**
     * @param readingDataReader the readingDataReader to set
     */
    public void setReadingDataReader(ReadingDataReader readingDataReader) {
        this.readingDataReader = readingDataReader;
    }

    /**
     * @return the readingDataReader
     */
    private ReadingDataReader getReadingDataReader() {
        return readingDataReader;
    }

    @Override
    public String getDescription() {
        return "Yasp stability data.";
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public FileFilter getFileFilter() {
        return this.stabilityListFileFilter;
    }

    @Override
    public boolean isHandling(List<File> files) {
        if ((files.size() == 1) && isStabilityList(files.get(0))) {
            /*
             * either the file has to be a stability-list
             */
            return true;
        } else {
            /*
             * or all the selected files have to be yasp trajectory files
             */
            for (File file : files) {
                if (!YaspFilters.TRAJECTORY_FILE_FILTER.accept(file)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isStabilityList(File file) {
        return YaspStabilityList.STABILITY_LIST_FILENAME.equals(file.getName());

    }

    @Override
    public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
        this.alohaBeanFactory = alohaBeanFactory;
    }

    private AlohaBeanFactory getAlohaBeanFactory() {
        return alohaBeanFactory;
    }

    @Override
    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    private MachineElementsManager getMachineElementsManager() {
        return this.machineElementsManager;
    }

    @Override
    public boolean requiresOptions() {
        return true;
    }
}
