package cern.accsoft.steering.aloha.gui.edit;

import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;

public class AbstractKnobEditHandler {

	private VariationData variationData;

	private ModelDelegateManager modelDelegateManager;

	public AbstractKnobEditHandler() {
		super();
	}

	/**
	 * setter used for DI
	 * 
	 * @param variationData
	 *            the Calculator to set
	 */
	public void setVariationData(VariationData variationData) {
		this.variationData = variationData;
	}

	protected VariationData getVariationData() {
		return this.variationData;
	}

	public void setModelDelegateManager(
			ModelDelegateManager modelDelegateManager) {
		this.modelDelegateManager = modelDelegateManager;
	}

	protected ModelDelegateManager getModelDelegateManager() {
		return modelDelegateManager;
	}

}