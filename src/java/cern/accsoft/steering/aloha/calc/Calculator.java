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
	void addListener(CalculatorListener listener);

	/**
	 * @param listener
	 *            the listener to remove
	 */
	void removeListener(CalculatorListener listener);

	/**
	 * reset the calculation
	 * 
	 * @throws CalculatorException
	 */
	void reset() throws CalculatorException;

	/**
	 * performs one step of calculation
	 * 
	 * @throws CalculatorException
	 */
	void calc() throws CalculatorException;

	/**
	 * @return the change of the parameters during the last calculation
	 */
	Matrix getDeltaParameterValues();

}
