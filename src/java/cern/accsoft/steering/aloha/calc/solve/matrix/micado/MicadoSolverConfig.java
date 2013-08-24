package cern.accsoft.steering.aloha.calc.solve.matrix.micado;

import cern.accsoft.steering.aloha.calc.solve.SolverConfig;

public interface MicadoSolverConfig extends SolverConfig {

	/**
	 * @param iterations
	 *            the iterations to set
	 */
	public abstract void setIterations(int iterations);

	/**
	 * @return the iterations
	 */
	public abstract int getIterations();

}