package cern.accsoft.steering.aloha.calc.algorithm;

import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.bean.aware.SensitivityMatrixManagerAware;
import cern.accsoft.steering.aloha.bean.aware.VariationDataAware;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;
import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;

public abstract class AbstractAlgorithm<T extends Solver> implements Algorithm,
		VariationDataAware, MachineElementsManagerAware,
		SensitivityMatrixManagerAware {

	/** The data containing all the variation parameters */
	private VariationData variationData;

	/** the class keeping track of all activated Machine elements */
	private MachineElementsManager machineElementsManager;

	/** The solver to use for minimizing the errors */
	private T solver;

	/**
	 * the class which takes care of creating the sensitivity-matrix and applying
	 * the calculated values,
	 */
	private SensitivityMatrixManager sensitivityMatrixManager;

	@Override
	public final void reset() {
		getVariationData().reset();
		getMachineElementsManager().resetAllGains();
	}

	/**
	 * this method must be overridden by the subclass. Here some specific
	 * internal reste-procedures can be placed.
	 */
	protected abstract void doReset();

	@Override
	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
	}

	protected MachineElementsManager getMachineElementsManager() {
		return machineElementsManager;
	}

	/**
	 * @param variationData
	 *            the variationData to set
	 */
	@Override
	public void setVariationData(VariationData variationData) {
		this.variationData = variationData;
	}

	/**
	 * @return the variationData
	 */
	protected VariationData getVariationData() {
		return variationData;
	}

	/**
	 * @return the solver
	 */
	protected T getSolver() {
		return this.solver;
	}

	public void setSolver(T solver) {
		this.solver = solver;
	}

	/**
	 * @param sensitivityMatrixManager
	 *            the sensitivityMatrixManager to set
	 */
	@Override
	public void setSensitivityMatrixManager(
			SensitivityMatrixManager sensitivityMatrixManager) {
		this.sensitivityMatrixManager = sensitivityMatrixManager;
	}

	/**
	 * @return the sensitivityMatrixManager
	 */
	protected SensitivityMatrixManager getSensitivityMatrixManager() {
		return sensitivityMatrixManager;
	}

}