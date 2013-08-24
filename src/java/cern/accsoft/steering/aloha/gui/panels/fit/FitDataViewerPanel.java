package cern.accsoft.steering.aloha.gui.panels.fit;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.gui.panels.DataViewsManager;
import cern.accsoft.steering.aloha.gui.panels.fit.FitDataSetManager.DS;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.ErrorDataSet;
import cern.accsoft.steering.util.gui.dv.ds.ErrorDataSetAdapter;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;
import cern.jdve.viewer.DataViewer;

public class FitDataViewerPanel extends JPanel {
    private static final long serialVersionUID = -5359391586208558362L;

    private DataViewer dataViewer = new DataViewer();

    private ChartFactory chartFactory;

    private FitDataSetManager dataSetManager;

    private MachineElementsManager machineElementsManager;

    private DataViewsManager dataViewsManager;

    private IterationChartFactory iterationChartFactory;

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

        Aloha2DChart chart;
        DVView dvView;

        /*
         * The charts for the monitor and corrector gains
         */
        dvView = new DVView("gains");
        getDataViewer().addView(dvView);
        dvView.setLayout(DVView.VERTICAL_LAYOUT);

        chart = createBarChart(DS.MONITOR_GAINS, null, "monitor", "gain", true);
        chart.setMarkerXProvider(getMachineElementsManager().getMonitorHVBorderProvider());
        dvView.addDataView(new DataView(chart));

        chart = createBarChart(DS.CORRECTOR_GAINS, null, "corrector", "gain", true);
        chart.setMarkerXProvider(getMachineElementsManager().getCorrectorHVBorderProvider());
        dvView.addDataView(new DataView(chart));

        /*
         * The chart for values of additional Parameters
         */
        dvView = new DVView("param values");
        getDataViewer().addView(dvView);
        dvView.setLayout(DVView.VERTICAL_LAYOUT);

        chart = createBarChart(DS.VARIATION_PARAMETER_VALUES, DS.VARIATION_PARAMETER_INITIAL_VALUES, "parameter",
                "value", true);
        dvView.addDataView(new DataView(chart));
        
        /*
         * The chart for changes of additional Parameters
         */
        dvView = new DVView("param changes");
        getDataViewer().addView(dvView);
        dvView.setLayout(DVView.VERTICAL_LAYOUT);

        chart = createBarChart(DS.VARIATION_PARAMETER_CHANGES, null, "parameter",
                "value change", true);
        dvView.addDataView(new DataView(chart));

        /*
         * The chart for relative changes of additional Parameters
         */
        dvView = new DVView("param rel changes");
        getDataViewer().addView(dvView);
        dvView.setLayout(DVView.VERTICAL_LAYOUT);

        chart = createBarChart(DS.VARIATION_PARAMETER_RELATIVE_CHANGES, null, "parameter",
                "relative value change", true);
        dvView.addDataView(new DataView(chart));

        
        /*
         * the chart for change in parameter values
         */
        dvView = new DVView("all param change");
        getDataViewer().addView(dvView);
        dvView.setLayout(DVView.VERTICAL_LAYOUT);

        chart = createBarChart(DS.DELTA_PARAMETER_VALUES, null, "parameter", "delta", false);
        dvView.addDataView(new DataView(chart));

        /*
         * The chart for the difference-vector
         */
        dvView = new DVView("difference");
        getDataViewer().addView(dvView);
        dvView.setLayout(DVView.VERTICAL_LAYOUT);

        chart = createBarChart(DS.DIFFERENCE_VECTOR, null, "index", "diff", false);
        dvView.addDataView(new DataView(chart));

        for (DVView dvV : getIterationChartFactory().getDvViews()) {
            getDataViewer().addView(dvV);
        }

        /*
         * register the dataviews to the manager so they can be treated by the dv-dialog
         */
        getDataViewsManager().registerDataViews(getDataViewer());
    }

    /**
     * creates the default BarChart
     * 
     * @param measurementDsType the dataset-type for the measurement
     * @param modelDsType the dataset-type for the model
     * @param xtitle the title of the x-axis.
     * @param ytitle the title of the y-axis.
     * @return
     */
    private Aloha2DChart createBarChart(DS measurementDsType, DS modelDsType, String xtitle, String ytitle,
            boolean measuredErrors) {
        if (getChartFactory() == null) {
            return null;
        }
        DataSet measurementDataSet = getDataSetManager().getDataSet(measurementDsType);
        DataSet modelDataSet = getDataSetManager().getDataSet(modelDsType);
        DataSource errorDataSource = null;
        if (measuredErrors && (measurementDataSet instanceof ErrorDataSet)) {
            errorDataSource = ErrorDataSetAdapter.createDefaultErrorDataSource((ErrorDataSet) measurementDataSet);
        }
        return getChartFactory().createBarChart(measurementDataSet, modelDataSet, errorDataSource, xtitle, ytitle);
    }

    /**
     * @return the dataViewer
     */
    public DataViewer getDataViewer() {
        return dataViewer;
    }

    public void setChartFactory(ChartFactory alohaChartFactory) {
        this.chartFactory = alohaChartFactory;
    }

    private ChartFactory getChartFactory() {
        return chartFactory;
    }

    public void setDataSetManager(FitDataSetManager dataSetManager) {
        this.dataSetManager = dataSetManager;
    }

    private FitDataSetManager getDataSetManager() {
        return dataSetManager;
    }

    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    private MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    public void setDataViewsManager(DataViewsManager dataViewsManager) {
        this.dataViewsManager = dataViewsManager;
    }

    private DataViewsManager getDataViewsManager() {
        return dataViewsManager;
    }

    public void setIterationChartFactory(IterationChartFactory iterationChartFactory) {
        this.iterationChartFactory = iterationChartFactory;
    }

    private IterationChartFactory getIterationChartFactory() {
        return iterationChartFactory;
    }

}
