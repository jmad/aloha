package cern.accsoft.steering.aloha.calc.sensitivity;

public interface SensitivityMatrixContributorState {

	/**
	 * @param active
	 *            the active to set
	 */
	public abstract void setActive(boolean active);

	/**
	 * @return the active
	 */
	public abstract boolean isActive();

	/**
	 * @param manualFactor
	 *            the manualFactor to set
	 */
	public abstract void setManualFactor(double manualFactor);

	/**
	 * @return the manualFactor
	 */
	public abstract double getManualFactor();

	/**
	 * @return the name of the contributor
	 */
	public String getContributorName();

}