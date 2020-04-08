/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.kickresp.display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.MeasurementManagerAware;
import cern.accsoft.steering.aloha.meas.BuiltinMeasurementType;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.meas.MeasurementManager;
import cern.accsoft.steering.aloha.meas.MeasurementManagerListener;
import cern.accsoft.steering.aloha.meas.ModelAwareMeasurement;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurement;
import cern.accsoft.steering.aloha.plugin.traj.meas.TrajectoryMeasurementImpl;

/**
 * a panel with several options for the kick-response measurement
 * 
 * @author kfuchsbe
 * 
 */
public class KickResponseOptionsPanel extends JPanel implements
		MeasurementManagerAware {
	private static final long serialVersionUID = 2604108585593501737L;

	/** the measurement manager to get the trajectory measurements */
	private MeasurementManager measurementManager;

	/** The kick-response measurement to take care of */
	private KickResponseMeasurement kickResponseMeasurment;

	private JComboBox comboBox;

	private MeasurementManagerListener managerListener = new MeasurementManagerListener() {

		@Override
		public void addedMeasurement(Measurement newMeasurement) {
			fillTrajectoryMeasurementComboBox();
		}

		@Override
		public void changedActiveMeasurement(Measurement activeMeasurement) {
			/* not interested in this */
		}

		@Override
		public void removedMeasurement(Measurement removedMeasurement) {
			fillTrajectoryMeasurementComboBox();
		}
	};

	private ActionListener comboBoxListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object newStability = comboBox.getSelectedItem();
			if ((newStability != null)
					&& (newStability instanceof TrajectoryMeasurement)) {
				getKickResponseMeasurment().setStabilityMeasurement(
						(TrajectoryMeasurementImpl) newStability);
			}
		}

	};

	public KickResponseOptionsPanel(KickResponseMeasurement measurement) {
		this.kickResponseMeasurment = measurement;
	}

	@InitMethod
	public void init() {
		initComponents();
		fillTrajectoryMeasurementComboBox();
	}

	private void initComponents() {
		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;

		add(new JLabel("Use as stability:"), constraints);

		constraints.gridy++;
		this.comboBox = new JComboBox();
		comboBox
				.setToolTipText("Select a trajectory measurement (if some are loaded) which shall be used as stability data.");
		add(comboBox, constraints);
	}

	@Override
	public void setMeasurementManager(MeasurementManager measurementManager) {
		this.measurementManager = measurementManager;
		this.measurementManager.addListener(this.managerListener);

	}

	private void fillTrajectoryMeasurementComboBox() {
		comboBox.removeActionListener(this.comboBoxListener);
		comboBox.removeAllItems();

		List<ModelAwareMeasurement> trajMeasurements = this.measurementManager
				.getMeasurements(BuiltinMeasurementType.TRAJECTORY);
		for (Measurement measurement : trajMeasurements) {
			comboBox.addItem(measurement);
		}

		TrajectoryMeasurement actStability = getKickResponseMeasurment()
				.getStabilityMeasurement();
		if ((actStability != null) && (trajMeasurements.contains(actStability))) {
			comboBox.setSelectedItem(actStability);
		} else if (trajMeasurements.size() > 0) {
			Measurement newStability = trajMeasurements.get(0);
			if ((newStability != null)
					&& (newStability instanceof TrajectoryMeasurement)) {
				getKickResponseMeasurment().setStabilityMeasurement(
						(TrajectoryMeasurementImpl) newStability);
				comboBox.setSelectedItem(newStability);
			}
		}
		comboBox.addActionListener(this.comboBoxListener);
	}

	private KickResponseMeasurement getKickResponseMeasurment() {
		return kickResponseMeasurment;
	}

}
