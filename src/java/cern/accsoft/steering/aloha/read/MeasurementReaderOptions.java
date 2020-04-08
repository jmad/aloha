/**
 * 
 */
package cern.accsoft.steering.aloha.read;

import cern.accsoft.steering.util.acc.BeamNumber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class holds the options which can be given when reading a measurement.
 * 
 * @author kfuchsbe
 */
public class MeasurementReaderOptions {

    /** The beam number to use when reading from yasp files */
    private BeamNumber beamNumber = BeamNumber.BEAM_1;

    public void setBeamNumber(BeamNumber beamNumber) {
        this.beamNumber = checkNotNull(beamNumber, "beamNumber must not be null");
    }

    public BeamNumber getBeamNumber() {
        return beamNumber;
    }

}
