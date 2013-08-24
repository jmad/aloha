/**
 * 
 */
package cern.accsoft.steering.aloha.gui.display;

import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;

/**
 * This is the interface of a class that keeps track of all displaySets.
 * 
 * @author kfuchsbe
 * 
 */
public interface DisplaySetManager {

	/**
	 * add a {@link DisplaySetFactory} to the manager.
	 * 
	 * @param factory
	 */
	public void addDisplaySetFactory(DisplaySetFactory factory);

	/**
	 * creates a displayset for the given measurement if necessary and sets it
	 * as active one. The displaySet is then also returned.
	 * 
	 * @param measurement
	 *            the measurement for which to find/create a {@link DisplaySet}
	 * @return the {@link DisplaySet}
	 */
	public DisplaySet display(Measurement measurement);

	/**
	 * sets the given {@link DisplaySet} as the active one.
	 * 
	 * @param displaySet
	 *            the {@link DisplaySet} to set as active
	 */
	public void setActiveDisplaySet(DisplaySet displaySet);

	/**
	 * @return the active {@link DisplaySet}
	 */
	public DisplaySet getActiveDisplaySet();

	/**
	 * add a listener to the {@link DisplaySetManager}
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addListener(DisplaySetManagerListener listener);

	/**
	 * removes a listener from the {@link DisplaySetManager}
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(DisplaySetManagerListener listener);

}
