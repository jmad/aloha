// @formatter:off
 /*******************************************************************************
 *
 * This file is part of JMad.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

package cern.accsoft.steering.util.meas.yasp.browse;

import static cern.accsoft.steering.util.gui.dv.ds.ColorConstants.COLOR_MEAS_DATA_TRAJECTORY;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.gui.dv.ds.AbstractJmadDataSet;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart.ChartRendererRole;
import cern.accsoft.steering.util.gui.dv.ds.ValidityDataSet;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.yasp.CorrectorValue;
import cern.accsoft.steering.util.meas.data.yasp.MonitorValue;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.ReadingDataReader;
import cern.accsoft.steering.util.meas.read.filter.impl.NameListReadSelectionFilter;
import cern.accsoft.steering.util.meas.read.yasp.YaspFileReader;
import cern.jdve.Style;
import cern.jdve.data.DataSet;
import cern.jdve.data.DefaultDataSource;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;
import cern.jdve.viewer.DataViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The panel to display the yasp-traj data
 *
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class YaspPreviewPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(YaspPreviewPanel.class);

    private ReadingDataReader reader = new YaspFileReader();

    /**
     * maps to store the datasets
     */
    private Map<String, MonitorValueDataSet> monitorValueDataSets = new HashMap<>();
    private Map<String, CorrectorValuesDataSet> correctorValueDataSets = new HashMap<>();

    /**
     * the default constructor, which initializes all the components
     */
    public YaspPreviewPanel() {
        initComponenets();
    }

    private void initComponenets() {

        for (Plane plane : Plane.values()) {
            for (BeamNumber beamNumber : BeamNumber.values()) {
                this.monitorValueDataSets.put(createKey(plane, beamNumber), new MonitorValueDataSet(
                        beamNumber.toString() + ", " + plane.toString() + " - monitor readings"));
                this.correctorValueDataSets.put(createKey(plane, beamNumber), new CorrectorValuesDataSet(
                        beamNumber.toString() + ", " + plane.toString() + " - corrector kicks"));
            }
        }

        setLayout(new BorderLayout());

        /*
         * The DataViewer
         */
        DataViewer dataViewer = new DataViewer();
        dataViewer.setPreferredSize(new Dimension(500, 500));
        add(dataViewer, BorderLayout.CENTER);

        Style trajectoryStyle = new Style(COLOR_MEAS_DATA_TRAJECTORY, COLOR_MEAS_DATA_TRAJECTORY);
        for (BeamNumber beam : BeamNumber.values()) {
            DVView view = new DVView(beam.toString());
            view.setLayout(DVView.VERTICAL_LAYOUT);
            dataViewer.addView(view);

            for (Plane plane : Plane.values()) {
                Aloha2DChart chart = new Aloha2DChart();
                chart.getRenderer(Aloha2DChart.ChartRendererRole.MEAS_DATA).setStyle(0, trajectoryStyle);
                DataSet dataSet = monitorValueDataSets.get(createKey(plane, beam));
                chart.setRenderDataSource(ChartRendererRole.MEAS_DATA, new DefaultDataSource(dataSet));
                chart.getArea().setBackground(YaspColors.BEAM_COLOR.get(beam));
                view.addDataView(new DataView(chart));

                chart = new Aloha2DChart();
                chart.getRenderer(Aloha2DChart.ChartRendererRole.MEAS_DATA).setStyle(0, trajectoryStyle);
                dataSet = correctorValueDataSets.get(createKey(plane, beam));
                chart.setRenderDataSource(ChartRendererRole.MEAS_DATA, new DefaultDataSource(dataSet));
                chart.getArea().setBackground(YaspColors.BEAM_COLOR.get(beam));
                view.addDataView(new DataView(chart));
            }
        }
    }

    private String createKey(Plane plane, BeamNumber beamNumber) {
        return plane.toString() + "-" + beamNumber.toString();
    }

    public void setYaspFile(File file) {
        try {
            for (BeamNumber beamNumber : BeamNumber.values()) {
                ReadingData readingData = reader.read(file, new NameListReadSelectionFilter(beamNumber));
                for (Plane plane : Plane.values()) {
                    String key = createKey(plane, beamNumber);
                    monitorValueDataSets.get(key).setMonitorValues(readingData.getMonitorValues());
                    correctorValueDataSets.get(key).setCorrectorValues(readingData.getCorrectorValues());
                }
            }
        } catch (ReaderException e) {
            LOGGER.warn("Error while reading file '" + file.getAbsolutePath() + "'. Maybe it is not a yasp file?", e);
        }
    }

    /**
     * the dataset for monitor-values
     *
     * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
     */
    private class MonitorValueDataSet extends AbstractJmadDataSet implements ValidityDataSet {

        /**
         * the data to display
         */
        private List<MonitorValue> monitorValues = new ArrayList<>();

        protected MonitorValueDataSet(String name) {
            super(name);
        }

        @Override
        public int getDataCount() {
            return monitorValues.size();
        }

        @Override
        public double getY(int index) {
            return monitorValues.get(index).getBeamPosition();
        }

        @Override
        public boolean getValidity(int index) {
            return monitorValues.get(index).isOk();
        }

        @Override
        public boolean hasValidityInformation() {
            return true;
        }

        @Override
        public String getDataLabel(int index) {
            return this.monitorValues.get(index).getName();
        }

        private void setMonitorValues(Collection<MonitorValue> monitorValues) {
            this.monitorValues = new ArrayList<>(monitorValues);
            refresh();
        }
    }

    /**
     * the dataset for corrector-values
     *
     * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
     */
    private class CorrectorValuesDataSet extends AbstractJmadDataSet {

        /**
         * the data to display
         */
        private List<CorrectorValue> correctorValues = new ArrayList<>();

        protected CorrectorValuesDataSet(String name) {
            super(name);
        }

        @Override
        public int getDataCount() {
            return this.correctorValues.size();
        }

        @Override
        public double getY(int index) {
            return this.correctorValues.get(index).kick;
        }

        private void setCorrectorValues(Collection<CorrectorValue> correctorValues) {
            this.correctorValues = new ArrayList<>(correctorValues);
            refresh();
        }

        @Override
        public String getDataLabel(int index) {
            return this.correctorValues.get(index).getName();
        }

    }

}
