/**
 * 
 */
package cern.accsoft.steering.aloha.display.annotate;

/**
 * this method annotation tells, which plane is represented by the data of the
 * method.
 * 
 * @author kfuchsbe
 * 
 */
public @interface PlotDataPlane {

	/**
	 * @return the plane this 
	 */
	DataPlane value();
}
