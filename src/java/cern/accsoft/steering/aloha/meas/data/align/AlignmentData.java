/*
 * $Id: AlignmentData.java,v 1.3 2009-01-19 17:13:40 kfuchsbe Exp $
 * 
 * $Date: 2009-01-19 17:13:40 $ 
 * $Revision: 1.3 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.meas.data.align;

import cern.accsoft.steering.aloha.meas.data.HelperData;

import java.util.List;

/**
 * This interface represents alignment data. It always contains all values. Not
 * filtered
 * 
 * @author kfuchsbe
 * 
 */
public interface AlignmentData extends HelperData {

	/**
	 * @return all element-names for which there is alignment-data available.
	 */
	public List<String> getElementNames();

	/**
	 * @param elementName
	 *            the element for which to retrieve an alignment value
	 * @param type
	 *            the type of alignment-value to retrieve.
	 * @return the AlignmentValue
	 */
	public AlignmentValue getAlignmentValue(String elementName,
			AlignmentValueType type);

	/**
	 * @return all alignment values
	 */
	public List<AlignmentValue> getAlignmentValues();

	/**
	 * @param value
	 *            the alignmentValues to set
	 */
	public void addAlignmentValue(AlignmentValue value);
}
