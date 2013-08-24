package cern.accsoft.steering.aloha.plugin.xxx.sensitivity;

import cern.accsoft.steering.jmad.domain.optics.Optic;

public interface OpticsConstraint {

	/**
	 * returns the residual for the given optics.
	 * 
	 * @param optics
	 *            the optics for which to calc the residual
	 * @return the residual
	 */
	public double calcValue(Optic optics);
	
	public double getTargetValue();

}
