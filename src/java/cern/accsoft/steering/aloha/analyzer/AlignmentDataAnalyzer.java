/*
 * $Id: AlignmentDataAnalyzer.java,v 1.4 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ 
 * $Revision: 1.4 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.analyzer;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.bean.aware.ChartFactoryAware;
import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentData;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentValue;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.jmad.gui.mark.MarkerXProvider;
import cern.accsoft.steering.util.gui.dv.ds.AbstractJmadDataSet;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart.ChartRendererRole;
import cern.accsoft.steering.util.gui.dv.ds.DvUtils;
import cern.jdve.data.DataSet;
import cern.jdve.viewer.DVView;

/**
 * This analyzer provides views, which display the loaded alignment-data
 * 
 * @author kfuchsbe
 * 
 */
public class AlignmentDataAnalyzer implements Analyzer, ChartFactoryAware {

	/** the name of this analyzer */
	private final static String ANALYZER_NAME = "Alignment Data";

	/** set the alignment data */
	private AlignmentData alignmentData;

	/** the dataset for the horizontal data */
	private AlignmentDataDataSet alignmentDsH = new AlignmentDataDataSet(
			"measured alignment data (H)", AlignmentDataDataSetType.DELTA_X);

	/** the dataset for the vertical data */
	private AlignmentDataDataSet alignmentDsV = new AlignmentDataDataSet(
			"measured alignment data (V)", AlignmentDataDataSetType.DELTA_Y);

	/** the dataset for the delta in s direction */
	private AlignmentDataDataSet alignmentDsS = new AlignmentDataDataSet(
			"measured alignment data (S)", AlignmentDataDataSetType.DELTA_S);

	/** the dataset for the delta in the tilt */
	private AlignmentDataDataSet alignmentDsTilt = new AlignmentDataDataSet(
			"measured alignment data (tilt)",
			AlignmentDataDataSetType.DELTA_TILT);

	/** the data view */
	private DVView dvView = new DVView(ANALYZER_NAME, DVView.GRID_LAYOUT);

	/** the optics data of the model (needed for marker-positions) */
	private ModelOpticsData modelOpticsData;

	/** the factory, which helps to create the charts */
	private ChartFactory chartFactory;

	/**
	 * init method.
	 */
	public void init() {
		createDataViews();
	}

	/**
	 * creates the DataViews
	 */
	private void createDataViews() {
		addView(alignmentDsH, "S [m]", "alignment error H [m]");
		addView(alignmentDsV, "S [m]", "alignment error V [m]");
		addView(alignmentDsS, "S [m]", "alignment error S [m]");
		addView(alignmentDsTilt, "S [m]", "alignment error Tilt [rad]");
	}

	/**
	 * adds a new view to the dataViews list
	 * 
	 * @param dataSet
	 * @param xLabel
	 * @param yLabel
	 */
	private void addView(DataSet dataSet, String xLabel, String yLabel) {
		if (getChartFactory() == null) {
			return;
		}
		Aloha2DChart chart;
		chart = getChartFactory().createBarChart(dataSet, null, null, xLabel,
				yLabel);
		chart.setRenderer(ChartRendererRole.MEAS_DATA, DvUtils
				.createTwoPointRenderer());
		chart.setMarkerXProvider(new MarkerXProvider() {
			@Override
			public List<Double> getXPositions(String elementName) {
				List<Double> xValues = new ArrayList<Double>();
				if (getModelOpticsData() != null) {
					xValues.add(getModelOpticsData().getSPosition(elementName));
				}
				return xValues;
			}
		});
		dvView.addDataView(DvUtils.createDataView(chart));
	}

	/**
	 * the type of the alignment data set
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private enum AlignmentDataDataSetType {
		DELTA_X, DELTA_Y, DELTA_TILT, DELTA_S;
	}

	/**
	 * this class is a dataset, which provides the values of the original
	 * alignment data loaded into aloha.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private class AlignmentDataDataSet extends AbstractJmadDataSet {

		/** the alignment-data */
		private List<AlignmentValue> alignmentValues = new ArrayList<AlignmentValue>();

		/** exact data to display */
		private AlignmentDataDataSetType type;

		/**
		 * the constructor, which enforces to give a name for the dataset
		 * 
		 * @param name
		 *            the name of the dataset
		 */
		protected AlignmentDataDataSet(String name,
				AlignmentDataDataSetType type) {
			super(name);
			this.type = type;
		}

		@Override
		public int getDataCount() {
			return getAlignmentValues().size();
		}

		@Override
		public double getX(int index) {
			return getAlignmentValues().get(index).getS();
		}

		@Override
		public double getY(int index) {
			AlignmentValue value = getAlignmentValues().get(index);
			if (AlignmentDataDataSetType.DELTA_X.equals(type)) {
				return value.getDeltaX();
			} else if (AlignmentDataDataSetType.DELTA_Y.equals(type)) {
				return value.getDeltaY();
			} else if (AlignmentDataDataSetType.DELTA_S.equals(type)) {
				return value.getDeltaS();
			} else if (AlignmentDataDataSetType.DELTA_TILT.equals(type)) {
				return value.getDeltaTilt();
			}
			return 0;
		}

		@Override
		public String getDataLabel(int index) {
			AlignmentValue value = getAlignmentValues().get(index);
			return value.getElementName() + " (" + value.getType() + ")";
		}

		/**
		 * @param alignmentValues
		 *            the alignmentValues to set
		 */
		public void setAlignmentValues(List<AlignmentValue> alignmentValues) {
			this.alignmentValues = alignmentValues;
			fireFullChange();
		}

		/**
		 * @return the alignmentValues
		 */
		private List<AlignmentValue> getAlignmentValues() {
			return alignmentValues;
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

	@Override
	public void refresh() {
		if (this.alignmentData != null) {
			alignmentDsH.setAlignmentValues(this.alignmentData
					.getAlignmentValues());
			alignmentDsV.setAlignmentValues(this.alignmentData
					.getAlignmentValues());
			alignmentDsS.setAlignmentValues(this.alignmentData
					.getAlignmentValues());
			alignmentDsTilt.setAlignmentValues(this.alignmentData
					.getAlignmentValues());
		}
	}

	public void setAlignmentData(AlignmentData alignmentData) {
		this.alignmentData = alignmentData;
	}

	public AlignmentData getAlignmentData() {
		return alignmentData;
	}

	@Override
	public DVView getDVView() {
		return this.dvView;
	}

	@Override
	public void setChartFactory(ChartFactory chartFactory) {
		this.chartFactory = chartFactory;
	}

	private ChartFactory getChartFactory() {
		return this.chartFactory;
	}
}
