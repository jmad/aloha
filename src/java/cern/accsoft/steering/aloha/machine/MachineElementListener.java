/*
 * $Id: MachineElementListener.java,v 1.1 2008-09-08 17:29:32 kfuchsbe Exp $
 * 
 * $Date: 2008-09-08 17:29:32 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.machine;

/**
 * Methods for listening to an element
 * 
 * @author kfuchsbe
 * 
 */
public interface MachineElementListener {

	/**
	 * fired, when the element changes its active-state
	 * 
	 * @param element
	 *            The element which changed its state
	 */
	public void changedActiveState(AbstractMachineElement element);

	/**
	 * fired, when the gain of the element is changed
	 * 
	 * @param element
	 *            the element whose gain was changed
	 */
	public void changedGain(AbstractMachineElement element);

}
