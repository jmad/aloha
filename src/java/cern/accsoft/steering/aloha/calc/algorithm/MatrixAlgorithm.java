package cern.accsoft.steering.aloha.calc.algorithm;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.calc.solve.SolverException;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolver;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResult;

public class MatrixAlgorithm extends AbstractAlgorithm<MatrixSolver> implements Algorithm {

    /**
     * just performs one step of calculation.
     * 
     * @return the resulting vector
     * @throws CalculatorException if the system of equations cannot be solved
     */
    public Matrix calc() throws CalculatorException {
        Matrix sensityMatrix = getSensityMatrixManager().createSensitivityMatrix();
        MatrixSolverResult result;
        try {
            result = getSolver().solve(sensityMatrix, getSensityMatrixManager().getActiveDifferenceVector(),
                    getSensityMatrixManager().getActiveDifferenceVectorErrors());
        } catch (SolverException e) {
            throw new CalculatorException("Could not solve the system of equations.", e);
        }
        getSensityMatrixManager().apply(result);
        return result.getResultVector();
    }

    @Override
    protected void doReset() {
        /* Nothing special to do here */
    }

}
