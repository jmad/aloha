package cern.accsoft.steering.aloha.util.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.util.io.TextFileParser;
import cern.accsoft.steering.util.io.TextFileParserException;
import cern.accsoft.steering.util.io.impl.TextFileParserImpl;

/**
 * this class represents a file which contains a list of names. It provides some
 * simple methods to easily access these names.
 * 
 * @author kfuchsbe
 * 
 */
public abstract class AbstractNameList {
	/** the actual filename of the list */
	private File file = null;

	/** here the actual names of the correctors are stored */
	private List<String> names = new ArrayList<String>();

	/**
	 * @param file
	 *            the filename of the list
	 */
	public AbstractNameList(File file) {
		this.file = file;
	}

	/**
	 * has to be overwritten by subclass to check for a valid Name.
	 * 
	 * @return the name which is valid for this list.
	 */
	protected abstract String getAllowedFileName();

	/**
	 * parses the file given in the Constructor and interprets each line as
	 * name. Correct naming convention is not verified!
	 * 
	 * @throws NameListException
	 */
	public void parse() throws NameListException {
		if (file == null) {
			throw new NameListException("file must not be null!");
		}

		if (!file.getName().endsWith(getAllowedFileName())) {
			throw new NameListException("file '" + file.getName()
					+ "' has not the correct name! (should be '"
					+ getAllowedFileName() + "");
		}

		TextFileParser parser = new TextFileParserImpl();
		try {
			names = parser.parse(file);
		} catch (TextFileParserException e) {
			throw new NameListException("Failed to parse file '"
					+ file.getAbsolutePath() + "'.", e);
		}
	}

	/**
	 * @return a list of names.
	 */
	public List<String> getNames() {
		return names;
	}
}
