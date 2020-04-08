/**
 * 
 */
package cern.accsoft.steering.aloha.calc.algorithm;

import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.plugin.api.AlgorithmFactory;

/**
 * This is the interface of a class that keeps track of all available Algorithm
 * factories. It creates algorithms for solvers if necessary or returns already
 * created instances..
 * 
 * @author kfuchsbe
 * 
 */
public interface AlgorithmManager {

	/**
	 * registers an algorithm-factory, that can be used to create an algorithm
	 * for a solver.
	 * 
	 * @param factory
	 */
	public void addAlgorithmFactory(AlgorithmFactory factory);

	/**
	 * must return an instance of an algorithm that uses the given solver. It
	 * should return always the same instance for the same solver instance.
	 * 
	 * @param solver
	 *            the solver for which to find an algorithm
	 * @return the algorithm
	 */
	public Algorithm getAlgorithm(Solver solver);
}
