/**
 * 
 */
package cern.accsoft.steering.aloha.bean;

import cern.accsoft.steering.aloha.bean.aware.BeanAware;

/**
 * This is the interface of a class to create a class and configure it with all
 * the common used beans it needs.
 * 
 * @author kfuchsbe
 * 
 */
public interface AlohaBeanFactory {

	/**
	 * creates an instance of the given class and configures it correctly with
	 * all the aloha-beans as given by its interfaces derived from
	 * {@link BeanAware}
	 * 
	 * @param <T>
	 * @param clazz
	 *            the class for which to create an instance
	 * @return a fully configured instancs
	 */
	public <T> T create(Class<? extends T> clazz);

	/**
	 * configures the given instance. This can be used, e.g. the bean has no
	 * default constructor. So it can be created with the new operator and then
	 * configured afterwards.
	 * 
	 * @param <T>
	 * @param instance
	 *            the bean to configure
	 */
	public <T> void configure(T beanUser);

}
