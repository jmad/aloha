package cern.accsoft.steering.aloha.gui.panels.fit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cern.accsoft.steering.aloha.gui.icons.Icon;
import cern.accsoft.steering.aloha.gui.menus.CalcActionHandler;

public class CalcButtonsPanel extends JPanel {
	private static final long serialVersionUID = -5557839218237004429L;

	/** the handler, which contains the methods to execute */
	private CalcActionHandler calcActionHandler;

	/** the number of iterations */
	private int iterations = 1;

	/**
	 * init method called by spring
	 */
	public void init() {
		initComponents();
	}

	/**
	 * adds all components to the panel
	 */
	private void initComponents() {
		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.BOTH;

		JButton btn;

		constraints.gridy++;
		btn = new JButton(createResetAction());
		add(btn, constraints);

		constraints.gridy++;
		btn = new JButton(createResetModelAction());
		add(btn, constraints);

		constraints.gridy++;
		btn = new JButton(createCalcAction());
		btn.setIcon(Icon.CALC_ONCE.getImageIcon());
		btn.setDisabledIcon(Icon.CALC_ONCE_DISABLED.getImageIcon());
		add(btn, constraints);

		constraints.gridy++;
		constraints.gridwidth = 1;
		JLabel lbl = new JLabel("Iterations: ");
		add(lbl, constraints);

		constraints.gridx++;
		final JFormattedTextField txtIterations = new JFormattedTextField(
				new Integer(iterations));
		txtIterations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iterations = (Integer) txtIterations.getValue();
			}
		});
		add(txtIterations, constraints);
	}

	/**
	 * creates the action, which starts the calculation
	 * 
	 * @return the action
	 */
	private Action createCalcAction() {
		AbstractAction calcAction = new AbstractAction("calc") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				getCalcActionHandler().calc(iterations);
			}
		};
		calcAction.putValue(Action.SHORT_DESCRIPTION,
				"Performs the given steps of calculation.");
		return calcAction;
	}

	private Action createResetAction() {
		AbstractAction calcAction = new AbstractAction("reset") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				getCalcActionHandler().reset();
			}
		};
		calcAction.putValue(Action.SHORT_DESCRIPTION,
				"Resets the Calculator to its initial state.");
		return calcAction;

	}

	private Action createResetModelAction() {
		AbstractAction calcAction = new AbstractAction("reset models") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				getCalcActionHandler().resetModels();
			}
		};
        calcAction.putValue(Action.SHORT_DESCRIPTION,
				"Resets all the models to their initial state.");
		return calcAction;

	}

	/**
	 * @param calcActionHandler
	 *            the calcActionHandler to set
	 */
	public void setCalcActionHandler(CalcActionHandler calcActionHandler) {
		this.calcActionHandler = calcActionHandler;
	}

	/**
	 * @return the calcActionHandler
	 */
	private CalcActionHandler getCalcActionHandler() {
		return calcActionHandler;
	}

}
