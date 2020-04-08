/**
 * 
 */
package cern.accsoft.steering.aloha.gui.display;

import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;

/**
 * This is the base implementation of all {@link DisplaySetFactory}s.
 * 
 * @author kfuchsbe
 * 
 */
public abstract class AbstractDisplaySetFactory implements DisplaySetFactory {

	/**
	 * the chart-factory to use for creating charts.
	 */
	private ChartFactory chartFactory;

	public void setChartFactory(ChartFactory chartFactory) {
		this.chartFactory = chartFactory;
	}

	protected ChartFactory getChartFactory() {
		return chartFactory;
	}

}
