/**
 * 
 */
package cern.accsoft.steering.aloha.model.adapt;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.model.JMadModel;

/**
 * This is the implementation of a class which provides model adapters for
 * different model types.
 * 
 * @author kfuchsbe
 * 
 */
public class ModelAdapterManagerImpl implements ModelAdapterManager {

	/**
	 * all the available model-adapters
	 */
	private List<JMadModelAdapter> modelAdapters = new ArrayList<>();

	@Override
	public JMadModelAdapter getModelAdapter(JMadModel model) {
		/*
		 * we simply return the first adapter that accepts the model.
		 */
		for (JMadModelAdapter adapter : getModelAdapters()) {
			if (adapter.appliesTo(model)) {
				return adapter;
			}
		}
		return null;
	}

	public void setModelAdapters(List<JMadModelAdapter> modelAdapters) {
		this.modelAdapters = modelAdapters;
	}

	private List<JMadModelAdapter> getModelAdapters() {
		return modelAdapters;
	}

}
