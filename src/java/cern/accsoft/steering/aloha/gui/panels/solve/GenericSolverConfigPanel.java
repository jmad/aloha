/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels.solve;

import cern.accsoft.steering.aloha.calc.solve.SolverConfig;

/**
 * This is generic class of an config-panel for solvers.
 * 
 * @author kfuchsbe
 * 
 */
public abstract class GenericSolverConfigPanel<T extends SolverConfig> extends
		SolverConfigPanel {
	private static final long serialVersionUID = 772269527169482804L;

	/** the solver to configure */
	private T solver;

	/**
	 * sets the solver to use
	 * 
	 * @param solver
	 */
	public final void setSolverConfig(T solver) {
		this.solver = solver;
	}

	/**
	 * @return the solver to be configured by this panel
	 */
	protected T getSolverConfig() {
		return this.solver;
	}

}
