/**
 * 
 */
package cern.accsoft.steering.aloha.calc;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.algorithm.Algorithm;

/**
 * This class keeps track of all available {@link Algorithm}s
 * 
 * @author tbaer
 * 
 */
public interface Calculator {

	/**
	 * @param listener
	 *            the listener to add
	 */
	public abstract void addListener(CalculatorListener listener);

	/**
	 * @param listener
	 *            the listener to remove
	 */
	public abstract void removeListener(CalculatorListener listener);

	/**
	 * reset the calculation
	 * 
	 * @throws CalculatorException
	 */
	public abstract void reset() throws CalculatorException;

	/**
	 * performs one step of calculation
	 * 
	 * @throws CalculatorException
	 */
	public abstract void calc() throws CalculatorException;

	/**
	 * @return the change of the parameters during the last calculation
	 */
	public Matrix getDeltaParameterValues();

}
