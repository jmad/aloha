/**
 * 
 */
package cern.accsoft.steering.aloha.read;

import cern.accsoft.steering.aloha.meas.ModelAwareMeasurement;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.util.meas.read.ReaderException;

import java.io.File;
import java.util.List;

/**
 * This is the interface for a reader, that directly returns a
 * {@link ModelAwareMeasurement}
 * 
 * @author kfuchsbe
 * 
 */
public interface MeasurementReader<T extends ModelAwareMeasurement> extends Reader {

	/**
	 * reads the file and returns a measurement that uses the model defined by
	 * the given model-delegate
	 * 
	 * @param files
	 *            the file to read
	 * @param modelDelegate
	 *            the model-delegate to use for the measurement.
	 * @param options
	 *            the options for reading the files
	 * @return the measurement
	 * @throws ReaderException
	 */
	public T read(List<File> files, ModelDelegate modelDelegate,
			MeasurementReaderOptions options) throws ReaderException;

	/**
	 * has to tell, if this measurement reader requires options. This is useful
	 * to decide in the gui, if the user has to be asked for them or not.
	 * 
	 * @return true if options are required, false if not
	 */
	public boolean requiresOptions();

}
