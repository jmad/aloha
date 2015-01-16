package cern.accsoft.steering.aloha.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cern.accsoft.steering.aloha.calc.algorithm.Algorithm;

public abstract class AlohaPanelFactory {

	public final JPanel createMonitorSelectionPanel() {
		return new ApplyPanel(createMonitorsPanel());
	}

	public final JPanel createCorrectorSelectionPanel() {
		return new ApplyPanel(createCorrectorsPanel());
	}

	public final JPanel createCalculatorInfoPanel(Algorithm calculator) {
		JPanel panel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;

		JLabel typeLabel = new JLabel();
		typeLabel.setText("Type: " + calculator.getClass().getCanonicalName());
		panel.add(typeLabel, constraints);

		return panel;
	}

	protected abstract MachineElementsPanel createMonitorsPanel();
	protected abstract MachineElementsPanel createCorrectorsPanel();
}
