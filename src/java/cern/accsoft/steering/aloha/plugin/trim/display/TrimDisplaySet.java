package cern.accsoft.steering.aloha.plugin.trim.display;

import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.plugin.trim.meas.TrimMeasurement;
import cern.jdve.viewer.DVView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TrimDisplaySet implements DisplaySet {

	/* no views for the moment */
	private List<DVView> views = new ArrayList<DVView>();

	private JPanel optionPanel = null;

	public TrimDisplaySet(TrimMeasurement measurement) {
		super();
		this.optionPanel = new TrimDataPanel(measurement);
	}

	@Override
	public JPanel getDetailPanel() {
		return optionPanel;
	}

	@Override
	public List<DVView> getDvViews() {
		return this.views;
	}

	@Override
	public void refresh() {
		/* Nothing to do for the moment */
	}

}
