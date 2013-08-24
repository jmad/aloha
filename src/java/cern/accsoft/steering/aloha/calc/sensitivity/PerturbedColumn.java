/*
 * $Id: PerturbedColumn.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

import Jama.Matrix;

/**
 * just a simple class, that represents a (column-) matrix together with a
 * normalization-factor
 * 
 * @author kfuchsbe
 * 
 */
public class PerturbedColumn {

	/** the factor with which the column is normalized */
	private double normalizationFactor = 1;

	/** the column matrix */
	private Matrix columnMatrix = new Matrix(1, 1);

	/**
	 * the constructor, which sets the private elements
	 * 
	 * @param columnMatrix
	 *            the column-matrix
	 * @param normalizationFactor
	 *            the normalization-factor
	 */
	public PerturbedColumn(Matrix columnMatrix, double normalizationFactor) {
		this.columnMatrix = columnMatrix;
		this.normalizationFactor = normalizationFactor;
	}

	/**
	 * @return the normalizationFactor
	 */
	public double getNormalizationFactor() {
		return normalizationFactor;
	}

	/**
	 * @return the columnMatrix
	 */
	public Matrix getColumnMatrix() {
		return columnMatrix;
	}
}
