package cern.accsoft.steering.util.meas.data.yasp;

import cern.accsoft.steering.util.meas.data.AbstractDataValue;
import cern.accsoft.steering.util.meas.data.Status;

public class MonitorValue extends AbstractDataValue {
	public final static int STATUS_OK = 0;

	// * NAME PLANE BEAM POS RMS SUM HW-STATUS STATUS STATUS-TAG
	private double beamPosition; // in microns
	public double rms;
	public double sum;
	public int status;
	public int hwStatus;

	public boolean isOk() {
		return (status == STATUS_OK);
	}

	@Override
	public Status getStatus() {
		if (status == STATUS_OK) {
			return Status.OK;
		} else {
			return Status.NOT_OK;
		}
	}

	public void setBeamPosition(double beamPosition) {
		this.beamPosition = beamPosition;
	}

	public double getBeamPosition() {
		return beamPosition;
	}

}
