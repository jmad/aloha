/**
 * 
 */
package cern.accsoft.steering.aloha.meas;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.model.ModelDelegate;

/**
 * This is the interface for a class, which keeps track of the loaded
 * measurements and the models they use.
 * 
 * @author kfuchsbe
 * 
 */
public interface MeasurementManager {

	/**
	 * @return all loaded measurements
	 */
	List<ModelAwareMeasurement> getMeasurements();

	/**
	 * @param type
	 *            the type of the measurements to retrieve
	 * @return all the measurements of a given type
	 */
	List<ModelAwareMeasurement> getMeasurements(MeasurementType type);

	/**
	 * @param measurement
	 *            the measurement to add to the manager
	 */
	void addMeasurement(ModelAwareMeasurement measurement);

	/**
	 * @param index
	 *            the measurement to remove from the manager
	 */
	void removeMeasurement(int index);

	/**
	 * sets the active measurement to the given one
	 * 
	 * @param index
	 *            the index of measurement to set as active
	 */
	void setActiveMeasurement(int index);

	/**
	 * sets the given measurement as the active one. The settings are then
	 * changed according to the offsets in this measurement. This should only be
	 * done through this mechanism, to ensure, that all old settings are
	 * removed.
	 * 
	 * @param measurement
	 */
	void setActiveMeasurement(ModelAwareMeasurement measurement);

	/**
	 * @return the actually active measurement
	 */
	Measurement getActiveMeasurement();

	/**
	 * add a listener
	 * 
	 * @param listener
	 *            the listener to add
	 */
	void addListener(MeasurementManagerListener listener);

	/**
	 * removes a listener
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	void removeListener(MeasurementManagerListener listener);

	/**
	 * This class is a helper class which holds one model instance and
	 * references to measurements which use the one instance.
	 * 
	 * This is intended to be a temporal description and shall not be kept as
	 * reference in order to avoid have unnecessary references to models and
	 * measurements.
	 * 
	 * This class is mainly intended to be shown in a combo-box for selecting an
	 * existing instance.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	class ModelDelegateInstance {

		/** The used model */
		private ModelDelegate modelDelegate;

		/** all the measurements which currently use this model. */
		private List<String> measurementNames = new ArrayList<String>();

		/**
		 * the constructor, which needs a model to which this instance belongs
		 * to.
		 * 
		 * @param modelDelegate
		 */
		public ModelDelegateInstance(ModelDelegate modelDelegate) {
			this.modelDelegate = modelDelegate;
		}

		/**
		 * adds a measurement.
		 * 
		 * @param measurement
		 */
		public void addMeasurementName(Measurement measurement) {
			this.measurementNames.add(measurement.getName());
		}

		@Override
		public String toString() {
			String name = this.getModelDelegate().getJMadModel().getName()
					+ " (";
			int count = 0;
			for (String measName : this.measurementNames) {
				if (count > 0) {
					name += ", ";
				}
				name += measName;
				count++;
			}
			name += ")";
			return name;
		}

		public ModelDelegate getModelDelegate() {
			return modelDelegate;
		}

	}
}
