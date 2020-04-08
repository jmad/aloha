/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels.solve;

import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.plugin.api.SolverConfigPanelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class keeps track of config-panels for solvers.
 * 
 * @author kfuchsbe
 * 
 */
public class SolverConfigPanelManagerImpl implements SolverConfigPanelManager {

	/** All already instantiated config-panels */
	private Map<Solver, SolverConfigPanel> solverConfigPanels = new HashMap<Solver, SolverConfigPanel>();

	/** All the available factories for solver config-panels */
	private List<SolverConfigPanelFactory> factories = new ArrayList<SolverConfigPanelFactory>();

	@Override
	public SolverConfigPanel getConfigPanel(Solver solver) {
		SolverConfigPanel panel = this.solverConfigPanels.get(solver);
		if (panel == null) {
			panel = createConfigPanel(solver);
			this.solverConfigPanels.put(solver, panel);
		}
		return panel;
	}

	/**
	 * adds all the panels for the solvers to the internal map.
	 * 
	 * @param configPanels
	 *            all the panel-instances to add
	 */
	public void setSolverConfigPanels(
			Map<Solver, SolverConfigPanel> configPanels) {
		this.solverConfigPanels.putAll(configPanels);
	}

	/**
	 * loops through all the factories to create a {@link SolverConfigPanel}.
	 * The first non-null panel is returned.
	 * 
	 * @param solver
	 *            the solver for which to create the panel
	 * @return the panel
	 */
	private SolverConfigPanel createConfigPanel(Solver solver) {
		for (SolverConfigPanelFactory factory : factories) {
			SolverConfigPanel panel = factory.createConfigPanel(solver);
			if (panel != null) {
				return panel;
			}
		}
		return null;
	}

	@Override
	public void addFactory(SolverConfigPanelFactory factory) {
		this.factories.add(factory);
	}

}
