/**
 * 
 */
package cern.accsoft.steering.aloha.gui.display;

import cern.jdve.viewer.DVView;

import javax.swing.*;
import java.util.List;

/**
 * A {@link DisplaySet} defines swing components to show in the aloha swing gui
 * for a specific type of measurement.
 * 
 * @author kfuchsbe
 * 
 */
public interface DisplaySet {

	/**
	 * this method has to return the panel which is shown in the gui to display
	 * details of the measurment.
	 * 
	 * @return an appropriate panel
	 */
	public JPanel getDetailPanel();

	/**
	 * this method shall return all views, that shall be displayed in the
	 * dataviewer.
	 * 
	 * @return a list of views
	 */
	public List<DVView> getDvViews();

	/**
	 * refresh all the displayed values.
	 */
	void refresh();

}
