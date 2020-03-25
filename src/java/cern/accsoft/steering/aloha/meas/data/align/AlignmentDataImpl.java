/*
 * $Id: AlignmentDataImpl.java,v 1.4 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.4 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.meas.data.align;

import cern.accsoft.steering.aloha.meas.data.HelperDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation of the alignment data
 * 
 * @author kfuchsbe
 * 
 */
public class AlignmentDataImpl implements AlignmentData {

	/** the names of the elements, containing each name only once. */
	List<String> elementNames = new ArrayList<String>();

	/** the indizes in the alignment-values list */
	Map<String, Integer> elementIndizes = new HashMap<String, Integer>();

	/** the alignment values, which were read e.g. from a file. */
	List<AlignmentValue> alignmentValues = new ArrayList<AlignmentValue>();

	/**
	 * @param value
	 *            the alignmentValues to set
	 */
	public void addAlignmentValue(AlignmentValue value) {
		if (value == null) {
			return;
		}

		this.elementNames.add(value.getElementName());
		this.alignmentValues.add(value);
		this.elementIndizes.put(createMapKey(value), this.alignmentValues
				.size() - 1);
	}

	@Override
	public AlignmentValue getAlignmentValue(String elementName,
			AlignmentValueType type) {
		Integer index = elementIndizes.get(createMapKey(elementName, type));
		if (index == null) {
			return null;
		}
		return this.alignmentValues.get(index);
	}

	@Override
	public List<String> getElementNames() {
		return this.elementNames;
	}

	/**
	 * creates a key, which is used to store the values in the map
	 * 
	 * @param value
	 *            the value for which to create a key
	 * @return the key
	 */
	String createMapKey(AlignmentValue value) {
		return createMapKey(value.getElementName(), value.getType());
	}

	/**
	 * create a key, which is used to store/retrieve values into/from the map
	 * 
	 * @param elementName
	 *            the name of the element
	 * @param type
	 *            the type of the value
	 * @return the key
	 */
	private String createMapKey(String elementName, AlignmentValueType type) {
		return elementName.toUpperCase() + "-" + type;
	}

	@Override
	public List<AlignmentValue> getAlignmentValues() {
		return this.alignmentValues;
	}

	@Override
	public HelperDataType getType() {
		return HelperDataType.ALIGNMENT;
	}

}
