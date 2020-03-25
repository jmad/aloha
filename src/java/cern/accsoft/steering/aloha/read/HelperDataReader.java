/**
 * 
 */
package cern.accsoft.steering.aloha.read;

import cern.accsoft.steering.aloha.meas.data.HelperData;
import cern.accsoft.steering.util.meas.read.ReaderException;

import java.io.File;
import java.util.List;

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
	 * @param files
	 *            the files to read the data from
	 * @return the data
	 * @throws ReaderException
	 */
	public T read(List<File> files) throws ReaderException;
}
