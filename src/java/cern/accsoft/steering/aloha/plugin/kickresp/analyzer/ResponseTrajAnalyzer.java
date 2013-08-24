/*
 * $Id: ResponseTrajAnalyzer.java,v 1.1 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.kickresp.analyzer;

import java.util.List;

import cern.accsoft.steering.aloha.analyzer.AbstractAnalyzer;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManagerListener;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.KickResponseData;
import cern.accsoft.steering.jmad.gui.mark.MarkerXProvider;
import cern.accsoft.steering.jmad.tools.response.DeflectionSign;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.accsoft.steering.util.gui.dv.ds.MatrixColumnDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.jdve.viewer.DVView;

/**
 * This analyzer displays the measured trajectories which were used to calc the
 * actually displayed response-matrix column.
 * 
 * @author kfuchsbe
 * 
 */
public class ResponseTrajAnalyzer extends
		AbstractAnalyzer<KickResponseMeasurement> implements
		MachineElementsManagerAware {

	/** the name of the analyzer */
	private final static String ANALYZER_NAME = "Response trajectory";

	/** the displayed dataviews */
	private DVView dvView = new DVView(ANALYZER_NAME, DVView.GRID_LAYOUT);

	private MatrixColumnDataSet dataHUp = new MatrixColumnDataSet(
			"Trajectory H up");
	private MatrixColumnDataSet dataHDown = new MatrixColumnDataSet(
			"Trajectory H down");
	private MatrixColumnDataSet dataVUp = new MatrixColumnDataSet(
			"Trajectory V up");
	private MatrixColumnDataSet dataVDown = new MatrixColumnDataSet(
			"Trajectory V down");

	/** the manager, which keeps track of all selected elements */
	private MachineElementsManager machineElementsManager;

	/**
	 * the init method
	 */
	@InitMethod
	public void init() {
		initDataViews();
	}

	/**
	 * creates the DataViews
	 */
	private void initDataViews() {
		this.dvView.removeAllDataViews();

		Aloha2DChart chart;
		chart = getChartFactory().createBarChart(this.dataHUp, null, null,
				"mux [2pi]", "pos x [m]");
		chart.setMarkerXProvider(new MarkerXProvider() {
			@Override
			public List<Double> getXPositions(String elementName) {
				return ListUtil.createOneElementList(getModelOpticsData()
						.getPhase(elementName, Plane.HORIZONTAL));
			}
		});
		this.dvView.addDataView(DvUtils.createDataView(chart));

		chart = getChartFactory().createBarChart(this.dataHDown, null, null,
				"mux [2pi]", "pos x [m]");
		chart.setMarkerXProvider(new MarkerXProvider() {
			@Override
			public List<Double> getXPositions(String elementName) {
				return ListUtil.createOneElementList(getModelOpticsData()
						.getPhase(elementName, Plane.HORIZONTAL));
			}
		});
		this.dvView.addDataView(DvUtils.createDataView(chart));

		chart = getChartFactory().createBarChart(this.dataVUp, null, null,
				"muy [2pi]", "pos y [m]");
		chart.setMarkerXProvider(new MarkerXProvider() {
			@Override
			public List<Double> getXPositions(String elementName) {
				return ListUtil.createOneElementList(getModelOpticsData()
						.getPhase(elementName, Plane.VERTICAL));
			}
		});
		this.dvView.addDataView(DvUtils.createDataView(chart));

		chart = getChartFactory().createBarChart(this.dataVDown, null, null,
				"muy [2pi]", "pos y [m]");
		chart.setMarkerXProvider(new MarkerXProvider() {
			@Override
			public List<Double> getXPositions(String elementName) {
				return ListUtil.createOneElementList(getModelOpticsData()
						.getPhase(elementName, Plane.VERTICAL));
			}
		});
		this.dvView.addDataView(DvUtils.createDataView(chart));
	}

	/**
	 * configures all dataSets
	 */
	private void configureDataSets() {
		configureDataSet(dataHUp, Plane.HORIZONTAL, DeflectionSign.PLUS);
		configureDataSet(dataVUp, Plane.VERTICAL, DeflectionSign.PLUS);
		configureDataSet(dataHDown, Plane.HORIZONTAL, DeflectionSign.MINUS);
		configureDataSet(dataVDown, Plane.VERTICAL, DeflectionSign.MINUS);
	}

	/**
	 * calculates the actual data and sets it to the dataset
	 * 
	 * @param dataSet
	 *            the dataSet to configure
	 * @param plane
	 *            the plane which to display
	 * @param sign
	 *            which trajectory to be contained.
	 */
	private void configureDataSet(MatrixColumnDataSet dataSet, Plane plane,
			DeflectionSign sign) {
		KickResponseData kickResponseData = getKickResponseData();
		if (kickResponseData == null) {
			return;
		}
		List<String> monitorNames = getMachineElementsManager()
				.getActiveMonitorNames(plane);

		dataSet.setLabels(monitorNames);
		dataSet.setValidityMatrix(kickResponseData.getTrajectoryValidity(plane,
				sign));
		dataSet.setMatrix(kickResponseData.getTrajectoryMatrix(plane, sign),
				getModelOpticsData().getMonitorPhases(plane));
	}

	/**
	 * @return the modelOpticsData
	 */
	private ModelOpticsData getModelOpticsData() {
		return getMeasurement().getModelDelegate().getModelOpticsData();
	}

	/**
	 * @param machineElementsManager
	 *            the machineElementsManager to set
	 */
	@Override
	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
		this.machineElementsManager
				.addListener(new MachineElementsManagerListener() {

					@Override
					public void changedActiveCorrector(int number,
							Corrector corrector) {
						dataHUp.setColumn(number);
						dataHDown.setColumn(number);
						dataVUp.setColumn(number);
						dataVDown.setColumn(number);
					}

					@Override
					public void changedActiveElements() {
						/* nothing to do */
					}

					@Override
					public void changedActiveMonitor(int number, Monitor monitor) {
						/* nothing to do */
					}

					@Override
					public void changedElements() {
						/* nothing to do */
					}

					@Override
					public void changedCorrectorGains() {
						/* nothing to do */
					}

					@Override
					public void changedMonitorGains() {
						/* nothing to do */
					}

				});
	}

	/**
	 * @return the machineElementsManager
	 */
	private MachineElementsManager getMachineElementsManager() {
		return machineElementsManager;
	}

	@Override
	public void refresh() {
		configureDataSets();
	}

	private KickResponseData getKickResponseData() {
		return getMeasurement().getData();
	}

	@Override
	public DVView getDVView() {
		return dvView;
	}
}
