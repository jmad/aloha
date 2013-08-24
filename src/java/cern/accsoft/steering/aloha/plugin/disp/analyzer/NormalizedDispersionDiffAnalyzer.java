/*
 * $Id: NormalizedDispersionDiffAnalyzer.java,v 1.5 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.5 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.analyzer;

import java.util.List;

import cern.accsoft.steering.aloha.analyzer.AbstractAnalyzer;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.CombinedDispersionData;
import cern.accsoft.steering.jmad.gui.mark.MarkerXProvider;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.jdve.data.DataSet;
import cern.jdve.data.DefaultDataSource;
import cern.jdve.viewer.DVView;

/**
 * @author kfuchsbe
 * 
 */
public class NormalizedDispersionDiffAnalyzer extends
		AbstractAnalyzer<DispersionMeasurement> {

	/** the name of this analyzer */
	private final static String ANALYZER_NAME = "Normalized Dispersion Diff.";

	/*
	 * the data-sets for the dispersion-data
	 */
	private ListDataSet dataH = new ListDataSet("Dispersion measured-model (H)");
	private ListDataSet dataHPlus = new ListDataSet(
			"Dispersion measured-model (H) plus rms");
	private ListDataSet dataHMinus = new ListDataSet(
			"Dispersion measured-model (H) minus rms");

	private ListDataSet dataV = new ListDataSet("Dispersion measured-model (V)");
	private ListDataSet dataVPlus = new ListDataSet(
			"Dispersion measured-model (V) plus rms");
	private ListDataSet dataVMinus = new ListDataSet(
			"Dispersion measured-model (V) minus rms");

	/** the dataViews */
	private DVView dvView = new DVView(ANALYZER_NAME, DVView.VERTICAL_LAYOUT);

	/**
	 * the init method. Just creates the DataViews
	 */
	@InitMethod
	public void init() {
		createDataViews();
	}

	/**
	 * creates the DataViews
	 */
	private void createDataViews() {
		if (getChartFactory() == null) {
			return;
		}

		Aloha2DChart chart;
		chart = getChartFactory().createBarChart(dataH, null,
				new DefaultDataSource(new DataSet[] { dataHPlus, dataHMinus }),
				"mux [2 pi]", "disp meas-model H / sqrt(beta) [sqrt(m)]");
		chart.setMarkerXProvider(new MarkerXProvider() {
			@Override
			public List<Double> getXPositions(String elementName) {
				return ListUtil.createOneElementList(getModelOpticsData()
						.getPhase(elementName, Plane.HORIZONTAL));
			}
		});
		this.dvView.addDataView(DvUtils.createFitInteractorView(chart));

		chart = getChartFactory().createBarChart(dataV, null,
				new DefaultDataSource(new DataSet[] { dataVPlus, dataVMinus }),
				"muy [2 pi]", "disp meas-model V / sqrt(beta) [sqrt(m)]");
		chart.setMarkerXProvider(new MarkerXProvider() {
			@Override
			public List<Double> getXPositions(String elementName) {
				return ListUtil.createOneElementList(getModelOpticsData()
						.getPhase(elementName, Plane.VERTICAL));
			}
		});
		this.dvView.addDataView(DvUtils.createFitInteractorView(chart));
	}

	/**
	 * sets the new values to the DataSets
	 */
	private void configureDataSets() {
		CombinedDispersionData combinedData = getCombinedData();
		if (combinedData == null) {
			return;
		}

		configureDataSet(dataH, Plane.HORIZONTAL, false);
		configureDataSet(dataHPlus, Plane.HORIZONTAL, true);
		configureDataSet(dataHMinus, Plane.HORIZONTAL, true);

		configureDataSet(dataV, Plane.VERTICAL, false);
		configureDataSet(dataVPlus, Plane.VERTICAL, true);
		configureDataSet(dataVMinus, Plane.VERTICAL, true);
	}

	/**
	 * configures one dataSet
	 * 
	 * @param dataSet
	 * @param plane
	 * @param setErrors
	 */
	private void configureDataSet(ListDataSet dataSet, Plane plane,
			boolean setErrors) {
		if (getCombinedData() == null) {
			return;
		}

		if (setErrors) {
			dataSet.setLabels(getMachineElementsManager()
					.getActiveMonitorNames(plane));
			dataSet
					.setValues(getModelOpticsData().getMonitorPhases(plane),
							getCombinedData()
									.getMonitorNormalizedDispersionDiff(plane),
							getCombinedData()
									.getMonitorNormalizedDispersionRms(plane),
							null);

		} else {
			dataSet.setLabels(getMachineElementsManager()
					.getActiveMonitorNames(plane));
			dataSet
					.setValues(getModelOpticsData().getMonitorPhases(plane),
							getCombinedData()
									.getMonitorNormalizedDispersionDiff(plane));
		}
	}

	private CombinedDispersionData getCombinedData() {
		return getMeasurement().getCombinedData();
	}

	/**
	 * @return the machineElementsManager
	 */
	private MachineElementsManager getMachineElementsManager() {
		return getMeasurement().getMachineElementsManager();
	}

	/**
	 * @return the modelOpticsData
	 */
	private ModelOpticsData getModelOpticsData() {
		return getMeasurement().getModelDelegate().getModelOpticsData();
	}

	@Override
	public void refresh() {
		configureDataSets();
	}

	@Override
	public DVView getDVView() {
		return this.dvView;
	}
}
