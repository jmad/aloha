package cern.accsoft.steering.aloha.plugin.traj.read.yasp;

import java.io.File;

import cern.accsoft.steering.aloha.util.io.AbstractNameList;

/**
 * this class represents a file with a list of files and the given filename.
 * 
 * @author kfuchsbe
 * 
 */
public class YaspStabilityList extends AbstractNameList {
	/** the filename has to be like this! */
	public final static String STABILITY_LIST_FILENAME = "stability.list";

	/**
	 * the constructor, which needs a file, where to read from.
	 * 
	 * @param file
	 */
	public YaspStabilityList(File file) {
		super(file);
	}
 
	@Override
	protected String getAllowedFileName() {
		return STABILITY_LIST_FILENAME;
	}

}
