package cern.accsoft.steering.aloha.gui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cern.accsoft.steering.aloha.meas.MeasurementManager.ModelDelegateInstance;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;
import cern.accsoft.steering.aloha.model.ModelDelegateManagerListener;
import cern.accsoft.steering.jmad.model.manage.JMadModelManager;
import cern.accsoft.steering.util.gui.CompUtils;
import cern.accsoft.steering.util.gui.panels.TableFilterPanel;
import cern.accsoft.steering.util.gui.table.SelectionSetTableModel;
import cern.accsoft.steering.util.gui.table.TableModelSelectionAdapter;

/**
 * this class represents a panel, which enables the display of model-delegates.
 * 
 * @author kfuchsbe
 * 
 */
public class ModelDelegatesPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/** the manager, which provides all the loaded model */
	private ModelDelegateManager modelDelegateManager;

	/** the table models for the elements-table and for the element-table. */
	private ModelDelegatesTableModel tableModel = null;

	private ModelDelegatesSelectionAdapter selectionAdapter = null;

	private JMadModelManager modelManager = null;

	private ModelDelegateManagerListener modelDelegateManagerListener = new ModelDelegateManagerListener() {

		@Override
		public void addedModelDelegate(ModelDelegate newModelDelegate) {
			tableModel.fireTableDataChanged();
		}

		@Override
		public void removedModelDelegate(ModelDelegate removedModelDelegate) {
			tableModel.fireTableDataChanged();
		}
	};

	/**
	 * the constructor
	 */
	public ModelDelegatesPanel() {
		super(new BorderLayout());
		initComponents();
	}

	/**
	 * create all containing Components
	 */
	private final void initComponents() {
		JTable table;

		/*
		 * the panel for the overall list
		 */
		JPanel listPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.BOTH;

		tableModel = new ModelDelegatesTableModel();
		table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.selectionAdapter = new ModelDelegatesSelectionAdapter(table);
		table.getSelectionModel().addListSelectionListener(
				this.selectionAdapter);
		tableModel
				.setTableModelSelectionAdapter(new TableModelSelectionAdapter(
						table));
		JScrollPane elementsScrollPane = CompUtils.createScrollPane(table);

		/*
		 * the filter for the measurements-table
		 */
		listPanel.add(new TableFilterPanel(table), constraints);

		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = 2;
		listPanel.add(elementsScrollPane, constraints);

		add(listPanel, BorderLayout.CENTER);

	}

	/**
	 * This class is the implementation of a listener to change the selected
	 * elements in the elements-table.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private class ModelDelegatesSelectionAdapter implements
			ListSelectionListener {
		private JTable table = null;

		public ModelDelegatesSelectionAdapter(JTable table) {
			this.table = table;
		}

		@Override
		public void valueChanged(ListSelectionEvent event) {
			if ((event.getSource() == table.getSelectionModel())
					&& (!event.getValueIsAdjusting())) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					setCurrentModelDelegateInstance(getSelectedIndex());
				}
			}
		}

		public int getSelectedIndex() {
			int index = table.getSelectedRow();
			if (index >= 0) {
				return table.convertRowIndexToModel(index);
			} else {
				return -1;
			}
		}

	}

	/**
	 * This class is the table model for the table of available elements.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private class ModelDelegatesTableModel extends SelectionSetTableModel {
		private static final long serialVersionUID = -1585264195650278990L;

		private final static int COLUMN_COUNT = 1;

		private final static int COL_NAME = 0;

		@Override
		public int getColumnCount() {
			return COLUMN_COUNT;
		}

		@Override
		public int getRowCount() {
			if (getModelDelegateManager() == null) {
				return 0;
			}
			return getModelDelegateManager().getModelDelegateInstances().size();
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			return;
		}

		@Override
		public Object getValueAt(int row, int col) {
			if (getModelDelegateManager() == null) {
				return null;
			}

			ModelDelegateInstance instance = getModelDelegateManager()
					.getModelDelegateInstances().get(row);
			switch (col) {
			case COL_NAME:
				return instance.toString();
			default:
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int col) {
			switch (col) {
			case COL_NAME:
				return String.class;
			default:
				return null;
			}
		}

		@Override
		public String getColumnName(int col) {
			switch (col) {
			case COL_NAME:
				return "name";
			default:
				return null;
			}
		}
	}

	/**
	 * sets the the element of the given index as the active one.
	 * 
	 * @param index
	 */
	private void setCurrentModelDelegateInstance(int index) {
		if ((index >= 0) && (getModelDelegateManager() != null)) {
			List<ModelDelegateInstance> instances = getModelDelegateManager()
					.getModelDelegateInstances();
			if (index < instances.size()) {

				ModelDelegateInstance instance = instances.get(index);
				getModelManager().setActiveModel(
						instance.getModelDelegate().getJMadModel());
			}
		}
	}

	public void setModelDelegateManager(
			ModelDelegateManager modelDelegateManager) {
		this.modelDelegateManager = modelDelegateManager;
		modelDelegateManager.addListener(this.modelDelegateManagerListener);
	}

	public ModelDelegateManager getModelDelegateManager() {
		return modelDelegateManager;
	}

	public void setModelManager(JMadModelManager modelManager) {
		this.modelManager = modelManager;
	}

	private JMadModelManager getModelManager() {
		return modelManager;
	}

}
