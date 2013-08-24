package cern.accsoft.steering.aloha.calc.solve.matrix;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.solve.SolverException;

public abstract class AbstractMatrixSolver implements MatrixSolver {

	/**
	 * checks the dimensions of the input-matrix and vector.
	 * 
	 * @param inMatrix
	 * @param inVector
	 * @throws SolverException
	 */
	protected void checkInputDimensions(Matrix inMatrix, Matrix inVector)
			throws SolverException {
		if ((!(inMatrix.getColumnDimension() >= 1))
				|| (!(inMatrix.getRowDimension() >= 1))) {
			throw new SolverException(
					"Matrix has no columns and/or rows. Cannot proceed.");
		}

		if (inVector.getRowDimension() != inMatrix.getRowDimension()) {
			throw new SolverException(
					"Vector has must have the same row dimensions as the matrix ("
							+ inMatrix.getRowDimension() + "), but it has '"
							+ inVector.getRowDimension() + "'. Cannot proceed.");
		}
		if (inVector.getColumnDimension() != 1) {
			throw new SolverException(
					"Vector must have only one column, but has "
							+ inVector.getColumnDimension()
							+ ". Cannot proceed.");
		}
	}

	@Override
	public String toString() {
		return getName();
	}

}