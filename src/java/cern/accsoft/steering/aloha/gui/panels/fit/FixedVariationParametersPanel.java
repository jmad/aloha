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
 * The panel for the variation parameters which are not varied in the current
 * fitting.
 * 
 * @author kfuchsbe
 * 
 */
public class FixedVariationParametersPanel extends
		AbstractVariationParameterPanel {

	private static final long serialVersionUID = -8295723842030707612L;

	@Override
	protected List<VariationParameter> getVariationParameters() {
		return getVariationData().getFixedVariationParameters();
	}

	@Override
	protected List<Action> getAdditionalButtonActions() {
		List<Action> actions = new ArrayList<Action>();
		actions.add(new AbstractAction("unfix") {
			private static final long serialVersionUID = 8734794163313517710L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String key = getSelectedParameterKey();
				if (key == null) {
					return;
				}
				getVariationData().unfixVariationParameter(key);
			}
		});
		return actions;
	}

}
