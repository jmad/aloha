package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;

public abstract class AbstractAlohaPlugin implements AlohaPlugin,
		AlohaBeanFactoryAware {

	/** The factory to create and configure aloha beans */
	protected AlohaBeanFactory alohaBeanFactory;

	@Override
	public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
		this.alohaBeanFactory = alohaBeanFactory;
	}

	protected AlohaBeanFactory getAlohaBeanFactory() {
		return this.alohaBeanFactory;
	}

}