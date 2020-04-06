package cern.accsoft.steering.aloha.gui.panels;

import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.conf.MonitorSelection;
import cern.accsoft.steering.aloha.gui.icons.Icon;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.persist.XmlMonitorSelectionPersistenceService;
import cern.accsoft.steering.jmad.util.xml.PersistenceServiceException;
import cern.accsoft.steering.util.gui.menu.ActionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MonitorsPanel extends MachineElementsPanel implements ActionProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorsPanel.class);

    /** The persistence service to use for loading and saving the monitor - selections */
    private XmlMonitorSelectionPersistenceService monitorPersistenceService = new XmlMonitorSelectionPersistenceService();

    /** The preferences to get the data path */
    private Preferences preferences;

    /**
     * the action to save the selection
     */
    private Action saveMonitorSelectionAction = new AbstractAction(null, Icon.SAVE.getImageIcon()) {
        private static final long serialVersionUID = 1L;

        {
            putValue(TOOL_TIP_TEXT_KEY, "Saves the selection-state of the monitors to a file.");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            saveMonitorSelection();
        }
    };

    /**
     * the action to load the selection of the monitors from a file.
     */
    private Action loadMonitorSelectionAction = new AbstractAction(null, Icon.FILE_OPEN.getImageIcon()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadMonitorSelection();
        }
    };

    /**
     * the file filter for the file chooser dialog.
     */
    private FileFilter monitorSelectionFileFilter = new FileFilter() {

        @Override
        public String getDescription() {
            return "Aloha Monitor selection files";
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            return f.getName().endsWith(XmlMonitorSelectionPersistenceService.XML_FILE_EXTENSION);
        }
    };

    @Override
    protected Type getType() {
        return Type.MONITORS;
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(loadMonitorSelectionAction, saveMonitorSelectionAction);
    }

    /**
     * saves the actual selection of the monitors to a file
     */
    private void saveMonitorSelection() {
        JFileChooser fileChooser = createFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            LOGGER.debug("Saving file aborted by user");
            return;
        }
        File file = fileChooser.getSelectedFile();
        if (file != null) {
            try {
                monitorPersistenceService.save(getMachineElementsManager().getActiveMonitorSelection(), file);
            } catch (PersistenceServiceException e) {
                LOGGER.error("Saving monitor selectin to file '" + file.getAbsolutePath() + "' failed.");
            }
        }

    }

    /**
     * loads the selection of the monitors from a file and applies them to the {@link MachineElementsManager}
     */
    private void loadMonitorSelection() {
        JFileChooser fileChooser = createFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            LOGGER.debug("Loading file aborted by user");
            return;
        }
        File file = fileChooser.getSelectedFile();
        MonitorSelection monitorSelection = null;
        if (file != null) {
            try {
                monitorSelection = monitorPersistenceService.load(file);
            } catch (PersistenceServiceException e) {
                LOGGER.error("Saving monitor selectin to file '" + file.getAbsolutePath() + "' failed.");
            }
        }

        if (monitorSelection != null) {
            getMachineElementsManager().apply(monitorSelection);
        }
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser(getPreferences().getDataPath());
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.addChoosableFileFilter(monitorSelectionFileFilter);
        fileChooser.setFileFilter(monitorSelectionFileFilter);
        return fileChooser;
    }

}
