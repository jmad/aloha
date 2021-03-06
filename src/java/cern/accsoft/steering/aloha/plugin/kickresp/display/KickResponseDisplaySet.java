/**
 *
 */
package cern.accsoft.steering.aloha.plugin.kickresp.display;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.gui.display.AbstractDisplaySet;
import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.gui.dv.MatrixCoordinatesPane;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManagerListener;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurementImpl;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.accsoft.steering.util.gui.dv.ds.MatrixColumnDataSet;
import cern.accsoft.steering.util.gui.dv.ds.MatrixDataSet;
import cern.accsoft.steering.util.gui.dv.ds.MatrixRowColDataSet;
import cern.accsoft.steering.util.gui.dv.ds.MatrixRowColDataSet.MatrixDsType;
import cern.accsoft.steering.util.gui.dv.ds.MatrixRowDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.jdve.Chart;
import cern.jdve.ChartInteractor;
import cern.jdve.Style;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSet3D;
import cern.jdve.data.DataSource;
import cern.jdve.data.DefaultDataSource;
import cern.jdve.event.ChartInteractionEvent;
import cern.jdve.event.ChartInteractionListener;
import cern.jdve.interactor.DataPickerInteractor;
import cern.jdve.renderer.ContourChartRenderer;
import cern.jdve.utils.DisplayPoint;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

/**
 * this is a special implementation of a DisplaySet for a kick-response measurement
 *
 * @author kfuchsbe
 */
public class KickResponseDisplaySet extends AbstractDisplaySet implements AlohaBeanFactoryAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(KickResponseDisplaySet.class);

    private final static String TITLE_CORRECTOR = "corrector";
    private final static String TITLE_MONITOR = "monitor";
    private final static String TITLE_RESPONSE = "response [m/rad]";
    private final static String TITLE_RESPONSE_DIFFERENCE = "response difference [m/rad]";
    private final static String TITLE_RESPONSE_DIFFERENCE_RMS = "response difference rms [m/rad]";
    private final static String TITLE_RELATIVE_RESPONSE_DIFFERENCE = "difference/error [1]";
    private final static String TITLE_RELATIVE_RESPONSE_DIFFERENCE_RMS = "difference/error rms [1]";

    /** The measuremet which this displaySet displays. */
    private KickResponseMeasurementImpl measurement;

    /** the chart-factory to use. */
    private ChartFactory chartFactory;

    /** the bean factory to create/configure other plugin objects */
    private AlohaBeanFactory alohaBeanFactory;

    /** here we keep all the DsAdapters */
    private HashMap<DS, DsAdapter> dsAdapters = new HashMap<>();

    private enum DS {
        MEASURED_CORRECTOR_RESPONSE, MODEL_CORRECTOR_RESPONSE, //
        MEASURED_CORRECTOR_RESPONSE_H, MODEL_CORRECTOR_RESPONSE_H, //
        MEASURED_CORRECTOR_RESPONSE_V, MODEL_CORRECTOR_RESPONSE_V, //
        MEASURED_MONITOR_RESPONSE, MODEL_MONITOR_RESPONSE, //
        MONITOR_DIFFERENCE, //
        MONITOR_DIFFERENCE_RMS, CORRECTOR_DIFFERENCE, //
        CORRECTOR_DIFFERENCE_RMS, RELATIVE_MONITOR_DIFFERENCE, //
        RELATIVE_MONITOR_DIFFERENCE_RMS, RELATIVE_CORRECTOR_DIFFERENCE, //
        RELATIVE_CORRECTOR_DIFFERENCE_RMS, //
        MEASURED_CORRECTOR_RESPONSE_HILO, //
        MEASURED_MONITOR_RESPONSE_HILO, //
        MEASURED_CORRECTOR_RESPONSE_H_HILO, //
        MEASURED_CORRECTOR_RESPONSE_V_HILO, //
        /* the matrix */
        MEASURED_RESPONSE_MATRIX(DsType.DS3D), MODEL_RESPONSE_MATRIX(DsType.DS3D), // 
        ABSOLUTE_DIFFERENCE_MATRIX(DsType.DS3D), RELATIVE_DIFFERENCE_MATRIX(DsType.DS3D);

        private DsType type = DsType.DS2D;

        /**
         * default constructor
         */
        private DS() {
            /* nothing to do */
        }

        private DS(DsType type) {
            this();
            this.type = type;
        }

        public DsType getType() {
            return this.type;
        }

        public static List<DS> fromType(DsType type) {
            List<DS> values = new ArrayList<>();
            for (DS value : DS.values()) {
                if (value.getType().equals(type)) {
                    values.add(value);
                }
            }
            return values;
        }
    }

    private enum DsType {
        DS2D, DS3D;
    }


    public KickResponseDisplaySet(KickResponseMeasurementImpl measurement, ChartFactory chartFactory) {
        this.measurement = measurement;
        this.chartFactory = chartFactory;

    }

    @InitMethod
    public void init() {
        for (DVView dvView : createDataViews()) {
            addDvView(dvView);
        }
        setDetailPanel(createDetailPanel());
    }

    /**
     * creates all DataViews
     */
    private List<DVView> createDataViews() {
        List<DVView> dvViews = new ArrayList<>();

        Aloha2DChart chart;

        /*
         * comparison view
         */
        DVView viewCompare = new DVView("Compare H+V");
        viewCompare.setLayout(DVView.VERTICAL_LAYOUT);
        dvViews.add(viewCompare);

        /*
         * The chart for comparison of responses
         */
        chart = createBarChart(DS.MEASURED_CORRECTOR_RESPONSE, DS.MODEL_CORRECTOR_RESPONSE,
                DS.MEASURED_CORRECTOR_RESPONSE_HILO, TITLE_MONITOR, TITLE_RESPONSE);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getMonitorHVBorderProvider());
        viewCompare.addDataView(new DataView(chart));

        /*
         * The chart for comparison of Monitor - responses
         */
        chart = createBarChart(DS.MEASURED_MONITOR_RESPONSE, DS.MODEL_MONITOR_RESPONSE,
                DS.MEASURED_MONITOR_RESPONSE_HILO, TITLE_CORRECTOR, TITLE_RESPONSE);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getCorrectorHVBorderProvider());
        viewCompare.addDataView(new DataView(chart));

        /*
         * view to compare corrector responses per plane
         */
        DVView viewCompareHV = new DVView("Compare H/V");
        viewCompareHV.setLayout(DVView.VERTICAL_LAYOUT);
        dvViews.add(viewCompareHV);

        chart = createBarChart(DS.MEASURED_CORRECTOR_RESPONSE_H, DS.MODEL_CORRECTOR_RESPONSE_H,
                DS.MEASURED_CORRECTOR_RESPONSE_H_HILO, TITLE_MONITOR, TITLE_RESPONSE);
        viewCompareHV.addDataView(new DataView(chart));

        chart = createBarChart(DS.MEASURED_CORRECTOR_RESPONSE_V, DS.MODEL_CORRECTOR_RESPONSE_V,
                DS.MEASURED_CORRECTOR_RESPONSE_V_HILO, TITLE_MONITOR, TITLE_RESPONSE);
        viewCompareHV.addDataView(new DataView(chart));

        /*
         * Difference - Charts
         */
        DVView viewDifference = new DVView("Absolute Diff");
        viewDifference.setLayout(DVView.GRID_LAYOUT);
        dvViews.add(viewDifference);

        chart = createBarChart(DS.MONITOR_DIFFERENCE, null, null, TITLE_CORRECTOR, TITLE_RESPONSE_DIFFERENCE);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getCorrectorHVBorderProvider());
        viewDifference.addDataView(new DataView(chart));

        chart = createBarChart(DS.CORRECTOR_DIFFERENCE, null, null, TITLE_MONITOR, TITLE_RESPONSE_DIFFERENCE);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getMonitorHVBorderProvider());
        viewDifference.addDataView(new DataView(chart));

        chart = createBarChart(DS.MONITOR_DIFFERENCE_RMS, null, null, TITLE_MONITOR, TITLE_RESPONSE_DIFFERENCE_RMS);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getMonitorHVBorderProvider());
        viewDifference.addDataView(new DataView(chart));

        chart = createBarChart(DS.CORRECTOR_DIFFERENCE_RMS, null, null, TITLE_CORRECTOR, TITLE_RESPONSE_DIFFERENCE_RMS);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getCorrectorHVBorderProvider());
        viewDifference.addDataView(new DataView(chart));

        /*
         * relative - Charts
         */
        DVView viewRelative = new DVView("Relative Diff");
        viewRelative.setLayout(DVView.GRID_LAYOUT);
        dvViews.add(viewRelative);

        chart = createBarChart(DS.RELATIVE_MONITOR_DIFFERENCE, null, null, TITLE_MONITOR,
                TITLE_RELATIVE_RESPONSE_DIFFERENCE);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getCorrectorHVBorderProvider());
        viewRelative.addDataView(new DataView(chart));

        chart = createBarChart(DS.RELATIVE_CORRECTOR_DIFFERENCE, null, null, TITLE_CORRECTOR,
                TITLE_RELATIVE_RESPONSE_DIFFERENCE);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getMonitorHVBorderProvider());
        viewRelative.addDataView(new DataView(chart));

        chart = createBarChart(DS.RELATIVE_MONITOR_DIFFERENCE_RMS, null, null, TITLE_MONITOR,
                TITLE_RELATIVE_RESPONSE_DIFFERENCE_RMS);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getMonitorHVBorderProvider());
        viewRelative.addDataView(new DataView(chart));

        chart = createBarChart(DS.RELATIVE_CORRECTOR_DIFFERENCE_RMS, null, null, TITLE_CORRECTOR,
                TITLE_RELATIVE_RESPONSE_DIFFERENCE_RMS);
        chart.setMarkerXProvider(measurement.getMachineElementsManager().getCorrectorHVBorderProvider());
        viewRelative.addDataView(new DataView(chart));

        return dvViews;
    }

    /**
     * creates the default BarChart
     *
     * @param measurementDsType the dataset-type for the measurement
     * @param modelDsType the dataset-type for the model
     * @param measurementDsrcTypeError the datasource containing the measurement errors
     * @param xtitle the title of the x-axis.
     * @param ytitle the title of the y-axis.
     * @return
     */
    private Aloha2DChart createBarChart(DS measurementDsType, DS modelDsType, DS measurementDsrcTypeError,
                                        String xtitle, String ytitle) {
        if (getChartFactory() == null) {
            return null;
        }

        DataSet measurementDataSet = getDataSet(measurementDsType);
        DataSet modelDataSet = getDataSet(modelDsType);
        DataSource measurementErrorDataSource = getDataSource(measurementDsrcTypeError);
        return getChartFactory().createBarChart(measurementDataSet, modelDataSet, measurementErrorDataSource, xtitle,
                ytitle);
    }

    /**
     * getter method for dataSources
     *
     * @param ds the type of the dataSource to get
     * @return the dataSource
     */
    private DataSource getDataSource(DS ds) {
        if (ds == null) {
            return null;
        }

        DsAdapter adapter = dsAdapters.get(ds);
        if (adapter == null) {
            adapter = createDsAdapter(ds);
            measurement.getMachineElementsManager().addListener(adapter);
            dsAdapters.put(ds, adapter);
        }
        if (adapter != null) {
            return adapter.dataSource();
        } else {
            return null;
        }
    }

    /**
     * getter method for 2D - DataSets. Lazy loading.
     *
     * @param ds the type of the 2D - DataSet
     * @return the DataSet.
     */
    private DataSet getDataSet(DS ds) {
        return Optional.ofNullable(getDataSource(ds))
                .map(DataSource::getDataSets)
                .map(dataSets -> dataSets[0])
                .orElse(null);
    }

    private DsAdapter createDsAdapter(DS ds) {
        DsAdapter adapter = null;
        switch (ds) {
            case MEASURED_CORRECTOR_RESPONSE:
                adapter = new CorrectorResponseAdapter(new MatrixColumnDataSet("Measured Response for one Corrector."),
                        MeasModelType.MEASUREMENT);
                break;
            case MODEL_CORRECTOR_RESPONSE:
                adapter = new CorrectorResponseAdapter(new MatrixColumnDataSet("Model Response for one Corrector."),
                        MeasModelType.MODEL);
                break;
            case MEASURED_CORRECTOR_RESPONSE_H:
                adapter = new CorrectorResponseAdapter(new MatrixColumnDataSet("Measured Response (H) for one Corrector."),
                        MeasModelType.MEASUREMENT, Plane.HORIZONTAL);
                break;
            case MODEL_CORRECTOR_RESPONSE_H:
                adapter = new CorrectorResponseAdapter(new MatrixColumnDataSet("Model Response (H) for one Corrector."),
                        MeasModelType.MODEL, Plane.HORIZONTAL);
                break;
            case MEASURED_CORRECTOR_RESPONSE_V:
                adapter = new CorrectorResponseAdapter(new MatrixColumnDataSet("Measured Response (V) for one Corrector."),
                        MeasModelType.MEASUREMENT, Plane.VERTICAL);
                break;
            case MODEL_CORRECTOR_RESPONSE_V:
                adapter = new CorrectorResponseAdapter(new MatrixColumnDataSet("Model Response (V) for one Corrector."),
                        MeasModelType.MODEL, Plane.VERTICAL);
                break;
            case MEASURED_MONITOR_RESPONSE:
                adapter = new SingleDsAdapter<MatrixRowDataSet>(new MatrixRowDataSet("Measured responses of one Monitor")) {
                    @Override
                    public void changedActiveMonitor(int number, Monitor monitor) {
                        dataSet.setRow(number);
                        dataSet.setName("Monitor: " + monitor.getName() + " (measured)");
                    }

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setMatrix(measurement.getData().getResponseMatrix());
                        dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());
                    }
                };
                break;

            case MEASURED_CORRECTOR_RESPONSE_HILO:
                MatrixColumnDataSet loCorrDs = new MatrixColumnDataSet("measured response for one corrector, lower limit");
                loCorrDs.setOffsetSubtract(true);
                MatrixColumnDataSet hiCorrDs = new MatrixColumnDataSet("measured response for one corrector, higher limit");
                hiCorrDs.setOffsetSubtract(false);
                adapter = new HiLowMatrixDsAdapter(loCorrDs, hiCorrDs);
                break;

            case MEASURED_MONITOR_RESPONSE_HILO:
                MatrixRowDataSet loMonDs = new MatrixRowDataSet("measured response for one monitor, lower limit");
                loMonDs.setOffsetSubtract(true);
                MatrixRowDataSet hiMonDs = new MatrixRowDataSet("measured response for one monitor, higher limit");
                hiMonDs.setOffsetSubtract(false);
                adapter = new HiLowMatrixDsAdapter(loMonDs, hiMonDs);
                break;
            case MEASURED_CORRECTOR_RESPONSE_H_HILO:
                MatrixColumnDataSet loCorrHDs = new MatrixColumnDataSet("measured response for one corrector, lower limit");
                loCorrHDs.setOffsetSubtract(true);
                MatrixColumnDataSet hiCorrHDs = new MatrixColumnDataSet("measured response for one corrector, higher limit");
                hiCorrHDs.setOffsetSubtract(false);
                adapter = new HiLowMatrixDsAdapter(loCorrHDs, hiCorrHDs, Plane.HORIZONTAL);
                break;

            case MEASURED_CORRECTOR_RESPONSE_V_HILO:
                MatrixColumnDataSet loCorrVDs = new MatrixColumnDataSet("measured response for one corrector, lower limit");
                loCorrVDs.setOffsetSubtract(true);
                MatrixColumnDataSet hiCorrVDs = new MatrixColumnDataSet("measured response for one corrector, higher limit");
                hiCorrVDs.setOffsetSubtract(false);
                adapter = new HiLowMatrixDsAdapter(loCorrVDs, hiCorrVDs, Plane.VERTICAL);
                break;


            case MODEL_MONITOR_RESPONSE:
                adapter = new SingleDsAdapter<MatrixRowDataSet>(new MatrixRowDataSet("Model responses of one Monitor")) {

                    @Override
                    public void changedActiveMonitor(int number, Monitor monitor) {
                        dataSet.setRow(number);
                        dataSet.setName("Monitor: " + monitor.getName() + " (model)");
                    }

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());
                        dataSet.setMatrix(measurement.getModelData().getResponseMatrix());
                    }
                };
                break;

            case MONITOR_DIFFERENCE:
                adapter = new SingleDsAdapter<MatrixRowDataSet>(new MatrixRowDataSet(
                        "Difference measurement - model for one Monitor")) {

                    @Override
                    public void changedActiveMonitor(int number, Monitor monitor) {
                        dataSet.setRow(number);
                        dataSet.setName("Monitor: " + monitor.getName());
                    }

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());
                        dataSet.setMatrix(measurement.getCombinedData().getDifferenceMatrix());
                    }
                };
                break;

            case MONITOR_DIFFERENCE_RMS:
                adapter = new SingleDsAdapter<ListDataSet>(new ListDataSet("Monitor Error rms")) {

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setYValues(measurement.getCombinedData().getMonitorDifferenceRms());
                    }

                };
                break;

            case CORRECTOR_DIFFERENCE:
                adapter = new SingleDsAdapter<MatrixColumnDataSet>(new MatrixColumnDataSet(
                        "Difference measurement -model for one Corrector")) {

                    @Override
                    public void changedActiveCorrector(int number, Corrector corrector) {
                        dataSet.setColumn(number);
                        dataSet.setName("Corrector: " + corrector.getName());
                    }

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());
                        dataSet.setMatrix(measurement.getCombinedData().getDifferenceMatrix());
                    }
                };
                break;

            case CORRECTOR_DIFFERENCE_RMS:
                adapter = new SingleDsAdapter<ListDataSet>(new ListDataSet("Corrector Error rms")) {

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setYValues(measurement.getCombinedData().getCorrectorDifferenceRms());
                    }
                };
                break;
            case RELATIVE_MONITOR_DIFFERENCE:
                adapter = new SingleDsAdapter<MatrixRowDataSet>(new MatrixRowDataSet(
                        "Difference measurement - model for one Monitor")) {

                    @Override
                    public void changedActiveMonitor(int number, Monitor monitor) {
                        dataSet.setRow(number);
                        dataSet.setName("Monitor: " + monitor.getName());
                    }

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());
                        dataSet.setMatrix(measurement.getCombinedData().getRelativeDiffMatrix());
                    }

                };
                break;

            case RELATIVE_MONITOR_DIFFERENCE_RMS:
                adapter = new SingleDsAdapter<ListDataSet>(new ListDataSet("Monitor Error rms")) {

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setYValues(measurement.getCombinedData().getMonitorRelativeDiffRms());
                    }

                };
                break;

            case RELATIVE_CORRECTOR_DIFFERENCE:
                adapter = new SingleDsAdapter<MatrixColumnDataSet>(new MatrixColumnDataSet(
                        "Difference measurement - model for one Corrector")) {

                    @Override
                    public void changedActiveCorrector(int number, Corrector corrector) {
                        dataSet.setColumn(number);
                        dataSet.setName("Corrector: " + corrector.getName());
                    }

                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());
                        dataSet.setMatrix(measurement.getCombinedData().getRelativeDiffMatrix());
                    }
                };
                break;

            case RELATIVE_CORRECTOR_DIFFERENCE_RMS:
                adapter = new SingleDsAdapter<ListDataSet>(new ListDataSet("Corrector Error rms")) {
                    @Override
                    public void refresh() {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setYValues(measurement.getCombinedData().getCorrectorRelativeDiffRms());
                    }
                };
                break;
            case MEASURED_RESPONSE_MATRIX:
                adapter = new SingleDsAdapter<MatrixDataSet>(
                        new MatrixDataSet("Measured Response - Matrix", new Matrix(1, 1))) {

                    @Override
                    public void refresh() {
                        dataSet.setMatrix(measurement.getData().getResponseMatrix());
                        dataSet.setRowLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setColumnLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                    }
                };
                break;

            case MODEL_RESPONSE_MATRIX:
                adapter = new SingleDsAdapter<MatrixDataSet>(new MatrixDataSet("Model Response - Matrix", new Matrix(1, 1))) {

                    @Override
                    public void refresh() {
                        dataSet.setRowLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setColumnLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setMatrix(measurement.getModelData().getResponseMatrix());
                    }
                };
                break;

            case ABSOLUTE_DIFFERENCE_MATRIX:
                adapter = new SingleDsAdapter<MatrixDataSet>(new MatrixDataSet("Difference measurement - model", new Matrix(
                        1, 1))) {
                    @Override
                    public void refresh() {
                        dataSet.setRowLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setColumnLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setMatrix(measurement.getCombinedData().getDifferenceMatrix());
                    }
                };
                break;

            case RELATIVE_DIFFERENCE_MATRIX:
                adapter = new SingleDsAdapter<MatrixDataSet>(new MatrixDataSet("Relative difference measurement - model",
                        new Matrix(1, 1))) {

                    @Override
                    public void refresh() {
                        dataSet.setRowLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                        dataSet.setColumnLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                        dataSet.setMatrix(measurement.getCombinedData().getRelativeDiffMatrix());
                    }
                };
                break;

        }
        return adapter;
    }

    private interface DsAdapter extends MachineElementsManagerListener {
        DataSource dataSource();
        void refresh();
    }

    private abstract static class DsAdapterImpl<T extends DataSource> implements DsAdapter, MachineElementsManagerListener {
        protected final T dataSource;

        private DsAdapterImpl(T dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public T dataSource() {
            return this.dataSource;
        }

        @Override
        public final void changedActiveElements() {
            refresh();
        }

        @Override
        public final void changedElements() {
            refresh();
        }

        @Override
        public void refresh() {
            /* override if needed */
        }
    }

    private abstract static class SingleDsAdapter<T extends DataSet> extends DsAdapterImpl<DefaultDataSource> {
        protected final T dataSet;

        private SingleDsAdapter(T dataSet) {
            super(new DefaultDataSource(dataSet));
            this.dataSet = dataSet;
        }
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        JTabbedPane tabbedPane = new JTabbedPane();
        panel.add(tabbedPane, constraints);

        KickResponseOptionsPanel optionsPanel = new KickResponseOptionsPanel(this.measurement);
        getAlohaBeanFactory().configure(optionsPanel);
        tabbedPane.addTab("kick-response options", optionsPanel);

        constraints.gridx++;
        JPanel matrixPanel = new MatrixPanel();
        panel.add(matrixPanel, constraints);
        return panel;
    }

    /**
     * special ds-adapter for HiLow - datasets
     *
     * @author kfuchsbe
     */
    private class HiLowMatrixDsAdapter extends DsAdapterImpl<DefaultDataSource> {

        private final Plane plane;
        private final MatrixRowColDataSet hiDataSet;
        private final MatrixRowColDataSet loDataSet;

        private HiLowMatrixDsAdapter(MatrixRowColDataSet loDataSet, MatrixRowColDataSet hiDataSet) {
            this(loDataSet, hiDataSet, null);
        }

        private HiLowMatrixDsAdapter(MatrixRowColDataSet loDataSet, MatrixRowColDataSet hiDataSet, Plane plane) {
            super(new DefaultDataSource(new DataSet[]{loDataSet, hiDataSet}));
            this.plane = plane;
            this.hiDataSet = hiDataSet;
            this.loDataSet = loDataSet;
        }

        @Override
        public void refresh() {
            dataSource.setAdjusting(true);
            for (MatrixRowColDataSet dataSet : asList(hiDataSet, loDataSet)) {
                dataSet.setMatrix(measurement.getData().getResponseMatrix());
                dataSet.setOffsetMatrix(measurement.getData().getRelativeRmsValues());
                if (MatrixDsType.ROW.equals(dataSet.getType())) {
                    if (this.plane == null) {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames());
                    } else {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveCorrectorNames(plane));
                    }

                    if (this.plane != null) {
                        int hCorrectorsCount = measurement.getMachineElementsManager().getActiveCorrectorsCount(
                                Plane.HORIZONTAL);
                        if (Plane.HORIZONTAL == plane) {
                            dataSet.setLastIndex(hCorrectorsCount);
                        } else if (Plane.VERTICAL == plane) {
                            dataSet.setFirstIndex(hCorrectorsCount);
                        }
                    }
                } else if (MatrixDsType.COLUMN.equals(dataSet.getType())) {
                    if (this.plane == null) {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
                    } else {
                        dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames(plane));
                    }

                    if (this.plane != null) {
                        int hMonitorsCount = measurement.getMachineElementsManager().getActiveMonitorsCount(
                                Plane.HORIZONTAL);
                        if (Plane.HORIZONTAL == plane) {
                            dataSet.setLastIndex(hMonitorsCount);
                        } else if (Plane.VERTICAL == plane) {
                            dataSet.setFirstIndex(hMonitorsCount);
                        }
                    }
                }
                dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());
            }
            dataSource.setAdjusting(false);
        }

        @Override
        public void changedActiveCorrector(int number, Corrector corrector) {
            if (MatrixDsType.COLUMN.equals(hiDataSet.getType())) {
                hiDataSet.setFixedIndex(number);
                hiDataSet.setName("corrector: " + corrector.getName());
                loDataSet.setFixedIndex(number);
                loDataSet.setName("corrector: " + corrector.getName());
            }
        }

        @Override
        public void changedActiveMonitor(int number, Monitor monitor) {
            if (MatrixDsType.ROW.equals(hiDataSet.getType())) {
                hiDataSet.setFixedIndex(number);
                hiDataSet.setName("monitor: " + monitor.getName());
                loDataSet.setFixedIndex(number);
                loDataSet.setName("monitor: " + monitor.getName());
            }
        }

    }

    @Override
    protected void doRefresh() {
        for (DsAdapter adapter : this.dsAdapters.values()) {
            adapter.refresh();
        }
    }

    /**
     * This class is the panel, which displays the matrices in dataViewer - charts
     *
     * @author kfuchsbe
     */
    private class MatrixPanel extends JPanel {
        private static final long serialVersionUID = -5708068839116603627L;

        private JCheckBox chkTranspose = null;

        public MatrixPanel() {
            initComponents();
        }

        /**
         * create all components and add them to the panel
         */
        private final void initComponents() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(400, 400));

            JTabbedPane tabbedPane = new JTabbedPane();
            add(tabbedPane, BorderLayout.CENTER);

            /*
             * The chart for the measured Response-Matrix
             */
            Chart chart;
            chart = createMatrixChart(DS.MEASURED_RESPONSE_MATRIX);
            tabbedPane.addTab("measured", chart);

            /*
             * The chart for the model-Matrix
             */
            chart = createMatrixChart(DS.MODEL_RESPONSE_MATRIX);
            tabbedPane.addTab("model", chart);

            /*
             * Difference - Charts
             */
            chart = createMatrixChart(DS.ABSOLUTE_DIFFERENCE_MATRIX);
            tabbedPane.addTab("diff", chart);

            /*
             * relative - Charts
             */
            chart = createMatrixChart(DS.RELATIVE_DIFFERENCE_MATRIX);
            tabbedPane.addTab("rel diff", chart);

            /*
             * the checkbox for transposing the displays
             */
            chkTranspose = new JCheckBox("Transpose Matrix views");
            chkTranspose.addActionListener(new TransposeCheckboxAdapter());
            add(chkTranspose, BorderLayout.SOUTH);

        }

        /**
         * creates a matrix-chart
         *
         * @param dstype
         * @return
         */
        private Chart createMatrixChart(DS dstype) {
            DataSet dataSet = getDataSet(dstype);
            Chart chart = new Chart();
            if (dataSet instanceof DataSet3D) {

                ContourChartRenderer renderer = new ContourChartRenderer();
                renderer.setDataSet(dataSet);
                renderer.setStyles(new Style[]{new Style(Color.blue), new Style(Color.magenta)});
                chart.addRenderer(renderer);

                DataPickerInteractor dataPicker = new DataPickerInteractor();
                dataPicker.setPointCoordPane(new MatrixCoordinatesPane());
                dataPicker.setCursorCoordPainted(false);
                dataPicker.setCrossPainted(true);
                dataPicker.setDisplayDataSetName(false);

                // Listen on mouse click
                dataPicker.addChartInteractionListener(new MatrixClickListener());

                chart.addInteractor(dataPicker);

                chart.addInteractor(ChartInteractor.ZOOM);
            } else {
                LOGGER.warn("dataSet '" + dataSet.getName()
                        + "' is not an instance of DataSet3D. Cannot create matrix chart.");
            }
            return chart;
        }

        /**
         * this class listens to the transpose-checkbox
         *
         * @author kfuchsbe
         */
        private class TransposeCheckboxAdapter implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (DS dstype : DS.fromType(DsType.DS3D)) {
                    DsAdapter dsAdapter = dsAdapters.get(dstype);
                    if (dsAdapter == null) {
                        continue;
                    }
                    DataSource ds = dsAdapter.dataSource();
                    if (ds == null) {
                        continue;
                    }
                    for (DataSet dataSet : ds.getDataSets()) {
                        if (dataSet instanceof MatrixDataSet) {
                            ((MatrixDataSet) dataSet).setTransposed(chkTranspose.isSelected());
                        }
                    }
                }
            }
        }

        /**
         * this class listens to clicks in the matrix-display and sets the actual corrector- and monitor index.
         *
         * @author kfuchsbe
         */
        private class MatrixClickListener implements ChartInteractionListener {

            public void interactionPerformed(ChartInteractionEvent evt) {
                DisplayPoint point = evt.getDisplayPoint();
                DataSet dataset = point.getDataSet();
                if (dataset instanceof MatrixDataSet) {
                    MatrixDataSet matrixDataSet = (MatrixDataSet) dataset;
                    int column = matrixDataSet.getColumnNumber((int) point.getX(), (int) point.getY());
                    int row = matrixDataSet.getRowNumber((int) point.getX(), (int) point.getY());

                    measurement.getMachineElementsManager().setActiveCorrectorNumber(column);
                    measurement.getMachineElementsManager().setActiveMonitorNumber(row);

                } else {
                    LOGGER.warn("Dataset was not an instance of MatrixDataSet -> could not get Matrix-Column.");
                }
            }
        }

    }

    /**
     * @return the factory to create the charts
     */
    private ChartFactory getChartFactory() {
        return this.chartFactory;
    }

    @Override
    public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
        this.alohaBeanFactory = alohaBeanFactory;
    }

    private AlohaBeanFactory getAlohaBeanFactory() {
        return this.alohaBeanFactory;
    }

    private class CorrectorResponseAdapter extends SingleDsAdapter<MatrixColumnDataSet> {

        private Plane plane = null;
        private MeasModelType measModelType = null;

        private CorrectorResponseAdapter(MatrixColumnDataSet dataSet, MeasModelType measModelType, Plane plane) {
            this(dataSet, measModelType);
            this.plane = plane;
        }

        private CorrectorResponseAdapter(MatrixColumnDataSet dataSet, MeasModelType measModelType) {
            super(dataSet);
            this.measModelType = measModelType;
        }

        @Override
        public void changedActiveCorrector(int number, Corrector corrector) {
            dataSet.setColumn(number);
            dataSet.setName("Corrector: " + corrector.getName());
        }

        @Override
        public void refresh() {
            if (this.plane == null) {
                dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames());
            } else {
                dataSet.setLabels(measurement.getMachineElementsManager().getActiveMonitorNames(plane));
            }
            dataSet.setMatrix(measModelType.getDataMatrix(measurement));
            dataSet.setValidityMatrix(measurement.getData().getValidityMatrix());

            if (this.plane != null) {
                int hMonitorsCount = measurement.getMachineElementsManager().getActiveMonitorsCount(Plane.HORIZONTAL);
                if (Plane.HORIZONTAL == plane) {
                    dataSet.setLastIndex(hMonitorsCount);
                } else if (Plane.VERTICAL == plane) {
                    dataSet.setFirstIndex(hMonitorsCount);
                }
            }
        }
    }

    private enum MeasModelType {
        MEASUREMENT {
            @Override
            protected Matrix getDataMatrix(KickResponseMeasurementImpl meas) {
                return meas.getData().getResponseMatrix();
            }
        }, //
        MODEL {
            @Override
            public Matrix getDataMatrix(KickResponseMeasurementImpl meas) {
                return meas.getModelData().getResponseMatrix();
            }
        };

        protected abstract Matrix getDataMatrix(KickResponseMeasurementImpl meas);
    }

}
