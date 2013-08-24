/**
 * 
 */
package cern.accsoft.steering.aloha.read;

import java.util.List;

import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;

/**
 * This is the interface of a class, that keeps track of all available readers.
 * 
 * @author kfuchsbe
 * 
 */
public interface ReaderManager {

	/**
	 * @return returns all the readers that are available
	 */
	public List<Reader> getReaders();

	/**
	 * add all the readers provided by the given {@link ReaderProvider} to the
	 * list of available readers.
	 * 
	 * @param readerProvider
	 */
	public void addReaders(ReaderProvider readerProvider);
}
