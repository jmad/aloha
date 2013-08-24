/*
 * $Id: SolverManager.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.solve;

import java.util.List;

/**
 * This interface provides methods to retrieve all available solvers and get and
 * set the active solver.
 * 
 * 
 * @author kfuchsbe
 * 
 */
public interface SolverManager {

	/**
	 * @return a list of all available solvers
	 */
	public List<Solver> getSolvers();

	/**
	 * set the given Solver as the active one.
	 * 
	 * @param solver
	 *            the solver to set as active.
	 */
	public void setActiveSolver(Solver solver);

	/**
	 * @return the actually active solver.
	 */
	public Solver getActiveSolver();

	public void addSolvers(List<Solver> solvers);

}
