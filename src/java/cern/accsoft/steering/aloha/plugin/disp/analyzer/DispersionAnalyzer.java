/*
 * $Id: DispersionAnalyzer.java,v 1.4 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ $Revision: 1.4 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.disp.analyzer;

import cern.accsoft.steering.aloha.analyzer.AbstractAnalyzer;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement;
import cern.accsoft.steering.aloha.plugin.disp.meas.data.DispersionData;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.accsoft.steering.util.gui.dv.ds.ErrorDataSetAdapter;
import cern.accsoft.steering.util.gui.dv.ds.ErrorDataSetAdapter.ErrorAddMode;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.jdve.Chart;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.data.DefaultDataSource;
import cern.jdve.viewer.DVView;

/**
 * This class implements an analyzer, which compares the measured and the model dispersion.
 * 
 * @author kfuchsbe
 */
public class DispersionAnalyzer extends AbstractAnalyzer<DispersionMeasurement> {

    /** the name of this analyzer */
    private final static String ANALYZER_NAME = "Dispersion (s)";

    /*
     * the data-sets for the dispersion-data
     */
    private ListDataSet dispersionMeasH = new ListDataSet("Dispersion measured (H)");

    private ListDataSet dispersionMeasV = new ListDataSet("Dispersion measured (V)");

    private ListDataSet dispersionModelH = new ListDataSet("Dispersion model (H)");
    private ListDataSet dispersionModelV = new ListDataSet("Dispersion model (V)");

    /** the dataViews for this analyzer */
    private DVView dvView = new DVView(ANALYZER_NAME, DVView.VERTICAL_LAYOUT);

    /**
     * the init method, which just creates the DataViews
     */
    @InitMethod
    public void init() {
        createDataViews();
    }

    /**
     * refreshes the dataviews
     */
    @Override
    public void refresh() {
        configureDataSets();
    }

    /**
     * creates the dataviews
     */
    private void createDataViews() {
        if (getChartFactory() == null) {
            return;
        }
        Chart chart;
        DataSource errorDataSource = new DefaultDataSource(new DataSet[] {
                new ErrorDataSetAdapter(dispersionMeasH, ErrorAddMode.PLUS),
                new ErrorDataSetAdapter(dispersionMeasH, ErrorAddMode.MINUS) });
        chart = getChartFactory().createBarChart(dispersionMeasH, dispersionModelH, errorDataSource, "s [m]",
                "dispersion H [m]");
        this.dvView.addDataView(DvUtils.createDataView(chart));

        errorDataSource = new DefaultDataSource(new DataSet[] {
                new ErrorDataSetAdapter(dispersionMeasV, ErrorAddMode.PLUS),
                new ErrorDataSetAdapter(dispersionMeasV, ErrorAddMode.MINUS) });

        chart = getChartFactory().createBarChart(dispersionMeasV, dispersionModelV, errorDataSource, "s [m]",
                "dispersion V [m]");
        this.dvView.addDataView(DvUtils.createDataView(chart));
    }

    /**
     * sets the new values to the DataSets
     */
    private void configureDataSets() {
        DispersionData dispersionData = getDispersionData();
        if (dispersionData == null) {
            return;
        }

        configureMeasDataSet(dispersionMeasH, Plane.HORIZONTAL);
        configureMeasDataSet(dispersionMeasV, Plane.VERTICAL);

        dispersionModelH.setValues(getModelOpticsData().getAllSPositions(), getModelOpticsData().getAllDispersions(
                Plane.HORIZONTAL));
        dispersionModelH.setLabels(getModelOpticsData().getAllNames());

        dispersionModelV.setValues(getModelOpticsData().getAllSPositions(), getModelOpticsData().getAllDispersions(
                Plane.VERTICAL));
        dispersionModelV.setLabels(getModelOpticsData().getAllNames());

    }

    /**
     * configures a measured dataSet with the actual values
     * 
     * @param dataSet the dataSet to configure
     * @param plane the plane, which datas to set.
     */
    private void configureMeasDataSet(ListDataSet dataSet, Plane plane) {
        DispersionData dispersionData = getDispersionData();
        if (dispersionData == null) {
            return;
        }

        dataSet.setValues(getModelOpticsData().getMonitorSPositions(plane), dispersionData.getValues(plane),
                dispersionData.getRms(plane), dispersionData.getValidity(plane));
        dataSet.setLabels(getMachineElementsManager().getActiveMonitorNames(plane));
    }

    /**
     * @return the actual dispersion-data
     */
    private DispersionData getDispersionData() {
        return getMeasurement().getData();
    }

    //
    // methods of AbstractAnalyzer
    //

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
    public DVView getDVView() {
        return this.dvView;
    }
}
