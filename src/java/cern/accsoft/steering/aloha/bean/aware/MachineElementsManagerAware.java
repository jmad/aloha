/**
 * 
 */
package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;

/**
 * If a class implements this interface then the {@link AlohaBeanFactory}
 * knows that it has to set the {@link MachineElementsManager} to the class.
 * 
 * @author kfuchsbe
 * 
 */
public interface MachineElementsManagerAware extends BeanAware {

	/**
	 * by the use of this method the {@link MachineElementsManager} is injected
	 * if the class is configured by the {@link BeanUserConfigurator}
	 */
	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager);
}
