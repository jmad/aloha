package cern.accsoft.steering.aloha.meas;

import cern.accsoft.steering.aloha.model.ModelDelegate;

/**
 * This class is the generic implementation of a measurement.
 * 
 * @author kfuchsbe
 * 
 */
public abstract class GenericMeasurementImpl<T> extends AbstractModelAwareMeasurement
		implements GenericMeasurement<T> {

	/** the measured data */
	private T data;

	/**
	 * the constructor, which enforces a name, model Delegate and data
	 * 
	 * @param name
	 *            the name for the measurement
	 * @param modelDelegate
	 *            the model delegate to use for this model.
	 * @param data
	 *            the data for the measurent
	 */
	public GenericMeasurementImpl(String name, ModelDelegate modelDelegate,
			T data) {
		super(name, modelDelegate);
		this.data = data;
	}

	@Override
	public T getData() {
		return this.data;
	}

}
