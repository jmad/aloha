package cern.accsoft.steering.aloha.machine;

import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.meas.data.Plane;

public class Monitor extends AbstractMachineElement {

    public Monitor(String name, Plane plane, BeamNumber beam) {
        super(name, plane, beam);
    }
}
