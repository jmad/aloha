package cern.accsoft.steering.aloha.plugin.msolve;

import cern.accsoft.steering.aloha.calc.algorithm.Algorithm;
import cern.accsoft.steering.aloha.calc.algorithm.MatrixAlgorithm;
import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolver;
import cern.accsoft.steering.aloha.plugin.api.AbstractAlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.AlgorithmFactory;

public class MatrixSolverPlugin extends AbstractAlohaPlugin implements
		AlgorithmFactory {

	@Override
	public Algorithm createAlgorithm(Solver solver) {
		if (solver instanceof MatrixSolver) {
			MatrixAlgorithm algorithm = getAlohaBeanFactory().create(
					MatrixAlgorithm.class);
			algorithm.setSolver((MatrixSolver) solver);
			return algorithm;

		}
		return null;
	}

	@Override
	public String getName() {
		return "Matrix solvers";
	}

}
