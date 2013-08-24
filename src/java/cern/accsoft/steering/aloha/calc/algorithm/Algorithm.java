package cern.accsoft.steering.aloha.calc.algorithm;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.CalculatorException;

/**
 * An Algorithm is a class that works together with a solver to minimize the
 * difference between measurement and model.
 * 
 * The calculator defines initializes the calculation and keeps track of the
 * iterations.
 * 
 * @author tbaer
 * 
 */
public interface Algorithm {

	public abstract void reset() throws CalculatorException;

	public abstract Matrix calc() throws CalculatorException;

}