/*
 * $Id: GuiTestSineFitInteractor.java,v 1.3 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.3 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.log4j.BasicConfigurator;

import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart.ChartRendererRole;
import cern.jdve.Chart;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.data.DefaultDataSet;
import cern.jdve.demo.data.RandomDataGenerator;
import cern.jdve.interactor.ZoomInteractor;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;
import cern.jdve.viewer.DataViewer;

/**
 * This is a simple TestCase, which invokes a gui with dataViewer in order to
 * test the SineFitInteractor.
 * 
 * @author kfuchsbe
 * 
 */
public class GuiTestSineFitInteractor extends JFrame {
	private static final long serialVersionUID = 1876785735966898377L;

	/** the points to display */
	private final static int NUMBER_OF_POINTS = 51;

	/** the dataviewer */
	private DataViewer dataViewer = new DataViewer();

	/**
	 * the default constructor
	 */
	public GuiTestSineFitInteractor() {
		super();
		initComponents();
	}

	/**
	 * init all components
	 */
	private void initComponents() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1000, 400));

		setTitle(GuiTestSineFitInteractor.class.toString());

		/*
		 * The DataViewer
		 */
		dataViewer.setExplorerVisible(true);
		add(dataViewer, BorderLayout.CENTER);

		DataSet ds1 = new DefaultDataSet("demo-measured", generateX(),
				generateY());
		DataSet ds2 = new DefaultDataSet("demo-model", generateX(), generateY());

		Chart chart = createBarChart(ds1, ds2, null, "X-label", "Y-label");
		DataView dv = DvUtils.createFitInteractorView(chart);
		DVView view = new DVView("Demo DvView");
		view.setLayout(DVView.VERTICAL_LAYOUT);
		view.addDataView(dv);
		dataViewer.addView(view);

	}

	private final Aloha2DChart createBarChart(DataSet measuredDataSet,
			DataSet modelDataSet, DataSource measuredErrorDataSource,
			String xtitle, String ytitle) {
		Aloha2DChart chart = new Aloha2DChart();
		chart.setXScaleTitle(xtitle);
		chart.setYScaleTitle(ytitle);

		chart.addInteractor(new ZoomInteractor());

		if (measuredDataSet != null) {
			chart.setRendererDataSet(ChartRendererRole.MEAS_DATA,
					measuredDataSet);
		}
		if (modelDataSet != null) {
			chart
					.setRendererDataSet(ChartRendererRole.MODEL_DATA,
							modelDataSet);
		}
		if (measuredErrorDataSource != null) {
			chart.setRenderDataSource(ChartRendererRole.MEAS_ERROR,
					measuredErrorDataSource);
		}

		chart.initMarkers();
		return chart;
	}

	/**
	 * generate random y-values
	 * 
	 * @return the y-values
	 */
	private double[] generateY() {
		return RandomDataGenerator
				.generateDoubleArray(5.0, 1, NUMBER_OF_POINTS);
	}

	/**
	 * @return increasing x - values.
	 */
	private double[] generateX() {
		double[] xValues = new double[NUMBER_OF_POINTS];
		for (int i = 0; i < xValues.length; i++) {
			xValues[i] = i;
		}
		return xValues;
	}

	/**
	 * the main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		JFrame frame = new GuiTestSineFitInteractor();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
