package cern.accsoft.steering.aloha.plugin.kickresp.read.yasp;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.data.InconsistentDataException;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CombinedKickResponseDataImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CorrectorKickData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.KickResponseData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.KickResponseDataImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.ModelKickResponseDataImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.read.KickResponseMaesurementReader;
import cern.accsoft.steering.aloha.read.CorrectorKickDataReader;
import cern.accsoft.steering.aloha.read.MeasurementReaderOptions;
import cern.accsoft.steering.aloha.read.yasp.AbstractYaspMeasurementReader;
import cern.accsoft.steering.aloha.read.yasp.YaspCorrectorKickDataReader;
import cern.accsoft.steering.aloha.read.yasp.YaspUtil;
import cern.accsoft.steering.jmad.tools.response.DeflectionSign;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.Status;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.filter.impl.NameListReadSelectionFilter;
import cern.accsoft.steering.util.meas.read.yasp.YaspReaderException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YaspKickResponseDataReader extends AbstractYaspMeasurementReader<KickResponseMeasurement>
        implements KickResponseMaesurementReader, AlohaBeanFactoryAware, MachineElementsManagerAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(YaspKickResponseDataReader.class);

    /**
     * The measurement number (last digit in file-name) to use for reading.
     */
    private Integer measurementNumber = null;

    /**
     * the base path, where to search for files.
     */
    private String basePath;

    /**
     * the criterion, which correctors and monitors to read.
     */
    private NameListReadSelectionFilter selection;

    /**
     * the reader options
     */
    private MeasurementReaderOptions options;

    /**
     * the class to create all the classes the and configure them with the commonly used beans.
     */
    private AlohaBeanFactory alohaBeanFactory;

    private MachineElementsManager machineElementsManager;

    private CorrectorKickDataReader correctorKickDataReader = new YaspCorrectorKickDataReader();

    /**
     * the file-filter for kick-response data
     */
    private final FileFilter kickResponseFileFilter = new FileFilter() {
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
            return YaspKickResponseDataReader.this.getDescription();
        }
    };

    @Override
    public String getDescription() {
        return "Yasp Kick-Response measurement.";
    }

    @Override
    public FileFilter getFileFilter() {
        return this.kickResponseFileFilter;
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
        return YaspCorrectorKickDataReader.isCorrectorFile(file.getName());
    }

    @Override
    public KickResponseMeasurement read(List<File> files, ModelDelegate modelDelegate, MeasurementReaderOptions options)
            throws ReaderException {

        if (files.size() != 1) {
            throw new ReaderException("Got " + files.size() + " files to read, but can only handle exactly 1 file.");
        }
        File file = files.get(0);

        this.basePath = file.getAbsoluteFile().getParentFile().getAbsolutePath();

        /*
         * we use the name of the parent dir as name of the measurement
         */
        String name = file.getAbsoluteFile().getParentFile().getName() + " - " + options.getBeamNumber();
        LOGGER.info("reading data from dir '{}'", basePath);

        this.selection = modelDelegate.createReadSelectionFilter(options.getBeamNumber());
        this.options = options;

        KickResponseData kickResponseData = readData();

        return createYaspMeasurement(name, kickResponseData, modelDelegate);
    }

    /**
     * factory-method for {@link KickResponseMeasurementImpl}, because {@link MachineElementsManager} needs to be set.
     *
     * @param name the name of the measurement
     * @param data the data of the measurement
     * @return the {@link KickResponseMeasurementImpl}
     */
    private KickResponseMeasurement createYaspMeasurement(String name, KickResponseData data,
            ModelDelegate modelDelegate) {

        /*
         * create the model - data
         */
        ModelKickResponseDataImpl modelKickResponseData = getAlohaBeanFactory().create(ModelKickResponseDataImpl.class);
        modelKickResponseData.setModelDelegate(modelDelegate);
        modelKickResponseData.setKickConfiguration(data);

        /*
         * create the combined data
         */
        CombinedKickResponseDataImpl combinedData = getAlohaBeanFactory().create(CombinedKickResponseDataImpl.class);
        combinedData.setKickResponseData(data);
        combinedData.setModelKickResponseData(modelKickResponseData);

        /*
         * and finally the measurement
         */
        KickResponseMeasurementImpl measurement = new KickResponseMeasurementImpl(name, modelDelegate, data,
                combinedData, modelKickResponseData);

        /*
         * XXX a little bit ugly to configure this that here !?
         */
        if (data instanceof KickResponseDataImpl) {
            ((KickResponseDataImpl) data).setKickResponseMeasurement(measurement);
        }

        getAlohaBeanFactory().configure(measurement);
        return measurement;

    }

    private KickResponseData readData() throws YaspReaderException {
        KickResponseDataImpl data = getAlohaBeanFactory().create(KickResponseDataImpl.class);

        data.setToModelConversionFactor(YaspUtil.TO_MODEL_CONVERSION_FACTOR);
        try {
            readCorrectorFiles(data);

            /*
             * do before readMonitorStati. occasionly some files will be removed!
             */
            getMachineElementsManager().setSuppressActiveElementsChangedEvent(true);
            readCorrectorStati(data);
            readMonitorStati(data);
            getMachineElementsManager().setSuppressActiveElementsChangedEvent(false);
            data.init();
        } catch (InconsistentDataException e) {
            throw new YaspReaderException("error while calculating data from measurement.", e);
        }
        return data;
    }

    private void readCorrectorFiles(KickResponseDataImpl data) throws InconsistentDataException, YaspReaderException {

        /* number of available corrector - files. */
        int correctorFilesCount = 0;

        List<String> fileNamesPlus = getSteeringFileNames(DeflectionSign.PLUS);
        List<String> fileNamesMinus = getSteeringFileNames(DeflectionSign.MINUS);

        correctorFilesCount = fileNamesPlus.size();
        /*
         * simple data check (should not happen, since both lists are created out of same file):
         */
        if (correctorFilesCount != fileNamesMinus.size()) {
            throw new InconsistentDataException(
                    "There are a different amount of plus and minus files - dont know what to do!");
        }

        for (int i = 0; i < correctorFilesCount; i++) {
            File filePlus = new File(fileNamesPlus.get(i));
            File fileMinus = new File(fileNamesMinus.get(i));

            try {
                CorrectorKickData dataPlus = correctorKickDataReader.read(filePlus, selection, options);
                CorrectorKickData dataMinus = correctorKickDataReader.read(fileMinus, selection, options);
                data.addDataPlus(dataPlus);
                data.addDataMinus(dataMinus);
            } catch (ReaderException e) {
                throw new YaspReaderException("Error while parsing Data-files!", e);
            }
        }
    }

    public void setMeasurementNumber(Integer measurementNumber) {
        this.measurementNumber = measurementNumber;
    }

    /**
     * sets status of each corrector to NOT_OK for which there is no steering file.
     */
    private void readCorrectorStati(KickResponseDataImpl data) {
        Map<String, Corrector> correctors = new HashMap<>();
        for (Corrector corrector : getMachineElementsManager().getAllCorrectors()) {
            correctors.put(corrector.getKey(), corrector);
        }

        ArrayList<String> uninterestingKeys = new ArrayList<>();
        for (CorrectorKickData cfPlus : data.getCorrectorKickDataPlus().values()) {
            String key = cfPlus.getCorrectorKey();
            Corrector corrector = correctors.get(key);
            if (corrector == null) {
                LOGGER.warn("Measurement exists for corrector '" + cfPlus.getCorrectorName()
                        + "' but we are not interested in it (because no corrector for key '" + key
                        + "' was found in the model). Perhaps wrong model was selected?");
                /*
                 * we have to make sure, that we don't keep the files, we are not interested in
                 */
                uninterestingKeys.add(cfPlus.getCorrectorKey());
            } else {
                corrector.setStatus(Status.OK);
                corrector.setActive(true);
            }
        }

        /*
         * remove the unused files
         */
        for (String key : uninterestingKeys) {
            data.removeDataPlus(key);
            data.removeDataMinus(key);
        }

    }

    /**
     * reads monitor-stati and sets to NOT_OK, if at least not OK in one of the files.
     *
     * @throws InconsistentDataException
     */
    private void readMonitorStati(KickResponseDataImpl data) throws InconsistentDataException {
        readStati(data.getCorrectorKickDataPlus().values());
        readStati(data.getCorrectorKickDataMinus().values());
    }

    private void readStati(Collection<CorrectorKickData> correctorDatas) {
        getMachineElementsManager().activateAvailableMonitors(correctorDatas);
    }

    private List<String> getSteeringFileNames(DeflectionSign sign) throws YaspReaderException {

        List<String> correctorNames = selection.getCorrectorNames();
        ArrayList<String> steeringFileNames = new ArrayList<>(correctorNames.size());

        for (Plane plane : Plane.values()) {
            for (String correctorName : correctorNames) {
                String fileName = basePath + File.separatorChar + YaspCorrectorKickDataReader
                        .constructFileName(correctorName, plane, sign, measurementNumber, false);
                if ((new File(fileName)).exists()) {
                    steeringFileNames.add(fileName);
                } else {
                    /* try gzipped version */
                    String gzippedFileName = basePath + File.separatorChar + YaspCorrectorKickDataReader
                            .constructFileName(correctorName, plane, sign, measurementNumber, true);
                    if ((new File(gzippedFileName)).exists()) {
                        steeringFileNames.add(gzippedFileName);
                    }
                }
            }
        }

        if (steeringFileNames.isEmpty()) {
            throw new YaspReaderException(
                    "No datafiles could be found for sign '" + sign + "' at path '" + basePath + "'!\n"
                            + "Maybe you have selected the wrong model for this data?.\n"
                            + "The Corrector names found in the model are:\n" + partition(correctorNames));
        }

        return steeringFileNames;
    }

    private String partition(List<String> names) {
        List<String> sublistStrings = new ArrayList<>();
        Joiner sublistJoiner = Joiner.on(", ");
        for (List<String> list : Lists.partition(names, 10)) {
            sublistStrings.add(sublistJoiner.join(list));
        }
        return "[" + Joiner.on(",\n").join(sublistStrings) + "]";
    }

    /**
     * @return the machineElementsManager
     */
    private MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    /**
     * @param machineElementsManager the machineElementsManager to set
     */
    @Override
    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    /**
     * @param correctorKickDataReader the correctorKickDataReader to set
     */
    public void setCorrectorKickDataReader(CorrectorKickDataReader correctorKickDataReader) {
        this.correctorKickDataReader = correctorKickDataReader;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }

    @Override
    public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
        this.alohaBeanFactory = alohaBeanFactory;
    }

    private AlohaBeanFactory getAlohaBeanFactory() {
        return alohaBeanFactory;
    }

    @Override
    public boolean requiresOptions() {
        return true;
    }

}