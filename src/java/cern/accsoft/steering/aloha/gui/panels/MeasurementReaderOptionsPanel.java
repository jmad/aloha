/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import cern.accsoft.steering.aloha.read.MeasurementReaderOptions;
import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.gui.panels.Applyable;
import cern.accsoft.steering.util.gui.panels.Titleable;

/**
 * This panel is used to query options from the user which are required when
 * importing data files
 * 
 * @author kfuchsbe
 * 
 */
public class MeasurementReaderOptionsPanel extends JPanel implements Titleable,
		Applyable {
	private static final long serialVersionUID = 1L;

	/** The options which we want to edit */
	private MeasurementReaderOptions measurementReaderOptions;

	/** The combo box for selecting the beam number */
	private JComboBox cboBeamNumbers;

	/**
	 * the constructor
	 * 
	 * @param options
	 *            the options to edit
	 */
	public MeasurementReaderOptionsPanel(MeasurementReaderOptions options) {
		this.measurementReaderOptions = options;
		initComponenets();
	}

	/**
	 * create all the gui components
	 */
	private void initComponenets() {
		setLayout(new BorderLayout());
		cboBeamNumbers = new JComboBox(BeamNumber.values());
		cboBeamNumbers.setSelectedItem(getMeasurementReaderOptions()
				.getBeamNumber());
		add(cboBeamNumbers, BorderLayout.CENTER);
	}

	@Override
	public String getTitle() {
		return "Reader options";
	}

	@Override
	public boolean apply() {
		getMeasurementReaderOptions().setBeamNumber(
				(BeamNumber) cboBeamNumbers.getSelectedItem());
		return true;
	}

	@Override
	public void cancel() {
		/* nothing to do. Leave everything as it is */
	}

	private MeasurementReaderOptions getMeasurementReaderOptions() {
		return measurementReaderOptions;
	}

}
