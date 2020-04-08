/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.meas.Measurement;

/**
 * This is the interface of a class that creates Displaysets from a measurement.
 * 
 * @author kfuchsbe
 * 
 */
public interface DisplaySetFactory extends AlohaPlugin {

	/**
	 * this method must return a new instance of a {@link DisplaySet} which
	 * correspond to the measurement. (If the factory can handle this kind of
	 * measurement)
	 * 
	 * If the Factory cannot create a {@link DisplaySet} from the given
	 * measurement, then this method has to return null.
	 * 
	 * @param measurement
	 *            the measurement for which to create a DisplaySet
	 * @return the new displaysets if the measurement is handled, null
	 *         otherwise.
	 */
	public DisplaySet createDisplaySet(Measurement measurement);
}
