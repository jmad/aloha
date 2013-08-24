package cern.accsoft.steering.aloha.calc.sensitivity;

/**
 * this class encapsulates simple configuration for the contributors
 * 
 * @author kfuchsbe
 * 
 */
class SensitivityMatrixContributorStateImpl implements SensitivityMatrixContributorState {

	/** the contributor */
	private SensitivityMatrixContributor contributor;

	/**
	 * the constructor which needs a contributor.
	 * 
	 * @param contributor
	 */
	public SensitivityMatrixContributorStateImpl(SensitivityMatrixContributor contributor) {
		this.contributor = contributor;
	}

	/**
	 * if active, the corresponding contributor is used to compose the matrix,
	 * if it is false it is not used.
	 */
	private boolean active = true;

	/**
	 * this factor can be set manually from outside, in order to weight this
	 * contributor in relation to the others while finding eigenvalues.
	 */
	private double manualFactor = 1;

	/**
	 * this factor is calculated automatically, to ensure, that the different
	 * contributors give eigenvalues of a similar magnitude, to ensure, that
	 * matrices of different contributors are weighted equally (if the manual
	 * factors are equal.)
	 */
	private double automaticNorm = 1;

	/**
	 * @return the contributor
	 */
	public SensitivityMatrixContributor getContributor() {
		return contributor;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param manualFactor
	 *            the manualFactor to set
	 */
	public void setManualFactor(double manualFactor) {
		this.manualFactor = manualFactor;
	}

	/**
	 * @return the manualFactor
	 */
	public double getManualFactor() {
		return manualFactor;
	}

	/**
	 * @param automaticNorm
	 *            the automaticNorm to set
	 */
	public void setAutomaticNorm(double automaticNorm) {
		this.automaticNorm = automaticNorm;
	}

	/**
	 * @return the total factor to apply to the lines of the contributor
	 */
	double getTotalFactor() {
		return (getManualFactor() / this.automaticNorm);
	}

	@Override
	public String getContributorName() {
		if (this.contributor == null) {
			return null;
		}
		return this.contributor.getName();
	}
}