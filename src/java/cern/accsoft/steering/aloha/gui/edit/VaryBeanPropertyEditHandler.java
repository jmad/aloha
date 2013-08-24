/*
 * $Id: VaryBeanPropertyEditHandler.java,v 1.1 2009-01-19 17:13:41 kfuchsbe Exp $
 * 
 * $Date: 2009-01-19 17:13:41 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.edit;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.calc.variation.KnobVariationParameter;
import cern.accsoft.steering.jmad.domain.knob.KnobType;
import cern.accsoft.steering.jmad.domain.knob.bean.BeanPropertyKnob;
import cern.accsoft.steering.util.gui.table.BeanTableEditHandler;

/**
 * This class allows to create variation parameters, which vary simple bean
 * properties.
 * 
 * @author kfuchsbe
 * 
 */
public class VaryBeanPropertyEditHandler extends AbstractKnobEditHandler
		implements BeanTableEditHandler {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(VaryElementsEditHandler.class);

	private KnobType knobType;

	/*
	 * methods of interface BeanTableEditHandler
	 */

	@Override
	public Boolean getCheckValue(Object bean, String propertyName) {
		if (getVariationData() == null) {
			return false;
		}
		return (getVariationData().getVariationParameter(
				KnobVariationParameter.createKey(getKnobType(),
						BeanPropertyKnob.createKey(bean, propertyName))) != null);
	}

	@Override
	public String getCheckableColumnHeader() {
		return "vary";
	}

	@Override
	public void setCheckValue(Object bean, String propertyName, boolean value) {
		if (getVariationData() == null) {
			return;
		}

		if (value) {
			getVariationData().addVariationParameter(
					new KnobVariationParameter(getModelDelegateManager(),
							getKnobType(), BeanPropertyKnob.createKey(bean,
									propertyName)));
			logger.debug("Variation parameter was createt for bean '"
					+ bean.toString() + "', property '" + propertyName + "'.");
		} else {
			getVariationData().removeVariationParameter(
					KnobVariationParameter.createKey(getKnobType(),
							BeanPropertyKnob.createKey(bean, propertyName)));
			logger.debug("Variation parameter was removed for bean '"
					+ bean.toString() + "', property '" + propertyName + "'.");
		}
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	private KnobType getKnobType() {
		return this.knobType;
	}

	public void setKnobType(KnobType knobType) {
		this.knobType = knobType;
	}

}
