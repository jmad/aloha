/**
 * 
 */
package cern.accsoft.steering.aloha.gui.display;

/**
 * @author kfuchsbe
 * 
 */
public interface DisplaySetManagerListener {

	/**
	 * fired, when the {@link DisplaySet} changed.
	 * 
	 * NOTE: The {@link DisplaySet}s can also be null.
	 * 
	 * @param oldDisplaySet
	 *            the {@link DisplaySet} which was active before the change
	 * @param newDisplaySet
	 *            the {@link DisplaySet} which is active now (after the change).
	 */
	public void changedDisplaySet(DisplaySet oldDisplaySet,
			DisplaySet newDisplaySet);
}
