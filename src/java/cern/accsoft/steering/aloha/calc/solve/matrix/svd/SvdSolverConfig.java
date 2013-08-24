package cern.accsoft.steering.aloha.calc.solve.matrix.svd;

import cern.accsoft.steering.aloha.calc.solve.SolverConfig;

public interface SvdSolverConfig extends SolverConfig {

	/**
	 * @param svdThresholdFactor
	 *            the svdThresholdFactor to set
	 */
	public abstract void setSvdThresholdFactor(double svdThresholdFactor);

	/**
	 * @return the svdThresholdFactor
	 */
	public abstract double getSvdThresholdFactor();

}