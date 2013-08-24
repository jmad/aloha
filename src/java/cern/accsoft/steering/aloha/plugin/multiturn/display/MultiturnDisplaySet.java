/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.display;

import java.util.HashMap;
import java.util.Map;

import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.ChartFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.gui.display.AbstractDisplaySet;
import cern.accsoft.steering.aloha.gui.display.GenericDisplaySet;
import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.MultiturnMeasurement;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDifferenceData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnVar;
import cern.accsoft.steering.jmad.domain.var.Variable;
import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.ErrorDataSetAdapter;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;

/**
 * this is the displayset for a multiturn measurement
 * 
 * @author kfuchsbe
 * 
 */
public class MultiturnDisplaySet extends
		GenericDisplaySet<MultiturnMeasurement> implements
		MachineElementsManagerAware, ChartFactoryAware {

	/** the chart-factory to create appropriate charts for aloha */
	private ChartFactory chartFactory;

	/** the {@link MachineElementsManager} */
	private MachineElementsManager machineElementsManager;

	/*
	 * the datasets
	 */
	private enum DataSetType {
		MEAS_BETA_H("Monitor beta H", null), //
		MEAS_BETA_V("Monitor beta V", null), //
		MEAS_BETA_H_S("Measured beta H (s)",MadxTwissVariable.S), //
		MEAS_BETA_V_S("Measured beta V (s)", MadxTwissVariable.S), //
		MODEL_BETA_H("Monitor beta H", null), //
		MODEL_BETA_V("Monitor beta V", null), //
		MODEL_BETA_H_S("Measured beta H (s)", MadxTwissVariable.S), //
		MODEL_BETA_V_S("Measured beta V (s)", MadxTwissVariable.S), //
		BEATING_BETA_H("beta-beating H", null), //
		BEATING_BETA_V("beta-beating V", null), //
		BEATING_BETA_H_S("beta-beating H (s)", MadxTwissVariable.S), //
		BEATING_BETA_V_S("beta-beating V (s)", MadxTwissVariable.S);

		private String name;
		private Variable xVariable;

		private DataSetType(String name, Variable xVariable) {
			this.name = name;
			this.xVariable = xVariable;
		}

		public String getName() {
			return this.name;
		}

		
		@SuppressWarnings("unused")
		public Variable getXVariable() {
			return this.xVariable;
		}

	}

	private Map<DataSetType, ListDataSet> dataSets = new HashMap<DataSetType, ListDataSet>();
	{
		for (DataSetType dsType : DataSetType.values()) {
			this.dataSets.put(dsType, new ListDataSet(dsType.getName()));
		}
	}

	public MultiturnDisplaySet(MultiturnMeasurement measurement) {
		super(measurement);
	}

	/**
	 * init method, which is automatically called after the beans are injected.
	 */
	@InitMethod
	public void init() {
		initComponents();
	}

	@Override
	protected void doRefresh() {
		refreshDataSets();
	}

	//
	// private methods
	//

	/**
	 * create all the components and add them to the {@link AbstractDisplaySet}
	 */
	private void initComponents() {

		DVView dvView;

		/*
		 * Views (H/V):
		 * 
		 * Beta
		 */
		dvView = new DVView("Beta H/V");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dvView.addDataView(createDataView(DataSetType.MEAS_BETA_H,
				DataSetType.MODEL_BETA_H, "monitor", "betax [m]"));
		dvView.addDataView(createDataView(DataSetType.MEAS_BETA_V,
				DataSetType.MODEL_BETA_V, "monitor", "betay [m]"));

		/*
		 * beta (s).
		 */
		dvView = new DVView("Beta H/V (s)");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dvView.addDataView(createDataView(DataSetType.MEAS_BETA_H_S,
				DataSetType.MODEL_BETA_H_S, "s [m]", "betax [m]"));
		dvView.addDataView(createDataView(DataSetType.MEAS_BETA_V_S,
				DataSetType.MODEL_BETA_V_S, "s [m]", "betay [m]"));

		dvView = new DVView("Beta beating H/V");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);
		dvView.addDataView(createDataView(DataSetType.BEATING_BETA_H, null,
				"monitor", "(betax-betax0)/betax0"));
		dvView.addDataView(createDataView(DataSetType.BEATING_BETA_V, null,
				"monitor", "(betay-betay0)/betay0"));

		dvView = new DVView("Beta beating H/V");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);
		dvView.addDataView(createDataView(DataSetType.BEATING_BETA_H_S, null,
				"s [m]", "(betax-betax0)/betax0"));
		dvView.addDataView(createDataView(DataSetType.BEATING_BETA_V_S, null,
				"s [m]", "(betay-betay0)/betay0"));
	}

	private DataView createDataView(DataSetType measDsType,
			DataSetType modelDsType, String xTitle, String yTitle) {

		ListDataSet dsMeas = getDs(measDsType);
		ListDataSet dsModel = getDs(modelDsType);
		Aloha2DChart chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				xTitle, yTitle);
		return new DataView(chart);
	}

	private ListDataSet getDs(DataSetType type) {
		return this.dataSets.get(type);
	}

	/**
	 * set new data to the datasets
	 */
	private void refreshDataSets() {
		MultiturnData data = getMeasurement().getData();
		ModelOpticsData modelData = getMeasurement().getModelDelegate()
				.getModelOpticsData();
		MultiturnDifferenceData diffData = getMeasurement().getDiffData();

		ListDataSet ds;

		/* Beta H/V */
		ds = this.dataSets.get(DataSetType.MEAS_BETA_H);
		ds.setValues(null, data.getValues(MultiturnVar.BETA, Plane.HORIZONTAL),
				data.getValues(MultiturnVar.BETA_ERROR, Plane.HORIZONTAL), data
						.getValidityValues(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MODEL_BETA_H);
		ds.setValues(null, modelData.getMonitorBetas(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MEAS_BETA_V);
		ds.setValues(null, data.getValues(MultiturnVar.BETA, Plane.VERTICAL),
				data.getValues(MultiturnVar.BETA_ERROR, Plane.VERTICAL), data
						.getValidityValues(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		ds = this.dataSets.get(DataSetType.MODEL_BETA_V);
		ds.setValues(null, modelData.getMonitorBetas(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		/* Beta H/V (s) */
		ds = this.dataSets.get(DataSetType.MEAS_BETA_H_S);
		ds.setValues(modelData.getMonitorSPositions(Plane.HORIZONTAL), data
				.getValues(MultiturnVar.BETA, Plane.HORIZONTAL), data
				.getValues(MultiturnVar.BETA_ERROR, Plane.HORIZONTAL), data
				.getValidityValues(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MODEL_BETA_H_S);
		ds.setValues(modelData.getAllSPositions(), modelData
				.getAllBetas(Plane.HORIZONTAL));
		ds.setLabels(modelData.getAllNames());

		ds = this.dataSets.get(DataSetType.MEAS_BETA_V_S);
		ds.setValues(modelData.getMonitorSPositions(Plane.VERTICAL), data
				.getValues(MultiturnVar.BETA, Plane.VERTICAL), data.getValues(
				MultiturnVar.BETA_ERROR, Plane.VERTICAL), data
				.getValidityValues(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		ds = this.dataSets.get(DataSetType.MODEL_BETA_V_S);
		ds.setValues(modelData.getAllSPositions(), modelData
				.getAllBetas(Plane.VERTICAL));
		ds.setLabels(modelData.getAllNames());

		ds = this.dataSets.get(DataSetType.BEATING_BETA_H);
		Plane plane = Plane.HORIZONTAL;
		ds.setValues(null, diffData.getBeatingValues(MultiturnVar.BETA, plane),
				diffData.getBeatingValues(MultiturnVar.BETA_ERROR, plane), data
						.getValidityValues(plane));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(plane));

		ds = this.dataSets.get(DataSetType.BEATING_BETA_V);
		plane = Plane.VERTICAL;
		ds.setValues(null, diffData.getBeatingValues(MultiturnVar.BETA, plane),
				diffData.getBeatingValues(MultiturnVar.BETA_ERROR, plane), data
						.getValidityValues(plane));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(plane));

		ds = this.dataSets.get(DataSetType.BEATING_BETA_H_S);
		plane = Plane.HORIZONTAL;
		ds.setValues(modelData.getMonitorSPositions(plane), diffData
				.getBeatingValues(MultiturnVar.BETA, plane), diffData
				.getBeatingValues(MultiturnVar.BETA_ERROR, plane), data
				.getValidityValues(plane));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(plane));

		ds = this.dataSets.get(DataSetType.BEATING_BETA_V_S);
		plane = Plane.VERTICAL;
		ds.setValues(modelData.getMonitorSPositions(plane), diffData
				.getBeatingValues(MultiturnVar.BETA, plane), diffData
				.getBeatingValues(MultiturnVar.BETA_ERROR, plane), data
				.getValidityValues(plane));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(plane));

	}

//	private void updateDataSet(DataSetType dsType, MultiturnData measData,
//			ModelOpticsData modelData) {
//		ListDataSet ds = this.dataSets.get(dsType);
//
//		List<Double> xValues = null;
//
//		/*
//		 * we need:
//		 * 
//		 * xValues yValues validityValues labels
//		 * 
//		 * x defined by DataLength JMadPlane MadxVariable
//		 * 
//		 * Data method
//		 */
//	}

	//
	// getters and setters
	//

	@Override
	public void setChartFactory(ChartFactory chartFactory) {
		this.chartFactory = chartFactory;
	}

	private ChartFactory getChartFactory() {
		return this.chartFactory;
	}

	@Override
	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
	}

	private MachineElementsManager getMachineElementsManager() {
		return this.machineElementsManager;
	}
}
