/**
 * 
 */
package cern.accsoft.steering.aloha.meas;

import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.ModelDelegate;

/**
 * This class provides the common methods for a measurement
 * 
 * @author kfuchsbe
 * 
 */
public abstract class AbstractModelAwareMeasurement implements
		ModelAwareMeasurement, MachineElementsManagerAware {

	/** the name of the measurement */
	private String name = "";
	/**
	 * the model-delegate, which must be used for calculations concerning this
	 * measurement.
	 */
	protected ModelDelegate modelDelegate;
	/**
	 * the {@link MachineElementsManager} which must be used for this
	 * measurement
	 */
	private MachineElementsManager machineElementsManager;

	/**
	 * the constructor, which enforces to give a name and a model delegate
	 * 
	 * @param name
	 */
	public AbstractModelAwareMeasurement(String name,
			ModelDelegate modelDelegate) {
		super();
		this.name = name;
		this.modelDelegate = modelDelegate;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public final String toString() {
		return getName();
	}

	@Override
	public ModelDelegate getModelDelegate() {
		return modelDelegate;
	}

	@Override
	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
	}

	@Override
	public MachineElementsManager getMachineElementsManager() {
		return machineElementsManager;
	}

}
