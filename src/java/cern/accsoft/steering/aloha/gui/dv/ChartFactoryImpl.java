/*
 * $Id: ChartFactory.java,v 1.1 2009-02-25 18:48:42 kfuchsbe Exp $
 *
 * $Date: 2009-02-25 18:48:42 $
 * $Revision: 1.1 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.dv;

import cern.accsoft.steering.jmad.gui.mark.MarkedElementsManager;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart.ChartRendererRole;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.jdve.ChartRenderer;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSetError;
import cern.jdve.data.DataSource;
import cern.jdve.renderer.ErrorDataSetRenderer;

/**
 * The class, which creates the charts for Aloha.
 *
 * @author kfuchsbe
 */
@SuppressWarnings("deprecation")
public abstract class ChartFactoryImpl implements ChartFactory {

    /*
     * This is the old variant: Errors handled through two different dataSets in
     * datasource and a HiLow - renderer
     */
    public final Aloha2DChart createBarChart(DataSet measuredDataSet, DataSet modelDataSet,
            DataSource measuredErrorDataSource, String xtitle, String ytitle) {
        Aloha2DChart chart = createChart(xtitle, ytitle);

        if (measuredDataSet != null) {
            chart.setRendererDataSet(ChartRendererRole.MEAS_DATA, measuredDataSet);
        }
        if (modelDataSet != null) {
            chart.setRendererDataSet(ChartRendererRole.MODEL_DATA, modelDataSet);
        }
        if (measuredErrorDataSource != null) {
            chart.setRenderDataSource(ChartRendererRole.MEAS_ERROR, measuredErrorDataSource);
        }

        chart.initMarkers();
        return chart;
    }

    public final Aloha2DChart createBarChart(DataSet measuredDataSet, DataSet modelDataSet, String xtitle,
            String ytitle) {
        Aloha2DChart chart = createChart(xtitle, ytitle);

        if (measuredDataSet != null) {
            chart.setRendererDataSet(ChartRendererRole.MEAS_DATA, measuredDataSet);
        }
        if (measuredDataSet instanceof DataSetError) {
            /*
             * first change the dataset, the error renderer cannot cope with an
             * empty dataset.
             */
            chart.setRenderer(ChartRendererRole.MEAS_ERROR, createErrorRenderer());
            chart.setRendererDataSet(ChartRendererRole.MEAS_ERROR, measuredDataSet);
        }
        if (modelDataSet != null) {
            chart.setRendererDataSet(ChartRendererRole.MODEL_DATA, modelDataSet);
        }

        chart.initMarkers();
        return chart;
    }

    private Aloha2DChart createChart(String xtitle, String ytitle) {
        Aloha2DChart chart = createAloha2DChart();
        chart.setXScaleTitle(xtitle);
        chart.setYScaleTitle(ytitle);
        return chart;
    }

    private ChartRenderer createErrorRenderer() {
        ErrorDataSetRenderer errorRenderer = new ErrorDataSetRenderer();

        // three different fill styles
        // errorRenderer.setStyle(0, new Style(Color.blue, new Color(255, 0, 0,
        // 128))); // transparent
        // errorRenderer.setStyle(0, new Style(Color.blue, new Color(255, 0,
        // 0))); // opaque
        // Style hatching = new Style(Color.blue, new Color(255, 0, 0),
        // Style.FillPattern.HATCH0);
        // hatching.setHatchingSpacing(5);
        // hatching.setHatchingAngle(-30);
        // float dash[] = { 3.0f };
        // Stroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
        // BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        // hatching.setHatchingStroke(stroke);
        // errorRenderer.setStyle(0, hatching);

        // sets whether to draw a polygon line through data points
        errorRenderer.setDrawPolyLine(false);

        // sets whether to draw a bar at the location of the data point
        // N.B. the bar's colour is defined by the stroke not the fill colour
        errorRenderer.setDrawBars(false);

        // use the following lines to draw fixed width bars
        // errorRenderer.setDynamicBarWidth(false);
        // errorRenderer.setBarWidth(10); // in pixel
        // in case of multiple bar data sets, sets whether to shift subsequent
        // sets
        // errorRenderer.setShiftBar(false);
        // errorRenderer.setShiftBarOffset(20);

        // drawing markers for each points is really slow (default: off)
        // errorRenderer.drawMarker(true);

        // errorRenderer.setErrorType(ErrorDataSetRenderer.ErrorStyle.ESTYLE_NONE);
        // errorRenderer.setErrorType(ErrorDataSetRenderer.ErrorStyle.ESTYLE_SURFACE);
        // errorRenderer.setErrorType(ErrorDataSetRenderer.ErrorStyle.ESTYLE_BAR);
        errorRenderer.setErrorType(ErrorDataSetRenderer.ErrorStyle.ESTYLE_BAR);

        // the following command sets whether points, otherwise drawn on the
        // same pixel,
        // are merged and represented via their average location and maximum
        // point error
        // spread
        // give it a try - especially with data sets with more than e.g. 10000
        // points
        errorRenderer.setPointReduction(false);
        errorRenderer.setDashSize(5); // also controls the point reduction
        // algorithm
        errorRenderer.setRenderingHint(DvUtils.VALIDITY_RENDERING_HINT);
        return errorRenderer;
    }

    /**
     * this method creates a default 2D-chart for aloha.
     * <p>
     * This method is injected by spring to deliver a well configured chart,
     * which listens correctly to the {@link MarkedElementsManager}.
     *
     * @return a configured chart for aloha
     */
    public abstract Aloha2DChart createAloha2DChart();
}
