/*
 * $Id: MicadoSolver.java,v 1.3 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ 
 * $Revision: 1.3 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.solve.matrix.micado;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.calc.solve.SolverException;
import cern.accsoft.steering.aloha.calc.solve.matrix.AbstractMatrixSolver;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolver;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResult;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the implementation of the {@link Solver} interface, which uses the
 * MICADO algorithm to solve the least square problem. This class uses a
 * (stripped down) version of the MICADO algorithm as used in YASP. It calls the
 * static Methods of {@link MICADO} and just adopts the interface for our needs.
 * 
 * @author kfuchsbe
 * 
 */
public class MicadoSolver extends AbstractMatrixSolver implements MatrixSolver,
		MicadoSolverConfig {
	private final static Logger LOGGER = LoggerFactory.getLogger(MicadoSolver.class);

	/** the name of the solver */
	private final static String SOLVER_NAME = "MICADO";

	/**
	 * the number of iterations, which we want to execute (number of parameters
	 * which will be changed.)
	 */
	private int iterations = 1;

	@Override
	public MatrixSolverResult solve(Matrix matrix, Matrix inVector,
			Matrix inVectorErrors) throws SolverException {
		checkInputDimensions(matrix, inVector);

		/*
		 * prepare the arrays needed for MICADO. In order to be sure, that we do
		 * not disturb anything else, we make copies of the arrays.
		 */

		/* the dimensions of the matrix */
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		/* the matrix as array */
		double[][] matrixArray = matrix.getArrayCopy();

		/*
		 * the inVector as array (originally it is a col-vector, so transpose it
		 * first to get a 1d-array.
		 */
		double[] inVectorArray = inVector.timesEquals(1.0).transpose()
				.getArrayCopy()[0];

		/* preparation for the output vectors */
		double[] parameterChangesValues = new double[cols];
		double[] pivots = new double[cols];
		int[] parameterChangesIndizes = new int[cols];
		double[] remainingDifferences = new double[rows];

		int iter = getIterations();
		if (iter > cols) {
			iter = cols;
			LOGGER.warn("Cannot perform " + getIterations()
					+ " iterations, since only " + iter
					+ " parameters defined. -> Performing " + iter
					+ " iterations.");
		}

		/*
		 * now we are ready to call the algorithm
		 */
		if (!MICADO.solveMicado(matrixArray, inVectorArray, rows, cols, iter,
				1, 1, parameterChangesValues, parameterChangesIndizes,
				remainingDifferences, pivots)) {
			throw new SolverException("Error while executing MICADO!");
		}

		/*
		 * and finally we have to construct the new vector and set the values in
		 * the correct order. (We have to invert the values we get from MICADO
		 * in order to get the right corrections)
		 */
		Matrix outVector = new Matrix(cols, 1);
		Matrix sensitivities = new Matrix(cols, 1);

		double sum = 0;
		for (int i = 0; i < parameterChangesIndizes.length; i++) {
			outVector.set(parameterChangesIndizes[i], 0,
					-parameterChangesValues[i]);

			double sensitivityValue = Math.abs(pivots[i]);
			sensitivities.set(parameterChangesIndizes[i], 0, sensitivityValue);
			sum += sensitivityValue;
		}

		MatrixSolverResultImpl result = new MatrixSolverResultImpl(outVector);

		if (sum > 0) {
			result.setParameterSensitivities(sensitivities.times(1 / sum));
		} else {
			result.setParameterSensitivities(sensitivities);
		}

		/* and thats it (hopefully ;-) */
		return result;
	}

	@Override
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	@Override
	public int getIterations() {
		return iterations;
	}

	@Override
	public String getName() {
		return SOLVER_NAME;
	}

}