package cern.accsoft.steering.aloha.gui.panels;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

import cern.accsoft.gui.beans.MultiSplitLayout;
import cern.accsoft.gui.beans.MultiSplitPane;
import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManager;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManagerListener;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Dimension PREFERRED_SIZE = new Dimension(600, 400);

	private AlohaPanelFactory alohaPanelFactory;

	private JLabel statusLabel = null;

	/** the panel, where the measurement - details are displayed. */
	private JPanel detailPanel = null;

	/** the panel to display all available measurements */
	private JPanel measurementsPanel;

	/** the panel to display the available HelperData */
	private JPanel helperDataPanel;

	/** the panel for the dataviewer views */
	private DataViewerPanel dataViewerPanel;

	/** the panel which displays all the models */
	private JPanel modelDelegatesPanel;

	/**
	 * init method, used by spring.
	 */
	public void init() {
		initComponents();
	}

	/**
	 * adds all components to the panel.
	 */
	private void initComponents() {
		setLayout(new BorderLayout());
		setPreferredSize(PREFERRED_SIZE);

		/*
		 * The multisplit layout and the multisplit-pane, which contains all
		 * others.
		 */
		String layoutDef = "(COLUMN (ROW weight=0.5 "
				+ "(LEAF name=left.top1 weight=0.2) (LEAF name=left.top2 weight=0.25) (LEAF name=right.top weight=0.55)) "
				+ "(LEAF name=bottom weight=0.5))";
		MultiSplitLayout.Node layoutModel = MultiSplitLayout
				.parseModel(layoutDef);
		MultiSplitPane multiSplitPane = new MultiSplitPane();
		multiSplitPane.getMultiSplitLayout().setModel(layoutModel);
		multiSplitPane.getMultiSplitLayout().setDividerSize(5);
		add(multiSplitPane, BorderLayout.CENTER);

		JTabbedPane measModelTabbedPane = new JTabbedPane();
		multiSplitPane.add(measModelTabbedPane, "left.top1");
		measModelTabbedPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

		if (getMeasurementsPanel() != null) {
			measModelTabbedPane.addTab("Measurements", getMeasurementsPanel());
		}
		if (getModelDelegatesPanel() != null) {
			measModelTabbedPane.addTab("Models", getModelDelegatesPanel());
		}
		if (getHelperDataPanel() != null) {
			measModelTabbedPane.add("HelperData", getHelperDataPanel());
		}

		/*
		 * the monitors and correctors tabs
		 */
		JTabbedPane monCorrTabbedPane = new JTabbedPane();
		multiSplitPane.add(monCorrTabbedPane, "left.top2");
		monCorrTabbedPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

		monCorrTabbedPane.addTab("Monitors", getAlohaPanelFactory()
				.createMonitorSelectionPanel());
		monCorrTabbedPane.addTab("Correctors", getAlohaPanelFactory()
				.createCorrectorSelectionPanel());

		/*
		 * the left panels: calc buttons and stuff for DataViewer
		 */

		this.detailPanel = new JPanel(new BorderLayout());
		this.detailPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		multiSplitPane.add(this.detailPanel, "right.top");

		if (getDataViewerPanel() != null) {
			getDataViewerPanel()
					.setBorder(new BevelBorder(BevelBorder.LOWERED));
			multiSplitPane.add(getDataViewerPanel(), "bottom");
		}

		multiSplitPane.validate();
		validate();
	}

	public void setStatusText(String text) {
		statusLabel.setText(text);
	}

	/**
	 * @param dataViewerPanel
	 *            the dataViewerPanel to set
	 */
	public void setDataViewerPanel(DataViewerPanel dataViewerPanel) {
		this.dataViewerPanel = dataViewerPanel;
	}

	/**
	 * @return the dataViewerPanel
	 */
	private JPanel getDataViewerPanel() {
		return dataViewerPanel;
	}

	public void setAlohaPanelFactory(AlohaPanelFactory alohaPanelFactory) {
		this.alohaPanelFactory = alohaPanelFactory;
	}

	public AlohaPanelFactory getAlohaPanelFactory() {
		return alohaPanelFactory;
	}

	public void setMeasurementsPanel(JPanel measurementsPanel) {
		this.measurementsPanel = measurementsPanel;
	}

	public JPanel getMeasurementsPanel() {
		return measurementsPanel;
	}

	public void setDisplaySetManager(DisplaySetManager displaySetManager) {
		displaySetManager.addListener(new DisplaySetManagerListener() {

			@Override
			public void changedDisplaySet(DisplaySet oldDisplaySet,
					DisplaySet newDisplaySet) {

				/*
				 * first we remove the old stuff
				 */
				detailPanel.removeAll();
				dataViewerPanel.removeAllViews();

				/*
				 * then we display the new one
				 */
				if (newDisplaySet != null) {
					JPanel panel = newDisplaySet.getDetailPanel();

					if (panel != null) {
						detailPanel.add(panel, BorderLayout.CENTER);
					}
					dataViewerPanel.addDvViews(newDisplaySet.getDvViews());
					detailPanel.validate();
					dataViewerPanel.validate();
					newDisplaySet.refresh();
				}

			}
		});
	}

	public void setModelDelegatesPanel(JPanel modelDelegatesPanel) {
		this.modelDelegatesPanel = modelDelegatesPanel;
	}

	private JPanel getModelDelegatesPanel() {
		return modelDelegatesPanel;
	}

	public void setHelperDataPanel(JPanel helperDataPanel) {
		this.helperDataPanel = helperDataPanel;
	}

	public JPanel getHelperDataPanel() {
		return helperDataPanel;
	}

}
