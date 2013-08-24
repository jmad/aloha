/**
 * 
 */
package cern.accsoft.steering.aloha.read;

import java.io.File;
import java.util.List;

import cern.accsoft.steering.aloha.meas.data.HelperData;
import cern.accsoft.steering.util.meas.read.ReaderException;

/**
 * This is the interface of any class that reads data into aloha
 * 
 * @author kfuchsbe
 * 
 */
public interface HelperDataReader<T extends HelperData> extends Reader {

	/**
	 * read and return the data from a given file
	 * 
	 * @param file
	 *            the file to read the data from
	 * @return the data
	 * @throws ReaderException
	 */
	public T read(List<File> files) throws ReaderException;
}
