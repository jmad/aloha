package cern.accsoft.steering.aloha.plugin.ssolve.solve.sannealing;

import javax.swing.JLabel;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.gui.panels.solve.GenericSolverConfigPanel;

/**
 * This is the config-panel for the simulated-annealing solver
 * 
 * @author tbaer
 * 
 */
public class SimulatedAnnealingConfigPanel extends
		GenericSolverConfigPanel<SimulatedAnnealingSolverConfig> {
	private static final long serialVersionUID = -2168153146811266498L;

	/**
	 * init-method
	 * 
	 * This is called by the {@link AlohaBeanFactory} because an
	 * {@link InitMethod} annotation is present.
	 */
	@InitMethod
	public void init() {
		initComponents();
	}

	/**
	 * create all components
	 */
	private void initComponents() {
		/*
		 * TODO fill in code for creating the swing components.
		 */
		add(new JLabel("Simulated annealing config-panel."));
		validate();
	}

	@Override
	public void apply() {
		/*
		 * TODO fill in code here to set the values to the SolverCofig
		 */
	}

}
