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

	public void addedModelDelegate(ModelDelegate newModelDelegate);

	public void removedModelDelegate(ModelDelegate removedModelDelegate);

}
