package cern.accsoft.steering.aloha.gui.panels;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataViewer;

public class DataViewerPanel extends JPanel {
	private static final long serialVersionUID = -5359391586208558362L;

	private DataViewer dataViewer = new DataViewer();

	/**
	 * init method used by spring
	 */
	public void init() {
		initComponents();
	}

	/**
	 * creates all DataViews
	 */
	private void initComponents() {
		setLayout(new BorderLayout());

		/*
		 * The DataViewer
		 */
		getDataViewer().setExplorerVisible(true);
		add(getDataViewer(), BorderLayout.CENTER);

	}

	public void removeAllViews() {
		this.dataViewer.removeAllViews();
	}

	public void addDvViews(List<DVView> dvViews) {
		for (DVView dvView : dvViews) {
			this.dataViewer.addView(dvView);
		}
	}

	/**
	 * @return the dataViewer
	 */
	public DataViewer getDataViewer() {
		return dataViewer;
	}

}
