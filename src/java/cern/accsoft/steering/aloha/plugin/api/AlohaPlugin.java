/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.aware.BeanAware;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.read.Reader;

/**
 * This is the interface of an analysis package for aloha. All packages have to
 * implement this interface in order to provide {@link Reader}s,
 * {@link SensitivityMatrixContributor}s or {@link DisplaySetFactory}s to aloha.
 * <p>
 * All classes found in the classpath that implement this interface are
 * instantiated via the {@link AlohaBeanFactory}. So by implementing one or more
 * of the interfaces derived by {@link BeanAware} they can decide which beans
 * they want to get injected. If a package needs to instantiate other objects
 * then it also should do this through the create methode of
 * {@link AlohaBeanFactory} in order to ensure, that the common beans are
 * injected to the instances.
 * <p>
 * Classes that implement this interface are expected to implement one or more
 * of the interfaces in the package provider, which then defines what
 * plugin-components they actually provide.
 * 
 * @author kfuchsbe
 * 
 */
public interface AlohaPlugin {

	/**
	 * @return the name of the plugin
	 */
	public String getName();

}
