/**
 *
 */
package cern.accsoft.steering.aloha.read;

import java.io.File;
import java.net.URI;
import java.util.List;

import cern.accsoft.steering.aloha.meas.ModelAwareMeasurement;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.util.meas.read.ReaderException;

/**
 * This is the interface for a reader, that directly returns a
 * {@link ModelAwareMeasurement}
 *
 * @author kfuchsbe
 */
public interface MeasurementReader<T extends ModelAwareMeasurement> extends Reader {

    /**
     * reads the file and returns a measurement that uses the model defined by
     * the given model-delegate
     *
     * @param files         the file to read
     * @param modelDelegate the model-delegate to use for the measurement.
     * @param options       the options for reading the files
     * @return the measurement
     * @throws ReaderException
     */
    T read(List<File> files, ModelDelegate modelDelegate, MeasurementReaderOptions options) throws ReaderException;

    /**
     * has to tell, if this measurement reader requires options. This is useful
     * to decide in the gui, if the user has to be asked for them or not.
     *
     * @return true if options are required, false if not
     */
    boolean requiresOptions();

    /**
     * The proposed JMad model definition URI. Null if this reader does not propose a particular JMad model and the
     * user should choose.
     * @return the proposed model URI, if any
     */
    URI proposedModelDefinitionUri(List<File> files);
}
