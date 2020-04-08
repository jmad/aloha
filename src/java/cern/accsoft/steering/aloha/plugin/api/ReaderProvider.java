/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.api;

import cern.accsoft.steering.aloha.read.Reader;

import java.util.List;

/**
 * This interface is intended to be implemented by an {@link AlohaPlugin} if it
 * provides one or more readers.
 * 
 * @author kfuchsbe
 * 
 */
public interface ReaderProvider extends AlohaPlugin {

	/**
	 * This has to return a list, which contains readers for certain
	 * measurements.
	 * 
	 * @return a list of {@link Reader}s
	 */
	public List<Reader> getReaders();
}
