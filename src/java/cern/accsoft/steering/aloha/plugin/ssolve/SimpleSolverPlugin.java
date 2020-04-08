/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.ssolve;

import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.calc.algorithm.Algorithm;
import cern.accsoft.steering.aloha.calc.algorithm.SimpleAlgorithm;
import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.gui.panels.solve.SolverConfigPanel;
import cern.accsoft.steering.aloha.plugin.api.AbstractAlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.AlgorithmFactory;
import cern.accsoft.steering.aloha.plugin.api.AlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.SolverConfigPanelFactory;
import cern.accsoft.steering.aloha.plugin.api.SolverProvider;
import cern.accsoft.steering.aloha.plugin.ssolve.solve.SimpleSolver;
import cern.accsoft.steering.aloha.plugin.ssolve.solve.sannealing.SimulatedAnnealingConfigPanel;
import cern.accsoft.steering.aloha.plugin.ssolve.solve.sannealing.SimulatedAnnealingSolver;
import cern.accsoft.steering.aloha.plugin.ssolve.solve.sannealing.SimulatedAnnealingSolverConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * A plugin which provides additional solvers and an additional calculator
 * 
 * @author tbaer
 * 
 */
public class SimpleSolverPlugin extends AbstractAlohaPlugin implements
		AlohaPlugin, SolverProvider, AlgorithmFactory, SolverConfigPanelFactory {

	/** all the solvers provided by this plugin */
	private List<Solver> solvers = new ArrayList<Solver>();

	@Override
	public String getName() {
		return "Simple solvers";
	}

	/**
	 * the init method which is called automatically by the bean-factory
	 */
	@InitMethod
	public void init() {
		this.solvers.add(getAlohaBeanFactory().create(
				SimulatedAnnealingSolver.class));
	}

	@Override
	public List<Solver> getSolvers() {
		return this.solvers;
	}

	@Override
	public Algorithm createAlgorithm(Solver solver) {
		if (solver instanceof SimpleSolver) {
			SimpleAlgorithm algorithm = getAlohaBeanFactory().create(
					SimpleAlgorithm.class);
			algorithm.setSolver((SimpleSolver) solver);
			return algorithm;
		}
		return null;
	}

	@Override
	public SolverConfigPanel createConfigPanel(Solver solver) {
		if (solver instanceof SimulatedAnnealingSolverConfig) {
			SimulatedAnnealingConfigPanel panel = new SimulatedAnnealingConfigPanel();
			panel.setSolverConfig((SimulatedAnnealingSolverConfig) solver);

			/*
			 * the configure of the aloha bean factore ensures that all the
			 * managers are set to the panel (if needed) and the init method is
			 * called.
			 */
			getAlohaBeanFactory().configure(panel);
			return panel;
		}
		return null;
	}

}
