/**
 * 
 */
package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;

/**
 * This is the interface of a class that uses a {@link AlohaBeanFactory}
 * itself.
 * 
 * @author kfuchsbe
 * 
 */
public interface AlohaBeanFactoryAware extends BeanAware {

	/**
	 * this method is used by the {@link AlohaBeanFactory} to inject itself.
	 * 
	 * @param alohaBeanFactory
	 */
	public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory);
}
