package cern.accsoft.steering.aloha.plugin.ssolve.solve.sannealing;

import cern.accsoft.steering.aloha.calc.solve.AbstractSolver;
import cern.accsoft.steering.aloha.plugin.ssolve.solve.SimpleSolver;

/**
 * This is the implementation of a Solver using the simulated annealing - method
 * <p>
 * TODO fill with functionality
 * 
 * @author tbaer
 * 
 */
public class SimulatedAnnealingSolver extends AbstractSolver implements
		SimpleSolver, SimulatedAnnealingSolverConfig {

	/** the name of the solver */
	private final static String SOLVER_NAME = "Simulated Annealing";

	@Override
	public String getName() {
		return SOLVER_NAME;
	}

}
