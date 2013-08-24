/*
 * $Id: ArrayUtil.java,v 1.1 2009-01-27 10:17:57 kfuchsbe Exp $
 * 
 * $Date: 2009-01-27 10:17:57 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.util;

import java.util.ArrayList;
import java.util.List;

/**
 * this class contains some static methods to deal with arrays.
 * 
 * @author kfuchsbe
 * 
 */
public class ArrayUtil {

	private ArrayUtil() {
		/* only static methods */
	}

	/**
	 * creates a list containing the defaultValue as each entry.
	 * 
	 * @param <T>
	 * @param capacity
	 * @param defaultValue
	 * @return
	 */
	public final static <T> List<T> createDefaultValueList(int capacity,
			T defaultValue) {
		List<T> list = new ArrayList<T>(capacity);
		for (int i = 0; i < capacity; i++) {
			list.add(defaultValue);
		}
		return list;
	}
 
}
