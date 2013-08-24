/*
 * $Id: Solver.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.solve;

/**
 * This interface is a generic solver for minimization for aloha.
 * 
 * A solver is responsible for the calculation of the parameter-change between
 * two iterations.
 * 
 * @author kfuchsbe
 * 
 */
public interface Solver {

	/**
	 * the name of this solver.
	 * 
	 * @return the name of the solver
	 */
	public String getName();

}
