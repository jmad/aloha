/**
 * 
 */
package cern.accsoft.steering.aloha.calc.solve.matrix;

import Jama.Matrix;

/**
 * The implementation of an object representing the result of an matrix-solve
 * iteration
 * 
 * @author kaifox
 * 
 */
public class MatrixSolverResultImpl implements MatrixSolverResult {

	/** the sensity values for the parameters */
	private Matrix parameterSensitivities = new Matrix(0, 0);

	/** The result of the solve operation */
	private Matrix resultVector = new Matrix(0, 0);

	/** The error estimates for the results */
	private Matrix parameterErrorEstimates = new Matrix(0, 0);

	/**
	 * the constructor which enforces to have at least the result.
	 * 
	 * @param resultVector
	 */
	public MatrixSolverResultImpl(Matrix resultVector) {
		this.resultVector = resultVector;
		this.parameterSensitivities = new Matrix(
				resultVector.getRowDimension(), resultVector
						.getColumnDimension());
		this.setParameterErrorEstimates(new Matrix(resultVector
				.getRowDimension(), resultVector.getColumnDimension()));
	}

	@Override
	public Matrix getParameterErrorEstimates() {
		return this.parameterErrorEstimates;
	}

	@Override
	public Matrix getParameterSensitivities() {
		return this.parameterSensitivities;
	}

	@Override
	public Matrix getResultVector() {
		return this.resultVector;
	}

	public void setParameterSensitivities(Matrix parameterSensitivities) {
		this.parameterSensitivities = parameterSensitivities;
	}

	public void setParameterErrorEstimates(Matrix parameterErrorEstimates) {
		this.parameterErrorEstimates = parameterErrorEstimates;
	}

}
