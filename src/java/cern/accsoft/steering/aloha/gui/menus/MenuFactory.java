package cern.accsoft.steering.aloha.gui.menus;

import cern.accsoft.gui.beans.AboutBox;
import cern.accsoft.gui.beans.SwingUtil;
import cern.accsoft.gui.frame.FrameManager;
import cern.accsoft.steering.aloha.gui.icons.Icon;
import cern.accsoft.steering.aloha.meas.MeasurementManager.ModelDelegateInstance;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;
import cern.accsoft.steering.aloha.model.ModelDelegateManagerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class MenuFactory {
    /* created components */
    private JToolBar toolBar = null;
    private JMenuBar menuBar = null;

    /* actions */
    private Action exitAction = null;
    private Action showJMadAction = null;
    private Action showAboutBoxAction = null;
    private JLabel jmadActiveModelLabel = null;

    private ModelDelegateManager modelDelegateManager;

    private MenuActionHandler menuActionHandler;

    /**
     * the action to display an open-file dialog
     */
    private Action openFileAction = new AbstractAction("Open", Icon.FILE_OPEN.getImageIcon()) {
        {
            putValue(AbstractAction.SHORT_DESCRIPTION, "Open a file");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            menuActionHandler.loadData();
        }
    };

    /**
     * the action to display the chart renderer options
     */
    private Action showChartRendererOptionsAction = new AbstractAction("DV") {
        {
            putValue(AbstractAction.SHORT_DESCRIPTION, "Show dialog for Dataviewer options.");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            menuActionHandler.showChartRendererOptionsDialog();
        }
    };

    /**
     * the action to display a dialog with the fit options
     */
    private Action showFitDialogAction = new AbstractAction("fit") {
        {
            putValue(AbstractAction.SHORT_DESCRIPTION, "Show dialog for fit options.");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            menuActionHandler.showFitDialog();
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
            toolBar.setFloatable(false);
            toolBar.add(getOpenFileAction());

            /* view actions */
            toolBar.addSeparator();
            toolBar.add(getShowChartRendererOptionsAction());
            toolBar.add(getShowFitPanelAction());
            toolBar.add(getShowJMadAction());

            toolBar.addSeparator(new Dimension(50, 10));
            toolBar.add(activeJmadModelLabel());

        }
        return toolBar;

    }

    private JLabel activeJmadModelLabel() {
        if (jmadActiveModelLabel == null) {
            jmadActiveModelLabel = new JLabel("JMad: <no model>");
        }
        return jmadActiveModelLabel;
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
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (ModelDelegateInstance instance : modelDelegateManager.getModelDelegateInstances()) {
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
                @Override
                public void actionPerformed(ActionEvent e) {
                    menuActionHandler.showJMadGui();
                }
            };
            showJMadAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Open JMad - frame");

        }
        return showJMadAction;
    }

    private Action getShowAboutBoxAction() {
        if (showAboutBoxAction == null) {
            showAboutBoxAction = new AbstractAction("About") {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    AboutBox aboutBox = new AboutBox(FrameManager.getInstance().getMainFrame().getJFrame());
                    aboutBox.setIcon(Icon.ABOUT.getImageIcon());
                    aboutBox.setText("Aloha", "io.jmad.aloha",
                            "(C) CERN 2008-2020 Kajetan Fuchsberger and the BE-OP software team", null);
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

    private Action getShowChartRendererOptionsAction() {
        return showChartRendererOptionsAction;
    }

    private Action getShowFitPanelAction() {
        return showFitDialogAction;
    }

    public void setModelDelegateManager(ModelDelegateManager modelDelegateManager) {
        this.modelDelegateManager = modelDelegateManager;
        modelDelegateManager.addListener(new ModelDelegateManagerListener() {
            @Override
            public void activeModelDelegateChanged(ModelDelegate modelDelegate) {
                SwingUtil.invokeLater(() -> jmadActiveModelLabel.setText(
                        "JMad: " + Optional.ofNullable(modelDelegate)
                                .map(m -> m.getJMadModel().getDescription())
                                .orElse("<no model>")));
            }
        });
    }
}
