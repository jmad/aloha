/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.traj.display;

import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.ChartFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.gui.display.AbstractDisplaySet;
import cern.accsoft.steering.aloha.gui.display.GenericDisplaySet;
import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.ErrorDataSetAdapter;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the displayset for a trajectory measurement
 * 
 * @author kfuchsbe
 * 
 */
public class TrajectoryDisplaySet extends
		GenericDisplaySet<TrajectoryMeasurement> implements
		MachineElementsManagerAware, ChartFactoryAware {

	/** the chart-factory to create appropriate charts for aloha */
	private ChartFactory chartFactory;

	/** the {@link MachineElementsManager} */
	private MachineElementsManager machineElementsManager;

	/*
	 * the datasets
	 */
	private enum DataSetType {
		MEAS_STABILITY_HV("Monitor Stability H+V"), //
		MEAS_STAB_H("Monitor Stability H"), //
		MEAS_STAB_V("Monitor Stability V"), //
		MEAS_TRAJ_HV("Measured Trajectory H+V"), //
		MEAS_TRAJ_H("Measured Trajectory H"), //
		MEAS_TRAJ_V("Measured Trajectory V"), //
		MODEL_TRAJ_HV("Model Trajectory H+V"), //
		MODEL_TRAJ_H("Model Trajectory H"), //
		MODEL_TRAJ_V("Model Trajectory V"), //
		MEAS_TRAJ_H_S("Measured Trajectory H (s)"), //
		MEAS_TRAJ_V_S("Measured Trajectory V (s)"), //
		MODEL_TRAJ_H_S("Model Trajectory H (s)"), //
		MODEL_TRAJ_V_S("Model Trajectory V (s)"), //
		MEAS_TRAJ_H_MU("Measured Trajectory H (mu)"), //
		MEAS_TRAJ_V_MU("Measured Trajectory V (mu)"), //
		MODEL_TRAJ_H_MU("Model Trajectory H (mu)"), //
		MODEL_TRAJ_V_MU("Measured Trajectory V (mu)");

		private String name;

		private DataSetType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	private Map<DataSetType, ListDataSet> dataSets = new HashMap<DataSetType, ListDataSet>();
	{
		for (DataSetType dsType : DataSetType.values()) {
			this.dataSets.put(dsType, new ListDataSet(dsType.getName()));
		}
	}

	public TrajectoryDisplaySet(TrajectoryMeasurement measurement) {
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
		Aloha2DChart chart;
		ListDataSet dsMeas;
		ListDataSet dsModel;

		/*
		 * Views (H+V):
		 * 
		 * Trajectory
		 */

		dvView = new DVView("Trajectory H+V");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dsMeas = getDs(DataSetType.MEAS_TRAJ_HV);
		dsModel = getDs(DataSetType.MODEL_TRAJ_HV);
		chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				"monitor", "pos [m]");
		chart.setMarkerXProvider(getMachineElementsManager()
				.getMonitorHVBorderProvider());
		dvView.addDataView(new DataView(chart));

		/*
		 * Stability
		 */
		dvView = new DVView("Stability H+V");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dsMeas = this.dataSets.get(DataSetType.MEAS_STABILITY_HV);
		chart = getChartFactory().createBarChart(dsMeas, null, null, "monitor",
				"rms [m]");
		chart.setMarkerXProvider(getMachineElementsManager()
				.getMonitorHVBorderProvider());
		dvView.addDataView(new DataView(chart));

		/*
		 * Views H/V separated
		 * 
		 * traj.
		 */
		dvView = new DVView("Trajectory H/V");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dsMeas = getDs(DataSetType.MEAS_TRAJ_H);
		dsModel = getDs(DataSetType.MODEL_TRAJ_H);
		chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				"monitor", "x [m]");
		dvView.addDataView(new DataView(chart));

		dsMeas = getDs(DataSetType.MEAS_TRAJ_V);
		dsModel = getDs(DataSetType.MODEL_TRAJ_V);
		chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				"monitor", "y [m]");
		dvView.addDataView(new DataView(chart));

		/* stability */
		dvView = new DVView("Stability H/V");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dsMeas = this.dataSets.get(DataSetType.MEAS_STAB_H);
		chart = getChartFactory().createBarChart(dsMeas, null, null, "monitor",
				"rms [m]");
		dvView.addDataView(new DataView(chart));

		dsMeas = this.dataSets.get(DataSetType.MEAS_STAB_V);
		chart = getChartFactory().createBarChart(dsMeas, null, null, "monitor",
				"rms [m]");
		dvView.addDataView(new DataView(chart));

		/*
		 * traj (s).
		 */
		dvView = new DVView("Trajectory H/V (s)");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dsMeas = getDs(DataSetType.MEAS_TRAJ_H_S);
		dsModel = getDs(DataSetType.MODEL_TRAJ_H_S);
		chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				"s [m]", "x [m]");
		dvView.addDataView(new DataView(chart));

		dsMeas = getDs(DataSetType.MEAS_TRAJ_V_S);
		dsModel = getDs(DataSetType.MODEL_TRAJ_V_S);
		chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				"s [m]", "y [m]");
		dvView.addDataView(new DataView(chart));

		/*
		 * traj (mu).
		 */
		dvView = new DVView("Trajectory H/V (mu)");
		dvView.setLayout(DVView.VERTICAL_LAYOUT);
		addDvView(dvView);

		dsMeas = getDs(DataSetType.MEAS_TRAJ_H_MU);
		dsModel = getDs(DataSetType.MODEL_TRAJ_H_MU);
		chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				"mux [2pi]", "x [m]");
		dvView.addDataView(new DataView(chart));

		dsMeas = getDs(DataSetType.MEAS_TRAJ_V_MU);
		dsModel = getDs(DataSetType.MODEL_TRAJ_V_MU);
		chart = getChartFactory().createBarChart(dsMeas, dsModel,
				ErrorDataSetAdapter.createDefaultErrorDataSource(dsMeas),
				"muy [2pi]", "y [m]");
		dvView.addDataView(new DataView(chart));

	}

	private ListDataSet getDs(DataSetType type) {
		return this.dataSets.get(type);
	}

	/**
	 * set new data to the datasets
	 */
	private void refreshDataSets() {
		TrajectoryData data = getMeasurement().getData();
		ModelOpticsData modelData = getMeasurement().getModelDelegate()
				.getModelOpticsData();

		ListDataSet ds;

		/* Stability H+V */
		ds = this.dataSets.get(DataSetType.MEAS_STABILITY_HV);
		ds.setValues(null, data.getRmsValues(), null, data.getValidityValues());
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames());

		/* Trajectory H+V */
		ds = this.dataSets.get(DataSetType.MEAS_TRAJ_HV);
		ds.setValues(null, data.getMeanValues(), data.getRmsValues(), data
				.getValidityValues());
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames());

		ds = this.dataSets.get(DataSetType.MODEL_TRAJ_HV);
		ds.setValues(null, modelData.getMonitorPos());
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames());

		/* Stability H/V */
		ds = this.dataSets.get(DataSetType.MEAS_STAB_H);
		ds.setValues(null, data.getRmsValues(Plane.HORIZONTAL), null, data
				.getValidityValues(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MEAS_STAB_V);
		ds.setValues(null, data.getRmsValues(Plane.VERTICAL), null, data
				.getValidityValues(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		/* Trajectory H/V */
		ds = this.dataSets.get(DataSetType.MEAS_TRAJ_H);
		ds.setValues(null, data.getMeanValues(Plane.HORIZONTAL), data
				.getRmsValues(Plane.HORIZONTAL), data
				.getValidityValues(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MODEL_TRAJ_H);
		ds.setValues(null, modelData.getMonitorPos(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MEAS_TRAJ_V);
		ds.setValues(null, data.getMeanValues(Plane.VERTICAL), data
				.getRmsValues(Plane.VERTICAL), data
				.getValidityValues(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		ds = this.dataSets.get(DataSetType.MODEL_TRAJ_V);
		ds.setValues(null, modelData.getMonitorPos(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		/* Trajectory H/V (s) */
		ds = this.dataSets.get(DataSetType.MEAS_TRAJ_H_S);
		ds.setValues(modelData.getMonitorSPositions(Plane.HORIZONTAL), data
				.getMeanValues(Plane.HORIZONTAL), data
				.getRmsValues(Plane.HORIZONTAL), data
				.getValidityValues(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MODEL_TRAJ_H_S);
		ds.setValues(modelData.getAllSPositions(), modelData
				.getAllPos(Plane.HORIZONTAL));
		ds.setLabels(modelData.getAllNames());

		ds = this.dataSets.get(DataSetType.MEAS_TRAJ_V_S);
		ds.setValues(modelData.getMonitorSPositions(Plane.VERTICAL), data
				.getMeanValues(Plane.VERTICAL), data
				.getRmsValues(Plane.VERTICAL), data
				.getValidityValues(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		ds = this.dataSets.get(DataSetType.MODEL_TRAJ_V_S);
		ds.setValues(modelData.getAllSPositions(), modelData
				.getAllPos(Plane.VERTICAL));
		ds.setLabels(modelData.getAllNames());

		/* Trajectory H/V (mu) */
		ds = this.dataSets.get(DataSetType.MEAS_TRAJ_H_MU);
		ds.setValues(modelData.getMonitorPhases(Plane.HORIZONTAL), data
				.getMeanValues(Plane.HORIZONTAL), data
				.getRmsValues(Plane.HORIZONTAL), data
				.getValidityValues(Plane.HORIZONTAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.HORIZONTAL));

		ds = this.dataSets.get(DataSetType.MODEL_TRAJ_H_MU);
		ds.setValues(modelData.getAllPhases(Plane.HORIZONTAL), modelData
				.getAllPos(Plane.HORIZONTAL));
		ds.setLabels(modelData.getAllNames());

		ds = this.dataSets.get(DataSetType.MEAS_TRAJ_V_MU);
		ds.setValues(modelData.getMonitorPhases(Plane.VERTICAL), data
				.getMeanValues(Plane.VERTICAL), data
				.getRmsValues(Plane.VERTICAL), data
				.getValidityValues(Plane.VERTICAL));
		ds.setLabels(getMachineElementsManager().getActiveMonitorNames(
				Plane.VERTICAL));

		ds = this.dataSets.get(DataSetType.MODEL_TRAJ_V_MU);
		ds.setValues(modelData.getAllPhases(Plane.VERTICAL), modelData
				.getAllPos(Plane.VERTICAL));
		ds.setLabels(modelData.getAllNames());

	}

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
