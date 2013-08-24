/**
 * 
 */
package cern.accsoft.steering.aloha.calc.iteration;

import java.util.List;

/**
 * This is the interface for an object, which stores iteration data of an SVD
 * iteration.
 * 
 * @author kfuchsbe
 * 
 */
public interface SvdIterationData extends IterationData {

	/**
	 * @return the eigenvalues which were calculated during this iteration
	 */
	public List<Double> getEigenValues();

	/**
	 * @return the cutoff-limit which was used in this iteration. (All
	 *         eigenvalues below this limit have been thrown away)
	 */
	public double getEigenValueLimit();
}
