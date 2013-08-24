/**
 * 
 */
package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.gui.dv.ChartFactory;

/**
 * interface to be implemented if the bean needs the chartFactory
 * 
 * @author kfuchsbe
 * 
 */
public interface ChartFactoryAware extends BeanAware {

	/**
	 * set the chart factory
	 * 
	 * @param chartFactory
	 *            the {@link ChartFactory} to set
	 */
	public void setChartFactory(ChartFactory chartFactory);
}
