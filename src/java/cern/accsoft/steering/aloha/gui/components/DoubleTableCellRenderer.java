/**
 * 
 */
package cern.accsoft.steering.aloha.gui.components;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import cern.accsoft.steering.aloha.app.Preferences;

public class DoubleTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private Preferences preferences = null;

	public DoubleTableCellRenderer(Preferences preferences) {
		super();
		this.preferences = preferences;
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	public void setValue(Object value) {
		setText((value == null) ? "" : getPreferences().getNumberFormat()
				.format(value));
	}

	public Preferences getPreferences() {
		return preferences;
	}

}