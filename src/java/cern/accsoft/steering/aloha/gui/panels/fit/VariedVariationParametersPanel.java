/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels.fit;

import cern.accsoft.steering.aloha.calc.variation.VariationParameter;
import cern.accsoft.steering.aloha.gui.panels.AbstractVariationParameterPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * this is the panel displaying the really varied variation parameters
 * 
 * @author kfuchsbe
 * 
 */
public class VariedVariationParametersPanel extends
		AbstractVariationParameterPanel {
	private static final long serialVersionUID = -4485545690833656337L;

	@Override
	protected List<VariationParameter> getVariationParameters() {
		return getVariationData().getVariationParameters();
	}

	@Override
	protected List<Action> getAdditionalButtonActions() {
		List<Action> actions = new ArrayList<Action>();
		actions.add(new AbstractAction("fix") {
			private static final long serialVersionUID = 8734794163313517710L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String key = getSelectedParameterKey();
				if (key == null) {
					return;
				}
				getVariationData().fixVariationParameter(key);
			}
		});
		return actions;
	}

}
