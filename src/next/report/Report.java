package cern.accsoft.steering.aloha.report;

import java.io.InputStream;

/**
 * the enum, which determines, which report to load.
 * 
 * @author Kajetan
 * 
 */
public enum Report {
	FIT_RESULTS("FitResults.jasper"),
	FIT_IMAGES("FitImages.jasper");

	/** the path-prefix, where to find the jasper files */
	private final static String PATH_PREFIX = "data/";
	
	/** the filename of the resource, which we want to display */
	private String fileName;

	/**
	 * constructor of the enum enforces to give a fileName
	 * 
	 * @param fileName
	 */
	private Report(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return the path to the jasper file.
	 */
	public InputStream getReport() {
		return (Report.class.getResourceAsStream(PATH_PREFIX + this.fileName));
	}

}