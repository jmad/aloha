/**
 * 
 */
package cern.accsoft.steering.aloha.model.adapt;

import cern.accsoft.steering.jmad.model.JMadModel;

/**
 * This manager returns a model adapter for a given model, if available.
 * 
 * @author kfuchsbe
 * 
 */
public interface ModelAdapterManager {

	/**
	 * returns a (singleton) instance of a model adapter which is valid for the
	 * given model.
	 * 
	 * @param model
	 *            the model for which to find an adapter
	 * @return the adapter.
	 */
	public JMadModelAdapter getModelAdapter(JMadModel model);
}
