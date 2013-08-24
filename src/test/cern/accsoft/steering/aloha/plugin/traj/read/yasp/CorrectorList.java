package cern.accsoft.steering.aloha.plugin.traj.read.yasp;

import java.io.File;

import cern.accsoft.steering.aloha.util.io.AbstractNameList;


/**
 * this class provides a method to parse a list of correctors, which
 * is created by the stearing program.
 * 
 * @author kfuchsbe
 *
 */
public class CorrectorList extends AbstractNameList {
	/* the filename has to be like this! */
	public final static String CORRECTOR_LIST_FILENAME = "CORR.LIST";

	public CorrectorList(File file) {
		super(file);
	}
	
	@Override
	protected String getAllowedFileName() {
		return CORRECTOR_LIST_FILENAME;
	}
}
