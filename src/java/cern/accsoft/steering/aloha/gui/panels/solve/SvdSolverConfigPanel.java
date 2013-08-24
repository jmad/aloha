/*
 * $Id: SvdSolverConfigPanel.java,v 1.1 2009-01-15 11:46:25 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:25 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.panels.solve;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.calc.solve.matrix.svd.SvdSolverConfig;

/**
 * This class provides gui-components to configure the SvdSolver
 * 
 * @author kfuchsbe
 * 
 */
public class SvdSolverConfigPanel extends
		GenericSolverConfigPanel<SvdSolverConfig> {
	private static final long serialVersionUID = 2973279357909647645L;

	/*
	 * the input fields
	 */

	/** the text field for the threshold factor */
	private JFormattedTextField txtSvdThresholdFactor;

	/** the aloha preferences */
	private Preferences preferences;

	/**
	 * init-method used by spring
	 */
	public void init() {
		initComponents();
	}

	/**
	 * create all components
	 */
	private void initComponents() {
		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;

		JLabel label = new JLabel("Svd-threshold factor: ");
		add(label, constraints);

		constraints.gridx += 1;
		txtSvdThresholdFactor = new JFormattedTextField(getPreferences()
				.getNumberFormat());
		txtSvdThresholdFactor
				.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		txtSvdThresholdFactor.setValue(getSolverConfig()
				.getSvdThresholdFactor());

		txtSvdThresholdFactor
				.setToolTipText("Cutoff-Factor for Svd: All Singular Values below (factor x maxSingularValue) are thrown away.");
		add(txtSvdThresholdFactor, constraints);
	}

	@Override
	public void apply() {
		getSolverConfig().setSvdThresholdFactor(
				(Double) txtSvdThresholdFactor.getValue());

	}

	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}

	private Preferences getPreferences() {
		return preferences;
	}

}
