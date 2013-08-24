package cern.accsoft.steering.aloha.plugin.xxx.sensitivity;

import org.apache.log4j.Logger;

import cern.accsoft.steering.jmad.domain.optics.Optic;
import cern.accsoft.steering.jmad.domain.optics.OpticPoint;
import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;

/**
 * represents a constraint between two elements in the optics. It has a value
 * and a target value. This is used then in the Ti8OpticsConstraintsContributor
 * to implement phase constraints between two elements.
 * 
 * @author kaifox
 * 
 */
public class ElementsOpticsDiffConstraint implements OpticsConstraint {

	/** The logger for the class */
	private final static Logger LOGGER = Logger.getLogger(ElementsOpticsDiffConstraint.class);

	/** the name of the first element for the diff */
	private String firstElementName;

	/** the name of the second element for the diff */
	private String secondElementName;

	/** the twissVariable for which to calc the diff */
	private MadxTwissVariable twissVariable;

	/** the value which should be reached by the fit */
	private double targetValue;

	/**
	 * the constructor to set all the values
	 * 
	 * @param firstElementName
	 * @param secondElementName
	 * @param twissVariable
	 * @param targetValue
	 */
	public ElementsOpticsDiffConstraint(String firstElementName, String secondElementName,
			MadxTwissVariable twissVariable, double targetValue) {
		super();
		this.firstElementName = firstElementName;
		this.secondElementName = secondElementName;
		this.twissVariable = twissVariable;
		this.targetValue = targetValue;
	}

	/**
	 * calculates the difference of the madx-variable between the two points.
	 * 
	 * @param optic
	 *            the optics for which to calculate the value
	 * @return the calculated difference
	 */
	@Override
	public double calcValue(Optic optic) {
		OpticPoint firstPoint = optic.getPointByName(this.firstElementName);
		OpticPoint secondPoint = optic.getPointByName(this.secondElementName);
		if (firstPoint == null || secondPoint == null) {
			LOGGER.error("At least one of the elements '" + this.firstElementName + "' or '" + this.secondElementName
					+ "' could not be found in the optics.");
			return 0;
		}

		Double firstValue = firstPoint.getValue(this.twissVariable);
		Double secondValue = secondPoint.getValue(this.twissVariable);
		if (firstValue == null || secondValue == null) {
			LOGGER.error("At least one of the two values could not be retrieved from the optics point.");
			return 0;
		}

		double value = secondValue - firstValue;
		LOGGER.debug("Diff for '" + this.twissVariable + "' between '" + secondElementName + "' and '"
				+ firstElementName + "' is: " + value + "(" + (value * 360) + " deg.); should be: " + this.targetValue
				+ "(" + (this.targetValue * 360) + " deg.).");
		return value;
	}

	@Override
	public double getTargetValue() {
		return this.targetValue;
	}
}
