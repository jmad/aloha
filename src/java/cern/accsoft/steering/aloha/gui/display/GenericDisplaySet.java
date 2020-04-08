/**
 * 
 */
package cern.accsoft.steering.aloha.gui.display;

import cern.accsoft.steering.aloha.meas.Measurement;

/**
 * @author kfuchsbe
 * 
 */
public abstract class GenericDisplaySet<T extends Measurement> extends
		AbstractDisplaySet {

	/** the measurement to display */
	private T measurement;

	public GenericDisplaySet(T measurement) {
		this.measurement = measurement;
	}

	protected T getMeasurement() {
		return this.measurement;
	}

}
