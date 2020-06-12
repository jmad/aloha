package cern.accsoft.steering.util.meas.data.yasp;

import cern.accsoft.steering.util.meas.data.AbstractDataValue;
import cern.accsoft.steering.util.meas.data.Status;

public class CorrectorValue extends AbstractDataValue {

    public double kick = 0; // in microrad
    public String strengthName = null;
    public double rtKick = 0;

    @Override
    public Status getStatus() {
        return Status.OK;
    }

}
