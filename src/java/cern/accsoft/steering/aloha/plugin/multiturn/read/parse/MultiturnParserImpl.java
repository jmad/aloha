/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.read.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDataImpl;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDataValue;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDataValueImpl;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnVar;
import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.io.TextFileParser;
import cern.accsoft.steering.util.io.TextFileParserException;
import cern.accsoft.steering.util.io.impl.TextFileParserImpl;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * This is the implementation of the parser for multiturn-files
 * 
 * @author kfuchsbe
 * 
 */
public class MultiturnParserImpl implements MultiturnParser {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(MultiturnParserImpl.class);

	/** the char which marks a header line */
	private final static String HEADER_TAG = "#";

	/** the regexpression to split the lines */
	private final static String SPLIT_REGEX = "[ ]+";

	/** the string which marks the beam entry in the header */
	private final static String TAG_BEAM = "beam";

	/** the string which marks the plane entry in the header */
	private final static String TAG_PLANE = "plane";

	/** the parser used to load the lines of the file */
	private TextFileParser textParser = new TextFileParserImpl();

	/** the parser used to parse the text-file */

	@Override
	public MultiturnData parse(File file) throws MultiturnParserException {
		MultiturnDataImpl data = new MultiturnDataImpl();
		readData(file, data);
		return data;
	}

	@Override
	public void parse(File file, MultiturnData data)
			throws MultiturnParserException {
		if (data instanceof MultiturnDataImpl) {
			readData(file, (MultiturnDataImpl) data);
		} else {
			throw new MultiturnParserException(
					"Data was not an instance of MultiturnDataImpl. Could not add data.");
		}

	}

	private void readData(File file, MultiturnDataImpl data)
			throws MultiturnParserException {

		List<String> lines;
		try {
			lines = this.textParser.parse(file);
		} catch (TextFileParserException e) {
			throw new MultiturnParserException(
					"Error while parsing multiturn-file '"
							+ file.getAbsolutePath() + "'.");
		}

		/* all the header lines as strings */
		List<String> headerLines = new ArrayList<String>();

		/*
		 * this will be filled in the loop when reaching the first non-header
		 * line.
		 */
		Map<Integer, MultiturnVar> columns = new HashMap<Integer, MultiturnVar>();

		/* the already parsed data values */
		List<MultiturnDataValue> dataValues = new ArrayList<MultiturnDataValue>();

		/* the plane and beam which will be assigned to the values. */
		Plane plane = null;
		BeamNumber beamNumber = null;

		for (String line : lines) {
			logger.trace("reading line:'" + line + "'.");
			if (line.startsWith(HEADER_TAG)) {
				/*
				 * we remove the hash in the beginning and store the headerlines
				 * for further use.
				 */
				line = line.substring(1);
				headerLines.add(line);

				/*
				 * further we are interested in beam and plane:
				 */
				String[] values = line.toLowerCase().split(SPLIT_REGEX);
				logger.trace("splitted line to '" + Arrays.toString(values));
				if (values[0].startsWith(TAG_BEAM.toLowerCase())) {
					beamNumber = BeamNumber.fromInt(Integer
							.parseInt(values[values.length - 1]));
					logger.debug("Beam in file is '" + beamNumber.toString()
							+ "'.");
				} else if (values[0].startsWith(TAG_PLANE.toLowerCase())) {
					plane = Plane.fromInt(Integer
							.parseInt(values[values.length - 1]));
					logger
							.debug("Plane in file is '" + plane.toString()
									+ "'.");
				}
				continue;
			}

			/*
			 * The last line of the header represents the columns, so we have to
			 * parse it when we reach the first non-header line.
			 */
			if (dataValues.size() == 0) {
				columns = parseColumnHeader(headerLines
						.get(headerLines.size() - 1));
				/*
				 * also beam and plane must not be null anymore!
				 */
				if ((beamNumber == null) || (plane == null)) {
					throw new MultiturnParserException(
							"Beam or plane could not be determined from the header. Cannot read the data!");
				}
			}

			/*
			 * if no header line then we have to extract the values.
			 */
			MultiturnDataValueImpl dataValue = new MultiturnDataValueImpl();
			dataValue.setBeamNumber(beamNumber);
			dataValue.setPlane(plane);
			String[] strValues = line.split(SPLIT_REGEX);

			for (int i = 0; i < strValues.length; i++) {
				String value = strValues[i];
				MultiturnVar column = columns.get(i);
				if (column == null) {
					continue;
				}
				column.setValue(dataValue, value);
			}

			if (dataValue.getName() == null) {
				throw new MultiturnParserException(
						"Name for datapoints could not be found. Are there no BPM names in the data?");
			}

			dataValues.add(dataValue);
		}

		/*
		 * finally we add them all to the data
		 */
		data.addDataValues(dataValues, plane);
	}

	/**
	 * parses the given string and returns a map which contains the indizes of
	 * the possible columns as keys and the available columns as values.
	 * 
	 * @param line
	 *            the line to parse
	 * @return the map with indizes as keys and the Columns as values.
	 */
	private Map<Integer, MultiturnVar> parseColumnHeader(String line) {
		Map<Integer, MultiturnVar> columns = new HashMap<Integer, MultiturnVar>();
		String[] colnames = line.split(SPLIT_REGEX);
		for (int i = 0; i < colnames.length; i++) {
			String name = colnames[i];
			MultiturnVar column = MultiturnVar.fromTag(name);
			if (column != MultiturnVar.UNKNOWN) {
				columns.put(i, column);
			}
		}
		return columns;
	}
}
