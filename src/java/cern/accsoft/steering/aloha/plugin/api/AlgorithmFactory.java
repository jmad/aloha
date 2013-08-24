/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.calc.algorithm.Algorithm;
import cern.accsoft.steering.aloha.calc.solve.Solver;

/**
 * the interface of a class that creates a calculator
 * 
 * @author tbaer
 * 
 */
public interface AlgorithmFactory extends AlohaPlugin {

	/**
	 * create a calculator for the given solver
	 * 
	 * @param solver
	 *            the solver for which to create a calculator
	 * @return the calculator
	 */
	public Algorithm createAlgorithm(Solver solver);

}
