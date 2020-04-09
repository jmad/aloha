/**
 * 
 */
package cern.accsoft.steering.aloha.model;

import cern.accsoft.steering.aloha.meas.MeasurementManager.ModelDelegateInstance;

import java.util.List;

/**
 * the interface of a class, that keeps track of all currently available model-delegates.
 * 
 * @author kfuchsbe
 */
public interface ModelDelegateManager {

    /**
     * @return all model-instances in a list
     */
    List<ModelDelegateInstance> getModelDelegateInstances();

    /**
     * @return all the model delegates.
     */
    List<ModelDelegate> getModelDelegates();

    /**
     * add a listener to the {@link ModelDelegateManager}
     * 
     * @param listener
     */
    void addListener(ModelDelegateManagerListener listener);

    /**
     * remove a listener
     * 
     * @param listener the listener to remove
     */
    void removeListener(ModelDelegateManagerListener listener);

    /**
     * @return The model - delegate of the active measurement (if any)
     */
    ModelDelegate getActiveModelDelegate();

}
