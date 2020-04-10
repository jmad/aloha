/**
 * 
 */
package cern.accsoft.steering.aloha.calc.sensitivity;

/**
 * The interface of a listener to a sensitivityMatrixManager
 * 
 * @author kfuchsbe
 * 
 */
public interface SensitivityMatrixManagerListener {

	/**
	 * fired, when the amount of contributors change.
	 */
	public void changedContributors();
}
