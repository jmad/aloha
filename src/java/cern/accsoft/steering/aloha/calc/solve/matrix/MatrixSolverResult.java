/**
 * 
 */
package cern.accsoft.steering.aloha.calc.solve.matrix;

import Jama.Matrix;

/**
 * This interface represents a result of a matrix-solve iteration.
 * 
 * @author kaifox
 * 
 */
public interface MatrixSolverResult {

	/**
	 * the resulting vector of the (pseudo) matrix inversion:
	 * <p>
	 * outVector = matrix^(-1) * inVector;
	 * 
	 * @return the resulting vector
	 */
	public Matrix getResultVector();

	/**
	 * @return the sensitivity-value for each parameter (column-vector)
	 */
	public Matrix getParameterSensitivities();

	/**
	 * calculates error estimates from the errors of the input parameters. 
	 * 
	 * @return a column vector with the errors for the parameters.
	 */
	public Matrix getParameterErrorEstimates();

	

}
