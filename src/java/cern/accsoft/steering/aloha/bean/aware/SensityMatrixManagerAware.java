package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;

public interface SensityMatrixManagerAware extends BeanAware {
	public void setSensityMatrixManager(
			SensitivityMatrixManager sensityMatrixManager);
}
