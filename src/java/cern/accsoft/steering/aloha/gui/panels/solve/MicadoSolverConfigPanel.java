/*
 * $Id: MicadoSolverConfigPanel.java,v 1.2 2009-02-25 18:48:44 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:44 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.panels.solve;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import cern.accsoft.steering.aloha.calc.solve.matrix.micado.MicadoSolverConfig;

/**
 * This class represents a panel, which provides swing components to configure
 * the Micado-solver.
 * 
 * @author kfuchsbe
 * 
 */
public class MicadoSolverConfigPanel extends
		GenericSolverConfigPanel<MicadoSolverConfig> {
	private static final long serialVersionUID = 2708622392003776061L;

	/*
	 * the input fields
	 */

	/** the text field for the threshold factor */
	private JFormattedTextField txtMicadoIterations;

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

		JLabel label = new JLabel("Micado-iterations: ");
		add(label, constraints);

		constraints.gridx += 1;
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setMaximumFractionDigits(0);
		numberFormat.setMaximumIntegerDigits(4);
		numberFormat.setGroupingUsed(false);
		numberFormat.setParseIntegerOnly(true);
		txtMicadoIterations = new JFormattedTextField(numberFormat);

		txtMicadoIterations
				.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		txtMicadoIterations.setValue(getSolverConfig().getIterations());

		txtMicadoIterations
				.setToolTipText("Micado-iterations: Determines how many iterations the micado-algorithm will perfor.\n "
						+ "This therefore then is also the number of parameters, which will be changed by the algorithm.");
		add(txtMicadoIterations, constraints);
	}

	@Override
	public void apply() {
		getSolverConfig().setIterations(
				(Integer.parseInt(txtMicadoIterations.getText())));
	}

}
