/*
 * $Id: VaryElementsEditHandler.java,v 1.4 2009-01-19 17:13:41 kfuchsbe Exp $
 *
 * $Date: 2009-01-19 17:13:41 $
 * $Revision: 1.4 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.edit;

import cern.accsoft.steering.aloha.calc.variation.KnobVariationParameter;
import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.knob.attribute.ElementAttribute;
import cern.accsoft.steering.jmad.gui.panels.ModelElementsPanelEditHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * uses the selection - feature of the elements-panel to enable certain
 * attributes as variation parameters.
 *
 * @author kfuchsbe
 */
public class VaryElementsEditHandler extends AbstractKnobEditHandler implements
        ModelElementsPanelEditHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(VaryElementsEditHandler.class);

    //
    // Methods for interface ModelElementsPanelEditHandler
    //

    @Override
    public String getCheckableColumnHeader() {
        return "vary";
    }

    @Override
    public Boolean getSelectionValue(Element element, String attributeName) {
        if (getVariationData() == null) {
            return false;
        }
        return (getVariationData().getVariationParameter(
                KnobVariationParameter.createKey(
                        ElementAttribute.getKnobType(), ElementAttribute
                                .createKey(element, attributeName))) != null);
    }

    @Override
    public void setSelectionValue(Element element, String attributeName,
                                  boolean value) {
        if (getVariationData() == null) {
            return;
        }

        String knobKey = ElementAttribute.createKey(element, attributeName);
        if (value) {
            if (element.getAttributeNames().contains(attributeName)) {
                getVariationData().addVariationParameter(
                        new KnobVariationParameter(getModelDelegateManager(),
                                ElementAttribute.getKnobType(), knobKey));
                LOGGER.debug("Variation parameter was createt for element '"
                        + element.getName() + "', attribut '" + attributeName
                        + "'.");
            }
        } else {
            getVariationData().removeVariationParameter(
                    KnobVariationParameter.createKey(ElementAttribute
                            .getKnobType(), knobKey));
            LOGGER.debug("Variation parameter was removed for element '"
                    + element.getName() + "', attribut '" + attributeName
                    + "'.");
        }
    }

}
