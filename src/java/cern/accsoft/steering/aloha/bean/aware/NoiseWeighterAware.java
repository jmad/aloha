/**
 * 
 */
package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;

/**
 * The interface of a class that can be configured by the
 * {@link AlohaBeanFactory} to use the common {@link NoiseWeighter} instance
 * 
 * @author kfuchsbe
 * 
 */
public interface NoiseWeighterAware extends BeanAware {

	/**
	 * this method is used by the {@link BeanUserConfigurator} to inject the
	 * {@link NoiseWeighter} instance
	 * 
	 * @param noiseWeighter
	 *            the {@link NoiseWeighter} to be used by the class
	 */
	public void setNoiseWeighter(NoiseWeighter noiseWeighter);
}
