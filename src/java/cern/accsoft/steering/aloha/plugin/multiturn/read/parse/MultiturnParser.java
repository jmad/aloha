/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.read.parse;

import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnData;

import java.io.File;

/**
 * this is the interface for a parser for Multiturn data. Since the data is
 * split over two files it has two methods, one to create a new data - object
 * and one to add data to an already existing object.
 * 
 * @author kfuchsbe
 * 
 */
public interface MultiturnParser {

	/**
	 * creates a new Multiturn data object from the data parsed from the file.
	 * 
	 * @param file
	 *            the file to parse
	 * @return a newly created multiturn object
	 * @throws MultiturnParserException
	 *             if something goes wrong
	 */
	public MultiturnData parse(File file) throws MultiturnParserException;

	/**
	 * parses the file and adds the containig data to the given data object
	 * 
	 * @param file
	 *            the file to parse
	 * @param data
	 *            the data to which to append the parsed data
	 * @throws MultiturnParserException
	 */
	public void parse(File file, MultiturnData data)
			throws MultiturnParserException;

}
