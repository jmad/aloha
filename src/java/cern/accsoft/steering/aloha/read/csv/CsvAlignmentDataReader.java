/*
 * $Id: CsvAlignmentDataReader.java,v 1.2 2009-01-19 17:13:41 kfuchsbe Exp $
 * 
 * $Date: 2009-01-19 17:13:41 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.read.csv;

import cern.accsoft.steering.aloha.meas.data.align.AlignmentData;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentDataImpl;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentValue;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentValueImpl;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentValueType;
import cern.accsoft.steering.aloha.read.AlignmentDataReader;
import cern.accsoft.steering.util.meas.read.ReaderException;
import com.csvreader.CsvReader;
import org.apache.log4j.Logger;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * This class reads alignment data from a csv file.
 * 
 * @author kfuchsbe
 * 
 */
public class CsvAlignmentDataReader implements AlignmentDataReader {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(CsvAlignmentDataReader.class);

	/** the separator, which is used to compose element-name and number */
	private final static String ELEMENT_NAME_SEPARATOR = ".";

	/** the type of data, which we are expecting to read. */
	private AlignmentDataType alignmentDataType;

	/**
	 * the file-filter for alignment data
	 */
	private FileFilter alignmentDataFileFilter = new FileFilter() {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			} else {
				return CsvAlignmentDataReader.this.isHandling(f);
			}
		}

		@Override
		public String getDescription() {
			return CsvAlignmentDataReader.this.getDescription();
		}
	};

	/*
	 * the mapping of the file-columns to destinct values
	 */

	/* header indizes */
	private final static int CSV_INDEX_HEADER = 0;
	private final static int CSV_LINE_INDEX_HEADER = 0;
	private final static int CSV_LAST_HEADER_LINE = 2;
	private final static String CSV_KEYWORD_ALESAGE = "alesage";
	private final static String CSV_KEYWORD_FAISCEAU = "faisceau";

	/* common columns */
	private final static int CSV_INDEX_NAME = 0;
	private final static int CSV_INDEX_NUMBER = 1;
	private final static int CSV_INDEX_POINT_TYPE = 2;

	/* pts faisceau */
	private final static int CSV_FAISCEAU_INDEX_S_POS = 3;
	private final static int CSV_FAISCEAU_RADIAL_ERROR = 4;
	private final static int CSV_FAISCEAU_VERTICAL_ERROR = 8;

	/* pts alesage */
	private final static int CSV_ALESAGE_S_ERROR = 8;
	private final static int CSV_ALESAGE_TILT_ERROR = 16;

	/**
	 * defines the type of csv-file, which we will are expecting to read.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	public enum AlignmentDataType {
		PTS_FAISCEAU, PTS_ALESAGE;
	}

	@Override
	public AlignmentData read(List<File> files) throws ReaderException {
		if (files.size() == 1) {
			return readAlignmentData(files.get(0), null);
		} else {
			throw new ReaderException("Got " + files.size()
					+ " files to read, but can only handle exactly 1 file.");
		}
	}

	/**
	 * performs the actual read procedure
	 * 
	 * @param file
	 *            the file from where to read
	 * @param data
	 *            the {@link AlignmentData} to add the newly read data to. if
	 *            Null, then a new data is created.
	 * @return the {@link AlignmentData}
	 * @throws CsvReaderException
	 *             if reading fails
	 */
	private AlignmentData readAlignmentData(File file, AlignmentData data)
			throws CsvReaderException {

		if (data == null) {
			data = new AlignmentDataImpl();
		}

		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			throw new CsvReaderException("Could not find file '"
					+ file.getAbsolutePath() + "'.", e);
		}
		CsvReader reader = new CsvReader(fileReader);

		try {
			/* read all lines */
			String[] line;
			int lineIndex = 0;
			while (reader.readRecord()) {
				line = reader.getValues();
				if (lineIndex == CSV_LINE_INDEX_HEADER) {
					determineAlignmentDataType(line);
				}
				if (lineIndex > CSV_LAST_HEADER_LINE) {
					AlignmentValue value = getAlignmentValue(data, line);
					fillAlignmentValue(value, line);
				}
				lineIndex++;
			}
		} catch (IOException e) {
			throw new CsvReaderException(
					"Error while reading line from csv-file.", e);
		}

		return data;
	}

	/**
	 * determine the type of the file from the first line
	 * 
	 * @param line
	 *            the line to parse
	 * @throws CsvReaderException
	 */
	private void determineAlignmentDataType(String[] line)
			throws CsvReaderException {
		String headerString = line[CSV_INDEX_HEADER];
		if (headerString.contains(CSV_KEYWORD_ALESAGE)) {
			this.alignmentDataType = AlignmentDataType.PTS_ALESAGE;
		} else if (headerString.contains(CSV_KEYWORD_FAISCEAU)) {
			this.alignmentDataType = AlignmentDataType.PTS_FAISCEAU;
		} else {
			throw new CsvReaderException(
					"Could not determine type of file from header line '"
							+ headerString + "'.");
		}
	}

	/**
	 * returns a new alignment value to the data, if none exists for the given
	 * line. If there exists one, then the found one is returned.
	 * 
	 * @param data
	 *            the {@link AlignmentData} in which to look for values, or to
	 *            append the new one to.
	 * @param line
	 *            the read line from the file
	 * @return the found or created {@link AlignmentValue}
	 */
	private AlignmentValue getAlignmentValue(AlignmentData data, String[] line) {
		String elementName = line[CSV_INDEX_NAME].trim().toUpperCase()
				+ ELEMENT_NAME_SEPARATOR + line[CSV_INDEX_NUMBER];
		AlignmentValueType type = AlignmentValueType
				.fromTag(line[CSV_INDEX_POINT_TYPE]);

		if (type == null) {
			logger.warn("Could not determine value-type from tag '"
					+ line[CSV_INDEX_POINT_TYPE] + "'. Ignoring this value.");
			return null;
		}

		/* check if the value already exists */
		AlignmentValue value = data.getAlignmentValue(elementName, type);

		/* if it does not exist, then create a new one */
		if (value == null) {
			value = new AlignmentValueImpl(elementName, type);
			data.addAlignmentValue(value);
		}

		return value;
	}

	/**
	 * creates a new alignment-value from a line of the csv-file
	 * 
	 * @param line
	 *            the line from which to create the alignment-values
	 * @return the {@link AlignmentValue}
	 */
	private void fillAlignmentValue(AlignmentValue value, String[] line) {
		if (AlignmentDataType.PTS_FAISCEAU.equals(this.alignmentDataType)) {
			value.setS(convertToDouble(line[CSV_FAISCEAU_INDEX_S_POS]));
			value.setDeltaX(convertToDouble(line[CSV_FAISCEAU_RADIAL_ERROR]));
			value.setDeltaY(convertToDouble(line[CSV_FAISCEAU_VERTICAL_ERROR]));

		} else if (AlignmentDataType.PTS_ALESAGE.equals(this.alignmentDataType)) {
			value.setDeltaS(convertToDouble(line[CSV_ALESAGE_S_ERROR]));
			value.setDeltaTilt(convertToDouble(line[CSV_ALESAGE_TILT_ERROR]));
		}
	}

	/**
	 * converts the string to a double and ignores the errors
	 * 
	 * @param string
	 *            the String to convert
	 * @return the double-value
	 */
	private double convertToDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			logger
					.warn(
							"Could not correctly parse a value to double. Ignoring this value.",
							e);
			return 0.0;
		}
	}

	@Override
	public String toString() {
		return getDescription();
	}

	@Override
	public String getDescription() {
		return "CSV - alignment file";
	}

	@Override
	public FileFilter getFileFilter() {
		return alignmentDataFileFilter;
	}

	@Override
	public boolean isHandling(List<File> files) {
		if (files.size() == 1) {
			return isHandling(files.get(0));
		} else {
			return false;
		}
	}

	private boolean isHandling(File file) {
		return file.getName().toUpperCase().endsWith((".CSV"));
	}

}
