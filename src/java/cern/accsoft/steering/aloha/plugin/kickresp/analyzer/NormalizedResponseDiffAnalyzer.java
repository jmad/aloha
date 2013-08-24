/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.kickresp.analyzer;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import cern.accsoft.steering.aloha.analyzer.AbstractAnalyzer;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.data.CombinedKickResponseData;
import cern.accsoft.steering.jmad.gui.mark.MarkerXProvider;
import cern.accsoft.steering.jmad.util.ListUtil;
import cern.accsoft.steering.util.gui.dv.ds.AbstractJmadDataSet;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
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
public class NormalizedResponseDiffAnalyzer extends
		AbstractAnalyzer<KickResponseMeasurementImpl> {
	private final static String ANALYZER_NAME = "Normalized Response Diff.";

	/* the dataSets */
	private NormalizedDataSet dataSetHMeas = new NormalizedDataSet(
			Plane.HORIZONTAL, 0);
	private NormalizedDataSet dataSetHMeasPlus = new NormalizedDataSet(
			Plane.HORIZONTAL, +1);
	private NormalizedDataSet dataSetHMeasMinus = new NormalizedDataSet(
			Plane.HORIZONTAL, -1);

	private NormalizedDataSet dataSetVMeas = new NormalizedDataSet(
			Plane.VERTICAL, 0);
	private NormalizedDataSet dataSetVMeasPlus = new NormalizedDataSet(
			Plane.VERTICAL, +1);
	private NormalizedDataSet dataSetVMeasMinus = new NormalizedDataSet(
			Plane.VERTICAL, -1);

	/** the dataViews for this analyzer */
	private DVView dvView = new DVView(ANALYZER_NAME, DVView.HORIZONTAL_LAYOUT);

	/** the optics data of the model */
	private ModelOpticsData modelOpticsData;

	/** the combined data, which we will display */
	private CombinedKickResponseData combinedKickResponseData;

	/** the manager, who keeps track of active elements */
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
		this.dvView.addDataView(createDataView(this.dataSetHMeas, null,
				new DefaultDataSource(new DataSet[] { dataSetHMeasPlus,
						dataSetHMeasMinus })));
		this.dvView.addDataView(createDataView(this.dataSetVMeas, null,
				new DefaultDataSource(new DataSet[] { dataSetVMeasPlus,
						dataSetVMeasMinus })));
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
				"phase [2 pi]", "response meas-model/sqrt(beta)");
		return DvUtils.createFitInteractorView(chart);
	}

	/**
	 * this is the dataset which returns the correct values for a normalized
	 * response.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private class NormalizedDataSet extends AbstractJmadDataSet implements
			MarkerXProvider {

		/*
		 * the variables which contain the values that will be displayed.
		 */
		private List<Double> phases = new ArrayList<Double>();
		private List<Double> normalizedValues = new ArrayList<Double>();

		/** the plane, which shall be represented by this dataset */
		private Plane plane;

		/**
		 * add or subtract the errors...
		 */
		private int errorAddFactor = 0;

		protected NormalizedDataSet(Plane plane, int errorAddFactor) {
			super("Normalized Response (" + plane.getTag() + ") "
					+ "measured-model");
			this.plane = plane;
			this.errorAddFactor = errorAddFactor;
		}

		@Override
		public int getDataCount() {
			return this.phases.size();
		}

		@Override
		public double getX(int index) {
			return this.phases.get(index);
		}

		@Override
		public double getY(int index) {
			return this.normalizedValues.get(index);
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

			int correctorNumber = getMachineElementsManager()
					.getActiveCorrectorNumber();

			super.setLabels(getMachineElementsManager().getActiveMonitorNames(
					this.plane));
			this.phases = getModelOpticsData().getMonitorPhases(this.plane);
			List<Double> betas = getModelOpticsData().getMonitorBetas(
					this.plane);
			Matrix diffMatrix = getCombinedKickResponseData()
					.getDifferenceMatrix();

			int monitorOffset = 0;
			if (Plane.VERTICAL.equals(this.plane)) {
				monitorOffset = getMachineElementsManager()
						.getActiveMonitorsCount(Plane.HORIZONTAL);
			}

			this.normalizedValues.clear();
			for (int i = 0; i < betas.size(); i++) {
				int responseIndex = i + monitorOffset;
				double reading = diffMatrix.get(responseIndex, correctorNumber);
				if (this.errorAddFactor != 0) {
					// TODO reactivate
					// reading += (this.errorAddFactor *
					// getKickResponseData().getRelativeMonitorNoises().get(
					// responseIndex, correctorNumber));
				}
				this.normalizedValues.add(reading / Math.sqrt(betas.get(i)));
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
	 * @param combinedKickResponseData
	 *            the combinedKickResponseData to set
	 */
	public void setCombinedKickResponseData(
			CombinedKickResponseData combinedKickResponseData) {
		this.combinedKickResponseData = combinedKickResponseData;
	}

	/**
	 * @return the combinedKickResponseData
	 */
	private CombinedKickResponseData getCombinedKickResponseData() {
		return combinedKickResponseData;
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

	@Override
	public void refresh() {
		dataSetHMeas.refresh();
		dataSetHMeasPlus.refresh();
		dataSetHMeasMinus.refresh();

		dataSetVMeas.refresh();
		dataSetVMeasPlus.refresh();
		dataSetVMeasMinus.refresh();
	}

	@Override
	public DVView getDVView() {
		return this.dvView;
	}

}
