/**
 * 
 */
package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.meas.MeasurementManager;

/**
 * This interface can be implemented by a class to get the
 * {@link MeasurementManager} injected by the {@link AlohaBeanFactory}
 * 
 * @author kfuchsbe
 * 
 */
public interface MeasurementManagerAware extends BeanAware {

	/**
	 * the method used by the factory to inject the {@link MeasurementManager}
	 * 
	 * @param measurementManager
	 */
	public void setMeasurementManager(MeasurementManager measurementManager);
}
