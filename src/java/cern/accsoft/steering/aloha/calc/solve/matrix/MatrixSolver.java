/**
 * 
 */
package cern.accsoft.steering.aloha.calc.solve.matrix;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.calc.solve.SolverException;

/**
 * This is the interface for a solver class that is based on solving a
 * matrix-least-square algorithm.
 * 
 * @author tbaer
 * 
 */
public interface MatrixSolver extends Solver {

	/**
	 * shall give the result: outVector = matrix^(-1) * inVector;
	 * 
	 * @param matrix
	 * @param inVector
	 * @param inVectorErrors
	 * @return the result
	 * @throws SolverException
	 */
	public MatrixSolverResult solve(Matrix matrix, Matrix inVector,
			Matrix inVectorErrors) throws SolverException;

}
