package cern.accsoft.steering.aloha.calc.sensitivity;

import java.util.List;

public interface SensitivityMatrixManagerConfig {

	/**
	 * @param varyMonitorGains
	 *            the varyMonitorGains to set
	 */
	public abstract void setVaryMonitorGains(boolean varyMonitorGains);

	/**
	 * @return the varyMonitorGains
	 */
	public abstract boolean isVaryMonitorGains();

	/**
	 * @param varyCorrectorGains
	 *            the varyCorrectorGains to set
	 */
	public abstract void setVaryCorrectorGains(boolean varyCorrectorGains);

	/**
	 * @return the varyCorrectorGains
	 */
	public abstract boolean isVaryCorrectorGains();

	/**
	 * @param minNorm
	 *            the minNorm to set
	 */
	public abstract void setMinNorm(double minNorm);

	/**
	 * @return the minNorm
	 */
	public abstract double getMinNorm();

	/**
	 * @return the configurations of the individual contributors
	 */
	public abstract List<SensitivityMatrixContributorState> getContributorConfigs();
	
	/**
	 * @param listener
	 *            the listener to add
	 */
	public void addListener(SensitivityMatrixManagerListener listener);

	/**
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(SensitivityMatrixManagerListener listener);


}