/*
 * $Id: VaryStrengthsEditHandler.java,v 1.3 2009-01-19 17:13:41 kfuchsbe Exp $
 *
 * $Date: 2009-01-19 17:13:41 $
 * $Revision: 1.3 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.edit;

import cern.accsoft.steering.aloha.calc.variation.KnobVariationParameter;
import cern.accsoft.steering.jmad.domain.knob.Knob;
import cern.accsoft.steering.jmad.gui.panels.AbstractKnobsPanel;
import cern.accsoft.steering.jmad.gui.panels.KnobsPanelEditHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implements editing-methods for {@link AbstractKnobsPanel} to create and
 * delete Variation-Parameters for a variationData.
 *
 * @author kfuchsbe
 */
public class VaryStrengthsEditHandler extends AbstractKnobEditHandler implements
        KnobsPanelEditHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(VaryStrengthsEditHandler.class);

    @Override
    public String getCheckableColumnHeader() {
        return "vary";
    }

    @Override
    public boolean getSelectionValue(Knob strength) {
        if (getVariationData() == null) {
            return false;
        }

        return (getVariationData().getVariationParameter(
                KnobVariationParameter.createKey(strength)) != null);
    }

    @Override
    public void setSelectionValue(Knob strength, boolean value) {
        if (getVariationData() == null) {
            return;
        }

        if (value) {
            getVariationData().addVariationParameter(
                    new KnobVariationParameter(getModelDelegateManager(),
                            strength));
            LOGGER.debug("Variation parameter was added for strength '"
                    + strength + "'.");
        } else {
            getVariationData().removeVariationParameter(
                    KnobVariationParameter.createKey(strength));
            LOGGER.debug("Variation parameter was removed for strength '"
                    + strength + "'.");
        }
    }

}
