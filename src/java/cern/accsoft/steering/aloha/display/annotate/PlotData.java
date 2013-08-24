/**
 * 
 */
package cern.accsoft.steering.aloha.display.annotate;

/**
 * this is an annotation that can be added to methods that return data that can
 * be plotted.
 * 
 * 
 * @author kfuchsbe
 * 
 */
public @interface PlotData {

	/**
	 * @return the name of the data. This should describe the data
	 */
	String name();

	/**
	 * @return the length of the data. This can be used e.g. to distinguish if
	 *         the data can be plotted in a certain plot.
	 */
	DataLength dataLength();
	
}
