/*
 * $Id: YaspKickResponseDataSource.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
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

import cern.accsoft.steering.aloha.proj.data.KickResponseDataSource;

/**
 * kick response-data source for yasp-measurement
 * 
 * @author kfuchsbe
 * 
 */
@Root(name = "yasp-kick-response-data")
public class YaspKickResponseDataSource implements KickResponseDataSource {

	/** the name of the directory */
	@Element(name = "dir-name")
	private String dirName;

	/** the number of the measurement we will load */
	@Element(name = "measurement-number")
	private int measurementNumber = 1;

	/**
	 * @param dirName
	 *            the dirName to set
	 */
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	/**
	 * @return the dirName
	 */
	public String getDirName() {
		return dirName;
	}

	/**
	 * @param measurementNumber the measurementNumber to set
	 */
	public void setMeasurementNumber(int measurementNumber) {
		this.measurementNumber = measurementNumber;
	}

	/**
	 * @return the measurementNumber
	 */
	public int getMeasurementNumber() {
		return measurementNumber;
	}
}
