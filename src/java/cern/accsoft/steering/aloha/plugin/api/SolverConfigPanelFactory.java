/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.gui.panels.solve.SolverConfigPanel;

/**
 * This class is the interface of a factory for solver-config panels
 * 
 * @author kfuchsbe
 * 
 */
public interface SolverConfigPanelFactory extends AlohaPlugin {

	/**
	 * this method must create a config panel for the given solver.
	 * 
	 * @param solver
	 *            the solver for which to create a panel
	 * @return the config-panel
	 */
	public SolverConfigPanel createConfigPanel(Solver solver);
}
