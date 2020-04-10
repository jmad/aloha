/*
 * $Id: SensityMatrixContributor.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ $Revision: 1.1 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.meas.Measurement;

/**
 * this interface defines methods for classes, that can contribute to the overall sensity-matrix. It defines methods
 * which then can be used by {@link SensitivityMatrixManagerImpl} to query parts of the matrix. NOTE: All matrices must
 * have to same number of rows. The number of columns in general are different.
 * 
 * @author kfuchsbe
 */
public interface SensitivityMatrixContributor {

    /**
     * @return the measurement to which this contributor is related to.
     */
    Measurement getMeasurement();

    /**
     * this method is called by the {@link SensitivityMatrixManagerImpl}, to make it possible for the contributors to
     * store their unperturbed values, in order to be able to correctly calculate the perturbed values later on.
     */
    void initUnperturbed();

    /**
     * @return a name for the contributor, which can be displayed
     */
    String getName();

    /**
     * this method can be called to determine the designated number of rows of the matrices that will be produced by
     * this contributor.
     * 
     * @return the number of rows of the matrices.
     */
    int getMatrixRowCount();

    /**
     * @return the part of the sensitivity-matrix corresponding to the monitor-gains.
     */
    Matrix calcMonitorSensitivityMatrix();

    /**
     * @return the part of the sensitivity-matrix corresponding to the corrector-gains.
     */
    Matrix calcCorrectorSensitivityMatrix();

    /**
     * @param delta the variation that was used to trim the parameter. This is needed by the method to calc the gradient
     *            correctly.
     * @param normalizationFactor the normalization factor to use to calc the column. If none is given (null) then the
     *            own normalization-factor must be calculated by the method.
     * @return a column, which represents the perturbation of the actual values in comparison to the default one.
     */
    PerturbedColumn calcPerturbedColumn(double delta, Double normalizationFactor);

    /**
     * @return the difference-vector between measurement-model, corresponding to the rows in the other matrices
     */
    Matrix getDifferenceVector();

    /**
     * This method must return a vector which has the same dimensions as the difference-vector as returned by
     * {@link #getDifferenceVector()}. This vector shall represent the errors on the difference vector. It will be used
     * to estimate the errors on the fitted parameters.
     * 
     * @return a vector representing the errors on the difference vector.
     */
    Matrix getDifferenceVectorErrors();

}
