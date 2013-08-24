/**
 * 
 */
package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.app.HelperDataManager;
import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;

/**
 * The interface of a class that can be configured by the
 * {@link AlohaBeanFactory} and uses the {@link HelperDataManager}
 * 
 * @author kfuchsbe
 * 
 */
public interface HelperDataManagerAware extends BeanAware {

	/**
	 * through this method the {@link HelperDataManager} will be injected if the
	 * class is configured by the {@link BeanUserConfigurator}
	 * 
	 * @param helperDataManager
	 *            the {@link HelperDataManager} to set
	 */
	public void setHelperDataManager(HelperDataManager helperDataManager);
}
