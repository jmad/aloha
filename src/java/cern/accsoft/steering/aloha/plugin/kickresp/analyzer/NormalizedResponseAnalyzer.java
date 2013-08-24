/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.kickresp.analyzer;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.analyzer.AbstractAnalyzer;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.ModelKickResponseData;
import cern.accsoft.steering.jmad.gui.mark.MarkerXProvider;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.data.DefaultDataSource;
import cern.jdve.event.DataSetEvent;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;

/**
 * This class implements an Analyzer, that presents the Measured Data as a
 * Dataset, normalized with respect to sqrt(beta)
 * 
 * @author kfuchsbe
 * 
 */
public class NormalizedResponseAnalyzer extends
		AbstractAnalyzer<KickResponseMeasurementImpl> {
	private final static String ANALYZER_NAME = "Normalized Response";

	/* the dataSets */
	private NormalizedDataSet dataSetHMeas = new NormalizedDataSet(
			Plane.HORIZONTAL, false, 0);
	private NormalizedDataSet dataSetHMeasHi = new NormalizedDataSet(
			Plane.HORIZONTAL, false, 1);
	private NormalizedDataSet dataSetHMeasLo = new NormalizedDataSet(
			Plane.HORIZONTAL, false, -1);

	private NormalizedDataSet dataSetVMeas = new NormalizedDataSet(
			Plane.VERTICAL, false, 0);
	private NormalizedDataSet dataSetVMeasHi = new NormalizedDataSet(
			Plane.VERTICAL, false, 1);
	private NormalizedDataSet dataSetVMeasLo = new NormalizedDataSet(
			Plane.VERTICAL, false, -1);

	private NormalizedDataSet dataSetHModel = new NormalizedDataSet(
			Plane.HORIZONTAL, true, 0);
	private NormalizedDataSet dataSetVModel = new NormalizedDataSet(
			Plane.VERTICAL, true, 0);

	/** the dataViews for this analyzer */
	private DVView dvView = new DVView(ANALYZER_NAME, DVView.GRID_LAYOUT);

	private ModelKickResponseData modelKickResponseData;

	/** the optics-data */
	private ModelOpticsData modelOpticsData;

	/** the manager, which keeps track of all selected elements */
	private MachineElementsManager machineElementsManager;

	/**
	 * the init method
	 */
	public void init() {
		initDataViews();
	}

	/**
	 * creates the DataViews
	 */
	private void initDataViews() {
		this.dvView.removeAllDataViews();
		this.dvView.addDataView(createDataView(this.dataSetHMeas,
				this.dataSetHModel, new DefaultDataSource(new DataSet[] {
						this.dataSetHMeasLo, this.dataSetHMeasHi })));
		this.dvView.addDataView(createDataView(this.dataSetVMeas,
				this.dataSetVModel, new DefaultDataSource(new DataSet[] {
						this.dataSetVMeasLo, this.dataSetVMeasHi })));
	}

	/**
	 * creates the DataView out of the DataSet
	 * 
	 * @param dataSet
	 * @return the dataView
	 */
	private DataView createDataView(DataSet measurementDataSet,
			DataSet modelDataSet, DataSource measurementErrorDataSource) {
		if (getChartFactory() == null) {
			return null;
		}

		Aloha2DChart chart = getChartFactory().createBarChart(
				measurementDataSet, modelDataSet, measurementErrorDataSource,
				"phase [2 pi]", "response/sqrt(beta)");
		return DvUtils.createFitInteractorView(chart);
	}

	/**
	 * this is the dataset which returns the correct values for a normalized
	 * response.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private class NormalizedDataSet extends ListDataSet implements
			MarkerXProvider {

		/** the plane, which shall be represented by this dataset */
		private Plane plane;

		/** true, if we use the model data, false if we use the measured data */
		private boolean model;

		/**
		 * the factor, with which we add the relative errors (should be one of
		 * -1, 0 or +1)
		 */
		private int errorAddFactor = 0;

		protected NormalizedDataSet(Plane plane, boolean model,
				int errorAddFactor) {
			super("Normalized Response (" + plane.getTag() + ") - "
					+ (model ? "model" : "measured"));
			this.plane = plane;
			this.model = model;
			this.errorAddFactor = errorAddFactor;
		}

		/**
		 * cause all listeners to refresh
		 */
		public void refresh() {
			calc();
			setLimitsValid(false);
			fireDataSetChanged(new DataSetEvent(this, DataSetEvent.FULL_CHANGE));
		}

		/**
		 * (re)calculates the dataSet
		 */
		private void calc() {
			if (getMeasurement() == null) {
				return;
			}
			if (getModelOpticsData() == null) {
				return;
			}

			List<Double> normalizedValues = new ArrayList<Double>();
			List<Boolean> validityValues = new ArrayList<Boolean>();

			int correctorNumber = getMachineElementsManager()
					.getActiveCorrectorNumber();

			List<Double> betas = getModelOpticsData().getMonitorBetas(
					this.plane);

			int monitorOffset = 0;
			if (Plane.VERTICAL.equals(this.plane)) {
				monitorOffset = getMachineElementsManager()
						.getActiveMonitorsCount(Plane.HORIZONTAL);
			}

			for (int i = 0; i < betas.size(); i++) {
				int responseIndex = i + monitorOffset;
				if (!getMeasurement().getData().getValidityMatrix().get(
						responseIndex, correctorNumber)) {
					validityValues.add(false);
				} else {
					validityValues.add(true);
				}
				Double reading = getReading(responseIndex, correctorNumber);
				normalizedValues.add(reading / Math.sqrt(betas.get(i)));
			}
			super.setLabels(getMachineElementsManager().getActiveMonitorNames(
					this.plane));
			super.setValues(getModelOpticsData().getMonitorPhases(this.plane),
					normalizedValues, validityValues);
		}

		/**
		 * returns the reading, either from model or from measurement
		 * 
		 * @param monitorNumber
		 * @param correctorNumber
		 * @return the reading (response)
		 */
		private Double getReading(int monitorNumber, int correctorNumber) {
			if (this.model) {
				return getModelKickResponseData().getResponseMatrix().get(
						monitorNumber, correctorNumber);

			} else {
				Double value = getMeasurement().getData().getResponseMatrix()
						.get(monitorNumber, correctorNumber);
				if (this.errorAddFactor != 0) {
					// TODO reactivate! ?
					// value += (this.errorAddFactor *
					// getMeasurement().getRelativeMonitorNoises().get(
					// monitorNumber, correctorNumber));
				}
				return value;
			}
		}

		/*
		 * overrides MarkerXProvider
		 */
		@Override
		public List<Double> getXPositions(String elementName) {
			return ListUtil.createOneElementList(getModelOpticsData().getPhase(
					elementName, this.plane));
		}

	}
	
	/**
	 * @param modelOpticsData
	 *            the modelOpticsData to set
	 */
	public void setModelOpticsData(ModelOpticsData modelOpticsData) {
		this.modelOpticsData = modelOpticsData;
	}

	/**
	 * @return the modelOpticsData
	 */
	private ModelOpticsData getModelOpticsData() {
		return modelOpticsData;
	}

	/**
	 * @param machineElementsManager
	 *            the machineElementsManager to set
	 */
	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
	}

	/**
	 * @return the machineElementsManager
	 */
	private MachineElementsManager getMachineElementsManager() {
		return machineElementsManager;
	}

	public void setModelKickResponseData(
			ModelKickResponseData modelKickResponseData) {
		this.modelKickResponseData = modelKickResponseData;
	}

	private ModelKickResponseData getModelKickResponseData() {
		return modelKickResponseData;
	}

	@Override
	public void refresh() {
		dataSetHMeas.refresh();
		dataSetHMeasHi.refresh();
		dataSetHMeasLo.refresh();

		dataSetVMeas.refresh();
		dataSetVMeasLo.refresh();
		dataSetVMeasHi.refresh();

		dataSetHModel.refresh();
		dataSetVModel.refresh();
	}

	@Override
	public DVView getDVView() {
		return this.dvView;
	}

}
