/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cern.accsoft.steering.aloha.meas.MeasurementManager.ModelDelegateInstance;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;
import cern.accsoft.steering.util.gui.panels.Applyable;
import cern.accsoft.steering.util.gui.panels.Titleable;

/**
 * This class displays a panel, which allows the user to select, if a new
 * instance of a model (more exact: a {@link ModelDelegate}) shall be created
 * for a measurement, or if the same instance than for another measurement shall
 * be used.
 * 
 * If the measurements were taken at different settings of the machine (e.g.
 * different initial conditions like deltap) then one has to choose new
 * instances here. If the conditions in the machine were the same, then one
 * should choose the same instance for all measurements, since otherwise all the
 * calculations have to be repeated in all instances, which is slower at the
 * moment. (This general rules might change when multithreading models will be
 * implemented ...)
 * 
 * @author kfuchsbe
 * 
 */
public class ModelInstanceSelectionPanel extends JPanel implements Titleable,
		Applyable {
	private static final long serialVersionUID = -2831375766173814136L;

	/** The manager which keeps track of all the loaded models. */
	private ModelDelegateManager modelDelegateManager;

	/** the selected (or created) modelDelegate */
	private ModelDelegate selectedModelDelegate;

	/**
	 * the combobox, which allows to select the model-instance, if the same as
	 * in another measurement shall be used.
	 */
	private JComboBox cboModelDelegateInstances;

	/**
	 * this button is true, if an old instance shall be reused.
	 */
	private JRadioButton btnSameInstance;

	/**
	 * this button is true, if a new instance shall be created.
	 */
	private JRadioButton btnNewInstance;

	/**
	 * This property defines if a new instance of the model shall be created, or
	 * not.
	 */
	private boolean newInstance = false;

	/**
	 * the default constructor.
	 */
	public ModelInstanceSelectionPanel() {
		/* just prepare the components */
		initComponents();
	}

	/**
	 * initialize the panel
	 */
	public void init() {
		/* show the actual data */
		showData();
	}

	/**
	 * create all the content
	 */
	private void initComponents() {
		setLayout(new GridBagLayout());
		JLabel label = new JLabel(
				"<html>"
						+ "Choose which model-instance to use for the measurement.<br><br>"
						+ "If the measurements were taken at different settings of the machine<br>"
						+ "(e.g. different initial conditions like deltap) then you have to<br>"
						+ "choose 'new instance' here. If the conditions in the machine were<br>"
						+ "the same then you should choose the same instance for all measurements<br>"
						+ "since otherwise all the calculations have to be repeated in all instances<br>"
						+ "which is much slower for the time being.<br>"
						+ "</html>");
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(label, constraints);

		constraints.gridy++;
		btnSameInstance = new JRadioButton("use Instance:");
		btnSameInstance.setSelected(true);
		add(btnSameInstance, constraints);

		constraints.gridy++;
		cboModelDelegateInstances = new JComboBox();
		add(cboModelDelegateInstances, constraints);

		constraints.gridy++;
		btnNewInstance = new JRadioButton("new Instance.");
		add(btnNewInstance, constraints);

		ButtonGroup buttonGroub = new ButtonGroup();
		buttonGroub.add(btnSameInstance);
		buttonGroub.add(btnNewInstance);
	}

	/**
	 * fills the combobox and sets the correct label the 'same instance' button.
	 */
	private void showData() {
		Collection<ModelDelegateInstance> instances = this
				.getModelDelegateManager().getModelDelegateInstances();
		for (ModelDelegateInstance instance : instances) {
			this.cboModelDelegateInstances.addItem(instance);
		}
		if (instances.size() > 0) {
			ModelDelegateInstance instance = instances.iterator().next();
			cboModelDelegateInstances.setSelectedItem(instance);
			btnNewInstance.setText("New Instance of Model '"
					+ instance.getModelDelegate().getJMadModel().getName()
					+ "'");
		}
	}

	/**
	 * remove all references
	 */
	public void dispose() {
		this.cboModelDelegateInstances.removeAll();
		this.selectedModelDelegate = null;
	}

	//
	// Methods of interface Titleable
	//

	@Override
	public String getTitle() {
		return "Select Model instance.";
	}

	//
	// Methods of interface Applyable
	//

	@Override
	public boolean apply() {
		this.selectedModelDelegate = ((ModelDelegateInstance) this.cboModelDelegateInstances
				.getSelectedItem()).getModelDelegate();
		this.setNewInstance(this.btnNewInstance.isSelected());
		return true;
	}

	@Override
	public void cancel() {
		dispose();
	}

	//
	// getters and setters
	//

	public ModelDelegate getSelectedModelDelegate() {
		return selectedModelDelegate;
	}

	private void setNewInstance(boolean newInstance) {
		this.newInstance = newInstance;
	}

	public boolean isNewInstance() {
		return newInstance;
	}

	public void setModelDelegateManager(
			ModelDelegateManager modelDelegateManager) {
		this.modelDelegateManager = modelDelegateManager;
	}

	private ModelDelegateManager getModelDelegateManager() {
		return modelDelegateManager;
	}

}
