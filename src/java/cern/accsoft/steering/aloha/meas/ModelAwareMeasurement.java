/**
 * 
 */
package cern.accsoft.steering.aloha.meas;

import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.ModelDelegate;

/**
 * This interface represents a measurement, which can be added to aloha. All the
 * available measurements will be collected by the {@link MeasurementManager}
 * and are available for displaying and fitting.
 * 
 * @author kfuchsbe
 * 
 */
public interface ModelAwareMeasurement extends Measurement {

	/**
	 * @return the model delegate which shall be used for calculations of this
	 *         measurement.
	 */
	ModelDelegate getModelDelegate();

	/**
	 * this must return the {@link MachineElementsManager} to use for this
	 * measurement.
	 * 
	 * @return the {@link MachineElementsManager}
	 */
	MachineElementsManager getMachineElementsManager();

}
