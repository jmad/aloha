package cern.accsoft.steering.aloha.plugin.api;

import java.util.List;

import cern.accsoft.steering.aloha.calc.solve.Solver;

public interface SolverProvider extends AlohaPlugin {

	public List<Solver> getSolvers();

}
