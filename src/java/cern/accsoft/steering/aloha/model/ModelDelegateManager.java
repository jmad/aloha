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
    public List<ModelDelegateInstance> getModelDelegateInstances();

    /**
     * @return all the model delegates.
     */
    public List<ModelDelegate> getModelDelegates();

    /**
     * add a listener to the {@link ModelDelegateManager}
     * 
     * @param listener
     */
    public void addListener(ModelDelegateManagerListener listener);

    /**
     * remove a listener
     * 
     * @param listener the listener to remove
     */
    public void removeListener(ModelDelegateManagerListener listener);

    /**
     * @return The model - delegate of the active measurement (if any)
     */
    public ModelDelegate getActiveModelDelegate();

}
