package cern.accsoft.steering.aloha.gui.dv;

import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;

public interface ChartFactory {

	/**
	 * creates a default chart for aloha, sets the given DataSets and sets the
	 * x/y titles.
	 * 
	 * @param measuredDataSet
	 *            the dataSet for the measurement-renderer
	 * @param modelDataSet
	 *            the dataSet for the model-renderer
	 * @param measuredErrorDataSource
	 *            the dataSet for the measurement-error
	 * @param xtitle
	 *            the tite of the x-axis
	 * @param ytitle
	 *            the title of the y-axis
	 * @return the chart
	 */
	public abstract Aloha2DChart createBarChart(DataSet measuredDataSet,
			DataSet modelDataSet, DataSource measuredErrorDataSource,
			String xtitle, String ytitle);

	public abstract Aloha2DChart createBarChart(DataSet measuredDataSet,
			DataSet modelDataSet, String xtitle, String ytitle);

}