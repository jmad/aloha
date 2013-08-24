/**
 * 
 */
package cern.accsoft.steering.aloha.calc.solve;

/**
 * this is the abstract class for a solver.
 * 
 * @author kfuchsbe
 * 
 */
public abstract class AbstractSolver implements Solver {

	@Override
	public final String toString() {
		return getName();
	}
}
