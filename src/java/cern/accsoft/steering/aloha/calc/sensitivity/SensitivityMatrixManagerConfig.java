package cern.accsoft.steering.aloha.calc.sensitivity;

import java.util.List;

public interface SensitivityMatrixManagerConfig {

	/**
	 * @param varyMonitorGains
	 *            the varyMonitorGains to set
	 */
	void setVaryMonitorGains(boolean varyMonitorGains);

	/**
	 * @return the varyMonitorGains
	 */
	boolean isVaryMonitorGains();

	/**
	 * @param varyCorrectorGains
	 *            the varyCorrectorGains to set
	 */
	void setVaryCorrectorGains(boolean varyCorrectorGains);

	/**
	 * @return the varyCorrectorGains
	 */
	boolean isVaryCorrectorGains();

	/**
	 * @param minNorm
	 *            the minNorm to set
	 */
	void setMinNorm(double minNorm);

	/**
	 * @return the minNorm
	 */
	double getMinNorm();

	/**
	 * @return the configurations of the individual contributors
	 */
	List<SensitivityMatrixContributorState> getContributorConfigs();
	
	/**
	 * @param listener
	 *            the listener to add
	 */
	void addListener(SensitivityMatrixManagerListener listener);

	/**
	 * @param listener
	 *            the listener to remove
	 */
	void removeListener(SensitivityMatrixManagerListener listener);


}