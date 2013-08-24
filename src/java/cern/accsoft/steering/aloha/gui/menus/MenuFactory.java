package cern.accsoft.steering.aloha.gui.menus;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import cern.accsoft.gui.beans.AboutBox;
import cern.accsoft.gui.frame.FrameManager;
import cern.accsoft.steering.aloha.gui.icons.Icon;
import cern.accsoft.steering.aloha.meas.MeasurementManager.ModelDelegateInstance;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;

public class MenuFactory {
    /** The logger for the class */
    private final static Logger logger = Logger.getLogger(MenuFactory.class);

    /* created components */
    private JToolBar toolBar = null;
    private JMenuBar menuBar = null;

    /* actions */
    private Action exitAction = null;
    private Action showJMadAction = null;
    private Action showAboutBoxAction = null;

    private ModelDelegateManager modelDelegateManager;

    private MenuActionHandler menuActionHandler;

    /**
     * the action to display an open-file dialog
     */
    private Action openFileAction = new AbstractAction("Open", Icon.FILE_OPEN.getImageIcon()) {
        private static final long serialVersionUID = 1L;
        {
            putValue(AbstractAction.SHORT_DESCRIPTION, "Open a file");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getMenuActionHandler().loadData();
        }
    };

    /**
     * the action to display the chart renderer options
     */
    private Action showChartRendererOptionsAction = new AbstractAction("DV") {
        private static final long serialVersionUID = 6372560823880915935L;
        {
            putValue(AbstractAction.SHORT_DESCRIPTION, "Show dialog for Dataviewer options.");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getMenuActionHandler().showChartRendererOptionsDialog();
        }
    };

    /**
     * the action to display a dialog with the fit options
     */
    private Action showFitDialogAction = new AbstractAction("fit") {
        private static final long serialVersionUID = 2875030640784513311L;
        {
            putValue(AbstractAction.SHORT_DESCRIPTION, "Show dialog for fit options.");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getMenuActionHandler().showFitDialog();
        }

    };

    /**
     * Factory - Method for the ToolBar
     * 
     * @return the toolBar
     */
    public final JToolBar getToolBar() {
        if (toolBar == null) {
            /* file actions */
            toolBar = new JToolBar();
            toolBar.add(getOpenFileAction());

            /* view actions */
            toolBar.addSeparator();
            toolBar.add(getShowChartRendererOptionsAction());
            toolBar.add(getShowFitPanelAction());
            toolBar.add(getShowJMadAction());

            /* other actions */
            toolBar.addSeparator();
            toolBar.add(getExitAction());

        }
        return toolBar;

    }

    /**
     * Factory - method for MenuBar
     * 
     * @return the menuBar
     */
    public final JMenuBar getMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();

            /* file menu */
            JMenu fileMenu = new JMenu("File");
            fileMenu.add(getOpenFileAction());

            fileMenu.addSeparator();
            fileMenu.add(getExitAction());

            menuBar.add(fileMenu);

            /* help menu */
            JMenu helpMenu = new JMenu("Help");
            helpMenu.add(getShowAboutBoxAction());

            menuBar.add(helpMenu);
        }
        return menuBar;
    }

    private Action getOpenFileAction() {
        return openFileAction;
    }

    private Action getExitAction() {
        if (exitAction == null) {
            ImageIcon icon = Icon.EXIT.getImageIcon();
            exitAction = new AbstractAction("Exit", icon) {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    for (ModelDelegateInstance instance : getModelDelegateManager().getModelDelegateInstances()) {
                        instance.getModelDelegate().cleanup();
                    }
                    System.exit(0);
                }
            };
            exitAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Exit Application");

        }
        return exitAction;
    }

    private Action getShowJMadAction() {
        if (showJMadAction == null) {
            ImageIcon icon = Icon.MADX.getImageIcon();
            showJMadAction = new AbstractAction("JMad", icon) {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    getMenuActionHandler().showJMadGui();
                }
            };
            showJMadAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Open JMad - frame");

        }
        return showJMadAction;
    }

    private Action getShowAboutBoxAction() {
        if (showAboutBoxAction == null) {
            showAboutBoxAction = new AbstractAction("About") {
                private static final long serialVersionUID = -1695054883852564439L;

                @Override
                public void actionPerformed(ActionEvent evt) {
                    AboutBox aboutBox = new AboutBox(FrameManager.getInstance().getMainFrame().getJFrame());
                    aboutBox.setIcon(Icon.ABOUT.getImageIcon());
                    aboutBox.setText("Aloha", "cern-accsoft-steering-aloha",
                            "(C) Copyright CERN 2008  Kajetan Fuchsberger AB-OP-SPS", null);
                    aboutBox.setVisible(true);
                }
            };
        }
        return showAboutBoxAction;
    }

    /**
     * @param menuActionHandler the menuActionHandler to set
     */
    public void setMenuActionHandler(MenuActionHandler menuActionHandler) {
        this.menuActionHandler = menuActionHandler;
    }

    /**
     * @return the menuActionHandler
     */
    public MenuActionHandler getMenuActionHandler() {
        return menuActionHandler;
    }

    private Action getShowChartRendererOptionsAction() {
        return showChartRendererOptionsAction;
    }

    private Action getShowFitPanelAction() {
        return showFitDialogAction;
    }

    public void setModelDelegateManager(ModelDelegateManager modelDelegateManager) {
        this.modelDelegateManager = modelDelegateManager;
    }

    public ModelDelegateManager getModelDelegateManager() {
        return modelDelegateManager;
    }

}
