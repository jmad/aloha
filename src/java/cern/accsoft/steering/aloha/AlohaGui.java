/*
 * $Id: AlohaGui.java,v 1.4 2009-02-25 18:48:41 kfuchsbe Exp $
 *
 * $Date: 2009-02-25 18:48:41 $
 * $Revision: 1.4 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha;

import cern.accsoft.gui.beans.spi.SplashScreen;
import cern.accsoft.gui.frame.ExternalFrame;
import cern.accsoft.gui.frame.FrameManager;
import cern.accsoft.steering.aloha.gui.icons.Icon;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the extension to our default accsoft gui, which provides the correct
 * window-listener
 *
 * @author kfuchsbe
 */
public class AlohaGui {
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private Map<String, JComponent> extraConsoleTabs = new HashMap<>();
    private int sizeX = 800;
    private int sizeY = 600;
    private SplashScreen splashScreen;
    private ExternalFrame frame;
    private String title = "";

    public final void init() {
        this.frame = FrameManager.getInstance().getMainFrame();
        this.frame.getJFrame().setIconImage(Icon.MAIN.getImageIcon().getImage());

        if (this.splashScreen != null) {
            this.splashScreen.setFrame(this.frame.getJFrame());
        }

        if (this.mainPanel != null) {
            this.frame.setRootComponent(this.mainPanel);
        }

        if (this.menuBar != null) {
            this.frame.setJMenuBar(this.menuBar);
        }

        if (this.toolBar != null) {
            this.frame.addToolBar(this.toolBar);
        }
        this.extraConsoleTabs.forEach(this.frame.getConsoleTabbedPane()::addTab);

        this.frame.setTitle(this.title);
        this.frame.setSize(sizeX, sizeY);
        this.frame.setConsoleInside(true);
        this.frame.setConsoleVisible(true);
    }

    public void show() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public final void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public final void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public final void setToolBar(JToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public final void setTitle(String title) {
        this.title = title;
        if (this.frame != null) {
            this.frame.setTitle(title);
        }
    }

    public final void setSplashScreen(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public void setExtraConsoleTabs(Map<String, JComponent> extraConsoleTabs) {
        this.extraConsoleTabs = extraConsoleTabs;
    }

	public ExternalFrame getMainFrame() {
    	return frame;
	}
}
