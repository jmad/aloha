package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.calc.solve.Solver;

import java.util.List;

public interface SolverProvider extends AlohaPlugin {

	public List<Solver> getSolvers();

}
