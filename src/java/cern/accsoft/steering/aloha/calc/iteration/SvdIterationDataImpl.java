/**
 * 
 */
package cern.accsoft.steering.aloha.calc.iteration;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation for an iteration data holding info of a SVD iteration
 * 
 * @author kfuchsbe
 * 
 */
public class SvdIterationDataImpl extends IterationDataImpl implements
		SvdIterationData {

	/** The cutoff limit of the eigenvalues */
	private double eigenValueLimit = 0.0;

	/** The eigenvalues of this iteration */
	private List<Double> eigenValues = new ArrayList<Double>();

	/**
	 * The constructor, which sets the iteration number
	 * 
	 * @param iterationNumber
	 */
	public SvdIterationDataImpl(int iterationNumber) {
		super(iterationNumber);
	}

	/**
	 * set the cutoff-limit of the eigenvalues
	 * 
	 * @param limit
	 *            the limit to set
	 */
	public void setEigenValueLimit(double limit) {
		this.eigenValueLimit = limit;
	}

	@Override
	public double getEigenValueLimit() {
		return this.eigenValueLimit;
	}

	public void setEigenValues(double[] eigenValues) {
		this.eigenValues.clear();
		for (double d : eigenValues) {
			this.eigenValues.add(d);
		}
	}

	@Override
	public List<Double> getEigenValues() {
		return this.eigenValues;
	}

}
