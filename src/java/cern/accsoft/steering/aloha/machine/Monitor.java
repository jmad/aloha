package cern.accsoft.steering.aloha.machine;

import cern.accsoft.steering.util.meas.data.Plane;

public class Monitor extends AbstractMachineElement {
	public Monitor() {
		super();
	}
	
	public Monitor(String name, Plane plane) {
		this.name = name;
		this.plane = plane;
	}
}
