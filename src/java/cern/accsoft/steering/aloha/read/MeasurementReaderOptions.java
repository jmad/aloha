/**
 * 
 */
package cern.accsoft.steering.aloha.read;

import cern.accsoft.steering.util.acc.BeamNumber;

/**
 * This class holds the options which can be given when reading a measurement.
 * 
 * @author kfuchsbe
 * 
 */
public class MeasurementReaderOptions {

	/** The beam number to use when reading from yasp files */
	private BeamNumber beamNumber = BeamNumber.BEAM_1;

	public void setBeamNumber(BeamNumber beamNumber) {
		this.beamNumber = beamNumber;
	}

	public BeamNumber getBeamNumber() {
		return beamNumber;
	}
}
