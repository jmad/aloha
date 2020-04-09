/**
 * 
 */
package cern.accsoft.steering.aloha.model;

/**
 * a listener to a modelDelegate
 * 
 * @author kfuchsbe
 * 
 */
public interface ModelDelegateManagerListener {
	default void activeModelDelegateChanged(ModelDelegate modelDelegate) {

	}
	default void addedModelDelegate(ModelDelegate newModelDelegate) {

	}
	default void removedModelDelegate(ModelDelegate removedModelDelegate) {

	}
}
