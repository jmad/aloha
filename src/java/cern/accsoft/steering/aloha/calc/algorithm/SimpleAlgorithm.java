package cern.accsoft.steering.aloha.calc.algorithm;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.plugin.ssolve.solve.SimpleSolver;

/**
 * This is the implementation of an iterative algorithm which ...
 * 
 * TODO describe the algorithm.
 * 
 * @author kfuchsbe
 * 
 */
public class SimpleAlgorithm extends AbstractAlgorithm<SimpleSolver> implements
		Algorithm {

	/**
	 * just performs one step of calculation.
	 */
	public Matrix calc() throws CalculatorException {
		/*
		 * for the moment just returns an empty matrix
		 * 
		 * TODO fill with meaningful code
		 */
		return new Matrix(getVariationData().getVariationParameters().size(), 1);
	}

	@Override
	protected void doReset() {
		/*
		 * TODO insert specific behaviour to reset the algorithm here, if
		 * necessary.
		 */
	}

}
