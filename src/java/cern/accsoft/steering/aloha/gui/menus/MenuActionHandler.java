package cern.accsoft.steering.aloha.gui.menus;

import cern.accsoft.gui.frame.Task;
import cern.accsoft.steering.aloha.app.HelperDataManager;
import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManager;
import cern.accsoft.steering.aloha.gui.panels.MeasurementReaderOptionsPanel;
import cern.accsoft.steering.aloha.gui.panels.ModelInstanceSelectionPanel;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.MeasurementManager;
import cern.accsoft.steering.aloha.meas.ModelAwareMeasurement;
import cern.accsoft.steering.aloha.meas.data.HelperData;
import cern.accsoft.steering.aloha.model.JMadModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;
import cern.accsoft.steering.aloha.model.adapt.JMadModelAdapter;
import cern.accsoft.steering.aloha.model.adapt.ModelAdapterManager;
import cern.accsoft.steering.aloha.plugin.kickresp.read.yasp.YaspKickResponseDataReader;
import cern.accsoft.steering.aloha.read.HelperDataReader;
import cern.accsoft.steering.aloha.read.MeasurementReader;
import cern.accsoft.steering.aloha.read.MeasurementReaderOptions;
import cern.accsoft.steering.aloha.read.Reader;
import cern.accsoft.steering.aloha.read.ReaderManager;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.gui.JMadGui;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.service.JMadService;
import cern.accsoft.steering.util.gui.DefaultAccsoftGui;
import cern.accsoft.steering.util.gui.dialog.PanelDialog;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.yasp.browse.YaspFileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * this singleton class handles the actions performed by menus and toolbars
 * 
 * @author kfuchsbe
 */
public abstract class MenuActionHandler {

    /** the logger for the class */
    private final static Logger LOGGER = LoggerFactory.getLogger(MenuActionHandler.class);

    /** The file chooser, which will be instantiated the first time it will be used and reused afterwards */
    private JFileChooser fileChooser = null;

    /** the aloha main frame */
    private Frame mainFrame = null;

    /** the mangaer which keeps track about modelAdapters */
    private ModelAdapterManager modelAdapterManager = null;

    /** the workingset, from which to get all the data */
    private HelperDataManager workingSet = null;

    /**
     * all the available measurement-readers for measurements and other data. They are queried in the order of the list,
     * if they can handle a specific file to open.
     */
    private ReaderManager readerManager;

    /** the manager, which keeps track of all loaded models */
    private ModelDelegateManager modelDelegateManager;

    /** the manager which keeps track of all loaded measurements */
    private MeasurementManager measurementManager;

    /** the manager which keeps track of all active elements */
    private MachineElementsManager machineElementsManager;

    private DisplaySetManager displaySetManager;

    /** a panel to provide options for the charts */
    private JPanel chartRendererPanel;

    /** The aloha preferences */
    private Preferences preferences;

    private JMadService jMadService;

    private JMadGui jMadGui;

    public void showChartRendererOptionsDialog() {
        if (getChartRendererPanel() != null) {
            PanelDialog.show(getChartRendererPanel(), mainFrame, false);
        }
    }

    public void showFitDialog() {
        DefaultAccsoftGui fitGui = getFitGui();
        if (fitGui != null) {
            fitGui.show();
        }
    }

    /**
     * displays a file selection dialog and then loads the data given by the file choosen. It tries to determine the
     * type of measurement by file-filters (in the order of available readers. If none of the Readers can automatically
     * handle the file, then dialog is shown to choose the type of measurement.
     */
    public void loadData() {
        if (fileChooser == null) {
            fileChooser = createFileChooser();
        }

        int returnValue = fileChooser.showOpenDialog(mainFrame);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            /* if not ok pressed, then we have nothing to do. */
            LOGGER.debug("Opening of file aborted by user.");
            return;
        }
        List<File> files = Arrays.asList(fileChooser.getSelectedFiles());

        /*
         * now we have to select the correct reader. We try first if one or more of them can handle the file
         * automatically:
         */
        List<Reader> readers = new ArrayList<Reader>();
        for (Reader r : getReaders()) {
            if (r.isHandling(files)) {
                readers.add(r);
            }
        }

        Reader reader = null;
        if (readers.size() == 1) {
            reader = readers.get(0);
        } else if (readers.size() > 1) {
            /*
             * if more than one reader can handle the file, then we have to ask the user.
             */
            reader = (Reader) JOptionPane.showInputDialog(mainFrame,
                    "<html>More than one readers can handle the files '" + files.toString() + "'.<br><br>"
                            + "Please select the type of data manually.</html>", "Select file type",
                    JOptionPane.QUESTION_MESSAGE, null, readers.toArray(), null);
        } else {
            /*
             * If none of the readers handles it automatically, then we ask the user.
             */
            reader = (Reader) JOptionPane.showInputDialog(mainFrame,
                    "<html>The type of the data files<br>'" + files.toString()
                            + "'<br>could not be determined automatically.<br><br>"
                            + "Please select the type of data manually.</html>", "Select file type",
                    JOptionPane.QUESTION_MESSAGE, null, getReaders().toArray(), null);
        }

        if (reader == null) {
            LOGGER.debug("User selected no valid reader. Aborting.");
            return;
        }

        /*
         * now we have two choices: Measurement or global data
         */
        if (reader instanceof MeasurementReader<?>) {
            /*
             * XXX Dirty: For YASP-KickResponse reader we have to additionaly set the measurement number.
             * 
             * TODO: Should be more general!
             */
            if (reader instanceof YaspKickResponseDataReader) {
                ((YaspKickResponseDataReader) reader).setMeasurementNumber(getMeasurementNumber());
            }

            /*
             * next we have to let the user choose the model for this measurement.
             */
            JMadModel model = null;
            ModelDelegate modelDelegate = null;
            if (this.modelDelegateManager.getModelDelegateInstances().size() == 0) {
                /*
                 * If this is the first measurement, the model is choosen and created from all available definitions.
                 */
                model = jMadGui.showCreateModelDialog();
                if (model == null) {
                    LOGGER.debug("No model was choosen. Aborting loading data.");
                    return;
                }

            } else {
                /*
                 * If there are already existing measurements, then we can either reuse one of the model-instances or
                 * create a new instance with the same model-definition.
                 */
                modelDelegate = showSelectModelDelegateDialog();
            }
            if ((model == null) && (modelDelegate == null)) {
                LOGGER.debug("No model was choosen. Aborting loading data.");
                return;
            }

            /*
             * ask the user for options
             */
            MeasurementReaderOptions options = null;
            if (((MeasurementReader<?>) reader).requiresOptions()) {
                options = new MeasurementReaderOptions();
                if (!PanelDialog.show(new MeasurementReaderOptionsPanel(options), mainFrame, true)) {
                    LOGGER.debug("Options aborted. Aborting loading data.");
                    return;
                }
            }

            /*
             * finally we are ready to read the data and add it to the measurement manager.
             */
            Task<Object> task = new LoadDataTask(reader, modelDelegate, model, files, options);
            task.start();

        } else if (reader instanceof HelperDataReader<?>) {
            HelperData data;
            try {
                data = ((HelperDataReader<?>) reader).read(files);
            } catch (ReaderException e) {
                LOGGER.error("Error while reading data", e);
                JOptionPane.showMessageDialog(mainFrame, "Could not read data from files <br>'" + files.toString()
                        + "'.\n" + "Exception was: '" + e.getMessage() + "'", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            getWorkingSet().putData(data);
        } else {
            LOGGER.error("Unknown instance of reader '" + reader.getClass().getCanonicalName()
                    + "'. Do not know what to do.");
        }
    }

    private class LoadDataTask extends Task<Object> {

        private Reader reader;
        private ModelDelegate modelDelegate;
        private JMadModel model;
        private List<File> files;
        private MeasurementReaderOptions options;

        private LoadDataTask(Reader reader, ModelDelegate modelDelegate, JMadModel model, List<File> files,
                MeasurementReaderOptions options) {
            this.reader = reader;
            this.modelDelegate = modelDelegate;
            this.files = files;
            this.model = model;
            this.options = options;
            setName("Loading data.");
            setCancellable(true);
        }

        @Override
        protected Object construct() {

            if (modelDelegate == null) {
                if (model == null) {
                    LOGGER.error("No model was choosen, aborting.");
                }
                try {
                    model.reset();
                } catch (JMadModelException e) {
                    LOGGER.error("Error while initializing model. aborting.", e);
                    return null;
                }
                modelDelegate = createModelDelegate(model);
            }

            LOGGER.info("Start loading data .");
            ModelAwareMeasurement measurement;
            try {
                measurement = ((MeasurementReader<?>) reader).read(files, modelDelegate, options);
            } catch (ReaderException e) {
                LOGGER.error("Error while reading measurement", e);
                JOptionPane.showMessageDialog(mainFrame,
                        "<html>Could not read Measurement from files <br>'" + files.toString() + "'.<br>"
                                + "Exception was: '" + e.getMessage() + "'</html>", "Error", JOptionPane.ERROR_MESSAGE);
                return null;

            }
            getMeasurementManager().addMeasurement(measurement);
            getDisplaySetManager().display(measurement);
            LOGGER.info("Data successfully loaded.");
            return null;
        }

    }

    /**
     * determines the measurement number to use for loading kick-response data. This is required for YASP Kick-Response
     * files. (last digit in filename)
     * 
     * @return the measurement number
     */
    private int getMeasurementNumber() {
        Integer measurementNumber = getPreferences().getMeasurementNumber();
        if (measurementNumber == null) {
            String response = JOptionPane.showInputDialog(mainFrame, "Measurement number", "1");
            try {
                measurementNumber = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                LOGGER.error("Could not parse measurement-number, using 1");
                measurementNumber = 1;
            }
        }
        return measurementNumber;
    }

    /**
     * creates a file chooser with the additional custom file-filter.
     * 
     * @return the fileChooser
     */
    private JFileChooser createFileChooser() {
        JFileChooser chooser = new YaspFileChooser();
        chooser.setMultiSelectionEnabled(true);
        for (FileFilter customFileFilter : collectFileFilters()) {
            chooser.addChoosableFileFilter(customFileFilter);
        }
        chooser.setAcceptAllFileFilterUsed(true);
        String dataPath = getPreferences().getDataPath();
        if (dataPath != null) {
            chooser.setCurrentDirectory(new File(dataPath));
        }
        return chooser;
    }

    private List<FileFilter> collectFileFilters() {
        List<FileFilter> fileFilters = new ArrayList<FileFilter>();
        for (Reader reader : getReaders()) {
            fileFilters.add(reader.getFileFilter());
        }
        return fileFilters;
    }

    /**
     * shows a dialog for selecting a new or old instance of a model-delegate.
     * 
     * @return the model-delegate (if one is selected or correctly created)
     */
    private ModelDelegate showSelectModelDelegateDialog() {
        ModelInstanceSelectionPanel modelInstanceSelectionPanel = new ModelInstanceSelectionPanel();
        modelInstanceSelectionPanel.setModelDelegateManager(this.modelDelegateManager);
        modelInstanceSelectionPanel.init();
        if (PanelDialog.show(modelInstanceSelectionPanel, this.mainFrame)) {
            ModelDelegate modelDelegate = null;
            if (modelInstanceSelectionPanel.isNewInstance()) {
                JMadModel model = jMadService.createModel(
                        modelInstanceSelectionPanel.getSelectedModelDelegate().getJMadModel().getModelDefinition());
                try {
                    model.reset();
                    modelDelegate = createModelDelegate(model);
                } catch (JMadModelException e) {
                    LOGGER.error("Error while creating new model.", e);
                }
            } else {
                modelDelegate = modelInstanceSelectionPanel.getSelectedModelDelegate();
            }
            modelInstanceSelectionPanel.dispose();
            return modelDelegate;
        } else {
            return null;
        }
    }

    /**
     * shows the gui for jmad
     */
    public void showJMadGui() {
        jMadGui.showGui();
    }

    /**
     * @return the actual {@link HelperDataManager}
     */
    private HelperDataManager getWorkingSet() {
        if (this.workingSet == null) {
            LOGGER.warn("workingSet not set! Maybe config error!");
        }
        return this.workingSet;
    }

    /**
     * used by spring to inject the workingSet
     * 
     * @param workingSet the {@link HelperDataManager} to set
     */
    public final void setWorkingSet(HelperDataManager workingSet) {
        this.workingSet = workingSet;
    }

    /**
     * @return the actual preferences
     */
    private Preferences getPreferences() {
        return this.preferences;
    }

    /**
     * @return the measurement-manager
     */
    private MeasurementManager getMeasurementManager() {
        return this.measurementManager;
    }

    /**
     * setter used by spring to set the main panel
     * 
     * @param panel
     */
    public void setMainFrame(Frame panel) {
        this.mainFrame = panel;
    }

    public void setMeasurementManager(MeasurementManager measurementManager) {
        this.measurementManager = measurementManager;
    }

    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    public MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    /**
     * creates a {@link ModelDelegate} for the model.
     * 
     * @param model the model for which to create a model delegate
     * @return the {@link ModelDelegate}
     */
    private ModelDelegate createModelDelegate(JMadModel model) {
        return new JMadModelDelegate(model, getMachineElementsManager(), getModelAdapter(model));
    }

    /**
     * returns the model-adapter for the given model if the modelAdapterManager is correctly set
     * 
     * @param model the model for which to find a model-adapter
     * @return the modelAdapter for the model
     */
    private JMadModelAdapter getModelAdapter(JMadModel model) {
        if (getModelAdapterManager() == null) {
            LOGGER.warn("ModelAdapterManager not set. Mazbe config error?");
            return null;
        }
        return getModelAdapterManager().getModelAdapter(model);
    }

    public List<Reader> getReaders() {
        return getReaderManager().getReaders();
    }

    public void setChartRendererPanel(JPanel chartRendererPanel) {
        this.chartRendererPanel = chartRendererPanel;
    }

    public JPanel getChartRendererPanel() {
        return chartRendererPanel;
    }

    /**
     * Lookup method for the fitting GUI. Will be injected by spring. This has the advantage, that it will only be
     * created as soon as we need it.
     * 
     * @return a new GUI-frame which provides the fitting options.
     */
    public abstract DefaultAccsoftGui getFitGui();

    public void setModelDelegateManager(ModelDelegateManager modelDelegateManager) {
        this.modelDelegateManager = modelDelegateManager;
    }

    public ModelDelegateManager getModelDelegateManager() {
        return modelDelegateManager;
    }

    public void setReaderManager(ReaderManager readerManager) {
        this.readerManager = readerManager;
    }

    public ReaderManager getReaderManager() {
        return readerManager;
    }

    public void setDisplaySetManager(DisplaySetManager displaySetManager) {
        this.displaySetManager = displaySetManager;
    }

    private DisplaySetManager getDisplaySetManager() {
        return displaySetManager;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public void setModelAdapterManager(ModelAdapterManager modelAdapterManager) {
        this.modelAdapterManager = modelAdapterManager;
    }

    private ModelAdapterManager getModelAdapterManager() {
        return modelAdapterManager;
    }

    public void setJMadService(JMadService jMadService) {
        this.jMadService = jMadService;
    }

    public void setJMadGui(JMadGui jMadGui) {
        jMadGui.getJmadGuiPreferences().setExitOnClose(false);
        this.jMadGui = jMadGui;
    }

}
