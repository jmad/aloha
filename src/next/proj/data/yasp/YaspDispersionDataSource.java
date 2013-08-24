/*
 * $Id: YaspDispersionDataSource.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.proj.data.yasp;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cern.accsoft.steering.aloha.proj.data.DispersionDataSource;

/**
 * the dispersion-data source for yasp-data
 * 
 * @author kfuchsbe
 * 
 */
@Root(name = "yasp-disperion-data")
public class YaspDispersionDataSource implements DispersionDataSource {

	/** the filename, relative to the project-dir */
	@Element(name = "file-name")
	private String fileName;

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

}
