package cern.accsoft.steering.aloha.meas.data.align;

public interface AlignmentValue {

	/**
	 * @return the name of the element, which this alignment-value corresponds
	 *         to
	 */
	public String getElementName();

	/**
	 * @return the type of this value (E/S)
	 */
	public AlignmentValueType getType();

	/**
	 * @return the deltaX
	 */
	public abstract double getDeltaX();

	/**
	 * @return the deltaY
	 */
	public abstract double getDeltaY();

	/**
	 * @return the deltaS
	 */
	public abstract double getS();

	/**
	 * @param deltaX
	 *            the deltaX to set
	 */
	public void setDeltaX(double deltaX);

	/**
	 * @param deltaY
	 *            the deltaY to set
	 */
	public void setDeltaY(double deltaY);

	/**
	 * @param s
	 *            the s to set
	 */
	public void setS(Double s);

	/**
	 * @param deltaS
	 *            the deltaS to set
	 */
	public void setDeltaS(double deltaS);

	/**
	 * @return the deltaS
	 */
	public double getDeltaS();

	/**
	 * @param deltaTilt
	 *            the deltaTilt to set
	 */
	public void setDeltaTilt(double deltaTilt);

	/**
	 * @return the deltaTilt
	 */
	public double getDeltaTilt();

}