/*
 * $Id: AlignmentValueType.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.meas.data.align;

/**
 * this enum represents the different types of values, which are possible for
 * alignment values
 * 
 * @author kfuchsbe
 * 
 */
public enum AlignmentValueType {
	/** (entre) */
	START("E"),

	/** (sortie) */
	END("S");

	/** the tag in the file */
	private String tag;

	/**
	 * the constructor, which needs a tag
	 * 
	 * @param tag
	 */
	private AlignmentValueType(String tag) {
		this.tag = tag;
	}

	/**
	 * used to compare to a string from the file
	 * 
	 * @param string
	 *            the string to compare to
	 * @return true if equal, false otherwise
	 */
	public boolean equals(String string) {
		return this.tag.equalsIgnoreCase(string.trim());
	}

	/**
	 * determines the type from a given string
	 * 
	 * @param tag
	 *            the tag from which to determine the type
	 * @return the type
	 */
	public final static AlignmentValueType fromTag(String tag) {
		for (AlignmentValueType type : AlignmentValueType.values()) {
			if (type.equals(tag)) {
				return type;
			}
		}
		return null;
	}

}
