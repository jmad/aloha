package cern.accsoft.steering.aloha.calc.algorithm;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.calc.solve.SolverException;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolver;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatrixAlgorithm extends AbstractAlgorithm<MatrixSolver> implements Algorithm {
    private final static Logger LOGGER = LoggerFactory.getLogger(MatrixAlgorithm.class);

    /**
     * just performs one step of calculation.
     *
     * @return the resulting vector
     * @throws CalculatorException if the system of equations cannot be solved
     */
    public Matrix calc() throws CalculatorException {
        /* we get the difference vectors first to avoid re-calculating the RM of the pristine model once again after
        establishing the sensitivity matrix (calculating the sensitivity may invalidate the model state) */
        Matrix activeDifferenceVector = getSensitivityMatrixManager().getActiveDifferenceVector();
        Matrix activeDifferenceVectorErrors = getSensitivityMatrixManager().getActiveDifferenceVectorErrors();

        Matrix sensitivityMatrix = getSensitivityMatrixManager().createSensitivityMatrix();
        MatrixSolverResult result;
        LOGGER.info("Solving {}x{} matrix problem ...", sensitivityMatrix.getRowDimension(),
                sensitivityMatrix.getColumnDimension());
        try {
            result = getSolver().solve(sensitivityMatrix, activeDifferenceVector, activeDifferenceVectorErrors);
        } catch (SolverException e) {
            throw new CalculatorException("Could not solve the system of equations.", e);
        }
        LOGGER.info("Matrix problem solved.");
        getSensitivityMatrixManager().apply(result);
        return result.getResultVector();
    }

    @Override
    protected void doReset() {
        /* Nothing special to do here */
    }

}
