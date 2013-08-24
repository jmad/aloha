/*
 * $Id: SolverManagerImpl.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.solve;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.calc.solve.matrix.MatrixSolver;

/**
 * This class manages different {@link MatrixSolver}s and keeps track of the
 * active one.
 * 
 * @author kfuchsbe
 * 
 */
public class SolverManagerImpl implements SolverManager {

	/** all available solvers */
	private List<Solver> solvers = new ArrayList<Solver>();

	/** the actually active Solver */
	private Solver activeSolver;

	/**
	 * set the list of available solvers (used by spring)
	 * 
	 * @param solvers
	 *            the solvers to set
	 */
	public void setSolvers(List<Solver> solvers) {
		this.solvers.addAll(solvers);
		if (this.solvers.size() > 0) {
			/* by default we set the first one as active one */
			this.activeSolver = this.solvers.get(0);
		}
	}

	public void addSolver(Solver solver) {
		this.solvers.add(solver);
	}

	@Override
	public void addSolvers(List<Solver> solvers) {
		this.solvers.addAll(solvers);
	}

	//
	// methods of interface SolverManager
	//

	@Override
	public Solver getActiveSolver() {
		return this.activeSolver;
	}

	@Override
	public List<Solver> getSolvers() {
		return this.solvers;
	}

	@Override
	public void setActiveSolver(Solver solver) {
		this.activeSolver = solver;
	}
}
