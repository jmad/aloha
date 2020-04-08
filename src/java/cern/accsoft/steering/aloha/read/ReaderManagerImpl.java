/**
 * 
 */
package cern.accsoft.steering.aloha.read;

import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the implementation of the {@link ReaderManager}. For the moment all
 * the readers are just injected by spring
 * 
 * @author kfuchsbe
 * 
 */
public class ReaderManagerImpl implements ReaderManager {

	/** all the available readers */
	private List<Reader> readers = new ArrayList<Reader>();

	@Override
	public List<Reader> getReaders() {
		return this.readers;
	}

	/**
	 * to inject the readers
	 * 
	 * @param readers
	 */
	public void setReaders(List<Reader> readers) {
		this.readers = readers;
	}

	@Override
	public void addReaders(ReaderProvider readerProvider) {
		this.readers.addAll(readerProvider.getReaders());
	}

}
