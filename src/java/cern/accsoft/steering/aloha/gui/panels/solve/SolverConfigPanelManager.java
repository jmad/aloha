/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels.solve;

import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.plugin.api.SolverConfigPanelFactory;

/**
 * This is the interface of a class, that keeps track of all available
 * SolverConfigPanelFactories and keeps track of all alredy instantiated panels.
 * 
 * @author kfuchsbe
 * 
 */
public interface SolverConfigPanelManager {

	/**
	 * must return a config-panel for the given solver. In general the manager
	 * should reuse the instances for the same solver-instances.
	 * 
	 * @param solver
	 *            the Solver for which to return the panel
	 * @return the config-panel for the given solver.
	 */
	public SolverConfigPanel getConfigPanel(Solver solver);

	/**
	 * registers a factory to the manager
	 * 
	 * @param factory
	 *            the factory to add
	 */
	public void addFactory(SolverConfigPanelFactory factory);

}
