/*
 * $Id: SvdSolver.java,v 1.3 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.solve.matrix.svd;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import cern.accsoft.steering.aloha.calc.solve.SolverException;
import cern.accsoft.steering.aloha.calc.solve.matrix.AbstractMatrixSolver;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolver;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResult;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResultImpl;
import cern.accsoft.steering.jmad.util.MatrixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the solver-interface, which uses SVD to calculate the inverse matrix
 * 
 * @author kfuchsbe
 */
public class SvdSolver extends AbstractMatrixSolver implements MatrixSolver, SvdSolverConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(SvdSolver.class);

    /** the name of the solver */
    private final static String SOLVER_NAME = "SVD";

    /**
     * singular values below the max singular value times this factor are not used
     */
    private double svdThresholdFactor = 0.025;

    @Override
    public MatrixSolverResult solve(Matrix inMatrix, Matrix inVector, Matrix inVectorErrors) throws SolverException {
        checkInputDimensions(inMatrix, inVector);

        Matrix matrix;
        Matrix vector;
        if (inMatrix.getRowDimension() >= inMatrix.getColumnDimension()) {
            matrix = inMatrix;
            vector = inVector;
        } else {
            LOGGER.warn("We need more matrix rows than columns for svd. Adding zero rows.");

            int size = inMatrix.getColumnDimension();

            /* create a bigger matrix and set the old one in the beginning */
            matrix = new Matrix(size, size);
            matrix.setMatrix(0, inMatrix.getRowDimension() - 1, 0, inMatrix.getColumnDimension() - 1, inMatrix);

            /* do the same with the input-vector */
            vector = new Matrix(size, inVector.getColumnDimension());
            vector.setMatrix(0, inVector.getRowDimension() - 1, 0, inVector.getColumnDimension() - 1, inVector);
        }

        LOGGER.info("starting singular value decomposition ...");
        SingularValueDecomposition svd = matrix.svd();
        LOGGER.info("   ... finished.");

        /* try to get rid of old stuff. */
        System.gc();

        LOGGER.info("multiplying matrices ...");
        Matrix transformMatrix = svd.getV().times(calcInvertDiagonal(svd.getSingularValues())).times(
                svd.getU().transpose());
        Matrix outVector = transformMatrix.times(vector);

        /*
         * creating the result
         */
        MatrixSolverResultImpl result = new MatrixSolverResultImpl(outVector);

        /*
         * calculating the sensitivities for the parameters
         * 
         * create a vector containing ones, in order to get the eigenvalues in the correct order
         */
        Matrix onesVector = new Matrix(vector.getRowDimension(), 1, 1.0);
        Matrix sensitivities = transformMatrix.times(onesVector);
        double sum = 0;
        for (int i = 0; i < sensitivities.getRowDimension(); i++) {
            double value = Math.abs(sensitivities.get(i, 0));
            if (value > 0) {
                value = 1 / value;
            } else {
                value = 0;
            }
            sensitivities.set(i, 0, value);
            sum += value;
        }

        /*
         * finally we normalize to the sum
         */
        if (sum > 0) {
            result.setParameterSensitivities(sensitivities.times(1 / sum));
        } else {
            result.setParameterSensitivities(sensitivities);
        }

        /*
         * calculate the errors
         */
        result.setParameterErrorEstimates(calcParameterErrorEstimates(transformMatrix, inVectorErrors));

        LOGGER.debug("   ... finished.");
        return result;
    }

    /**
     * calculates the "inverse" matrix by taking the inverse of the singular values. All values below the
     * threshold-factor are ignored.
     * 
     * @param singularValues the singular values whose inverted values will be contained in the diagonal of the matrix
     * @return the matrix with 1/singularValue in the diagonal
     */
    Matrix calcInvertDiagonal(double[] singularValues) {
        int valuesCount = singularValues.length;

        Matrix matrix = new Matrix(valuesCount, valuesCount);

        if (valuesCount < 1) {
            return matrix;
        }

        // the max value is the first in the array:
        double maxValue = Math.abs(singularValues[0]);
        double threshold = maxValue * getSvdThresholdFactor();

        String usedSingularValues = "";
        String skippedSingularValues = "";
        for (int i = 0; i < valuesCount; i++) {
            if (Math.abs(singularValues[i]) > threshold) {
                matrix.set(i, i, 1 / singularValues[i]);
                if (usedSingularValues.length() > 0) {
                    usedSingularValues += ",";
                }
                usedSingularValues += singularValues[i];
            } else {
                if (skippedSingularValues.length() > 0) {
                    skippedSingularValues += ",";
                }
                skippedSingularValues += singularValues[i];
            }
        }
        LOGGER.info("Used singular Values: " + usedSingularValues + ".");
        LOGGER.info("Skipped singular Values: " + skippedSingularValues + ".");
        return matrix;
    }

    @Override
    public void setSvdThresholdFactor(double svdThresholdFactor) {
        this.svdThresholdFactor = svdThresholdFactor;
    }

    @Override
    public double getSvdThresholdFactor() {
        return svdThresholdFactor;
    }

    @Override
    public String getName() {
        return SOLVER_NAME;
    }

    /**
     * calculates the error estimates for the parameters calculated by one fit iteration.
     * <p>
     * The implemented currently is calculated by: sqrt(transformMatrix.^2 . inErrors.^2).
     * <p>
     * Here the ".^" means an element-wise square, "." is the normal dot product, and also the has to be element-wise.
     * 
     * @param transformMatrix the pseudo inverse matrix
     * @param inErrors the errors on the input-vector
     * @return the estimated errors for the calculated parameter-deltas
     * @throws SolverException if the dimensions are not compatible.
     */
    private Matrix calcParameterErrorEstimates(Matrix transformMatrix, Matrix inErrors) throws SolverException {
        Matrix vector;
        if (transformMatrix.getColumnDimension() > inErrors.getRowDimension()) {
            /* seems that the in matrix was resized */
            LOGGER.warn("Seems that matrix was resized. Adding zero rows to error vector.");
            int size = transformMatrix.getColumnDimension();

            /* resize the input-vector */
            vector = new Matrix(size, inErrors.getColumnDimension());
            vector.setMatrix(0, inErrors.getRowDimension() - 1, 0, inErrors.getColumnDimension() - 1, inErrors);

        } else if (transformMatrix.getColumnDimension() == inErrors.getRowDimension()) {
            vector = inErrors;
        } else {
            throw new SolverException("inError vector has more rows than the transformMatrix has columns.");
        }

        /*
         * square the elements
         */
        Matrix transformMatrixSquared = transformMatrix.arrayTimes(transformMatrix);
        Matrix vectorSquared = vector.arrayTimes(vector);

        return MatrixUtil.sqrtByElements(transformMatrixSquared).times(vectorSquared);
    }

}
