package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;

public interface SensitivityMatrixManagerAware extends BeanAware {
    void setSensitivityMatrixManager(SensitivityMatrixManager sensitivityMatrixManager);
}
