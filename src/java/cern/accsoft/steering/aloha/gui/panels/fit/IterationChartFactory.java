/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels.fit;

import cern.accsoft.steering.aloha.calc.Calculator;
import cern.accsoft.steering.aloha.calc.CalculatorListener;
import cern.accsoft.steering.aloha.calc.iteration.IterationData;
import cern.accsoft.steering.aloha.calc.iteration.IterationManager;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.jdve.Chart;
import cern.jdve.ChartRenderer;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.data.DefaultDataSet;
import cern.jdve.data.DefaultDataSource;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;

import java.util.ArrayList;
import java.util.List;

/**
 * provides a helper method to create charts for the iteration values.
 * 
 * @author kaifox
 */
public class IterationChartFactory {

    /** The class who keeps track about all the performed iterations */
    private IterationManager iterationManager;

    /** The manager, which knows about all the contributors */
    private SensitivityMatrixManager sensitivityMatrixManager;

    /** the dataset for the rms of all contributors */
    private ListDataSet dataSetAllRms = new ListDataSet("RMS all contributors.");

    /** the dataset for the rms of the active contributors */
    private ListDataSet dataSetActiveRms = new ListDataSet("RMS active contributors.");

    private ChartRenderer contributorsRenderer;

    private List<DVView> dvViews = new ArrayList<DVView>();

    /**
     * The listener to be attached to the calculator
     */
    private CalculatorListener calculatorListener = new CalculatorListener() {

        @Override
        public void changedVariationParameters(Calculator calculator) {
            refreshCharts();
        }

        @Override
        public void changedCalculatedValues(Calculator calculator) {
            refreshCharts();
        }
    };

    public void init() {
        this.dvViews = createDvViews();
        refreshCharts();
    }

    /**
     * @return configured dvviews to display the iteration data
     */
    private List<DVView> createDvViews() {
        List<DVView> dvViews = new ArrayList<DVView>();

        DVView dvResiduals = new DVView("residuals");
        dvViews.add(dvResiduals);
        dvResiduals.setLayout(DVView.VERTICAL_LAYOUT);

        Chart chart = createChart("rms", new DefaultDataSource(new DataSet[] { dataSetAllRms, dataSetActiveRms }));
        dvResiduals.addDataView(new DataView(chart));

        /*
         * chart for the rms-values
         */
        chart = createChart("rms", new DefaultDataSource(new DataSet[] { new DefaultDataSet("RMS per contributor",
                new double[] {}) }));
        contributorsRenderer = chart.getRenderer(0);
        dvResiduals.addDataView(new DataView(chart));

        /*
         * chart for the rms values of the individual contributors
         */

        return dvViews;
    }

    private Chart createChart(String yTitle, DataSource dataSource) {
        Chart chart = new Chart();
        chart.setXScaleTitle("iteration number");
        chart.setYScaleTitle(yTitle);
        DvUtils.configureDefaultInteractors(chart);
        ChartRenderer renderer = DvUtils.createMarkerPolylineRenderer();
        renderer.setDataSource(dataSource);
        chart.addRenderer(renderer);
        chart.setLegendVisible(true);
        return chart;
    }

    public void setCalculator(Calculator calculator) {
        calculator.addListener(this.calculatorListener);
    }

    private void refreshCharts() {
        /*
         * for the general ones, we only have to update the datasets
         */
        XYValues xyValues = getDiffRmsValues(RmsType.ALL);
        this.dataSetAllRms.setValues(xyValues.xValues, xyValues.yValues);

        xyValues = getDiffRmsValues(RmsType.ACTIVE);
        this.dataSetActiveRms.setValues(xyValues.xValues, xyValues.yValues);

        /*
         * The amount of contributors might have changed, so we have to re-add the datasets
         */
        DefaultDataSource dataSource = new DefaultDataSource();
        for (SensitivityMatrixContributor contributor : getSensitivityMatrixManager().getAllContributors()) {
            ListDataSet dataSet = new ListDataSet(contributor.getName());
            XYValues xy = getDiffRmsValues(contributor);

            dataSet.setValues(xy.xValues, xy.yValues);
            dataSource.addDataSet(dataSet);
        }
        contributorsRenderer.setDataSource(dataSource);

    }

    private XYValues getDiffRmsValues(SensitivityMatrixContributor contributor) {
        XYValues xyValues = new XYValues();
        for (IterationData iteration : getIterationManager().getIterations()) {
            Double yValue = iteration.getDifferenceRms(contributor);
            /*
             * this might be null!
             */
            if (yValue != null) {
                xyValues.xValues.add(new Double(iteration.getIterationNumber()));
                xyValues.yValues.add(yValue);
            }
        }
        return xyValues;

    }

    private XYValues getDiffRmsValues(RmsType type) {
        XYValues xyValues = new XYValues();
        for (IterationData iteration : getIterationManager().getIterations()) {
            xyValues.xValues.add(new Double(iteration.getIterationNumber()));
            if (RmsType.ALL == type) {
                xyValues.yValues.add(iteration.getAllDifferenceRms());
            } else if (RmsType.ACTIVE == type) {
                xyValues.yValues.add(iteration.getActiveDifferenceRms());
            } else {
                xyValues.yValues.add(0.0);
            }
        }
        return xyValues;
    }

    private enum RmsType {
        ALL, ACTIVE;
    }

    public void setIterationManager(IterationManager iterationManager) {
        this.iterationManager = iterationManager;
    }

    private IterationManager getIterationManager() {
        return iterationManager;
    }

    public void setSensitivityMatrixManager(SensitivityMatrixManager sensitivityMatrixManager) {
        this.sensitivityMatrixManager = sensitivityMatrixManager;
    }

    private SensitivityMatrixManager getSensitivityMatrixManager() {
        return sensitivityMatrixManager;
    }

    public List<DVView> getDvViews() {
        return dvViews;
    }

    private class XYValues {
        List<Double> xValues = new ArrayList<Double>();
        List<Double> yValues = new ArrayList<Double>();
    }
}
