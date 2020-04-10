/*
 * $Id: SensitivityMatrixManager.java,v 1.3 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolverResult;
import cern.accsoft.steering.aloha.meas.Measurement;

import java.util.List;

/**
 * this interface defines the methods of a class, that provides a sensitivity matrix for a fit. It also provides a method to
 * apply the found changes in the parameters (corresponding to the columns of the sensitivity matrix) to the correct
 * parameters of the model.
 * 
 * @author kfuchsbe
 */
public interface SensitivityMatrixManager {

    /**
     * @return the difference vector, corresponding to the rows of the sensitivity matrix
     */
    Matrix getActiveDifferenceVector();

    /**
     * @return the difference vector including all contributors
     */
    Matrix getAllDifferenceVector();

    /**
     * @return the errors on the difference vector, corresponding to the rows of the sensitivity matrix
     */
    Matrix getActiveDifferenceVectorErrors();

    /**
     * creates the sensitivity matrix
     * 
     * @return the sensitivity matrix.
     * @throws CalculatorException
     */
    Matrix createSensitivityMatrix() throws CalculatorException;

    /**
     * applies the given changes in the parameter values to the correct values of the model.
     * 
     * @param solverResult the result as calculated from the matrix solver
     */
    void apply(MatrixSolverResult solverResult);

    /**
     * adds a contributor to the manager
     * 
     * @param contributor
     */
    void addContributor(SensitivityMatrixContributor contributor);

    /**
     * removes the contributor for the given measurement.
     * 
     * @param measurement
     */
    void removeContributors(Measurement measurement);

    /**
     * @param listener the listener to add
     */
    void addListener(SensitivityMatrixManagerListener listener);

    /**
     * @param listener the listener to remove
     */
    void removeListener(SensitivityMatrixManagerListener listener);

    List<SensitivityMatrixContributor> getActiveContributors();

    List<SensitivityMatrixContributor> getAllContributors();

}
