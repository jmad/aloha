package cern.accsoft.steering.aloha.gui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cern.accsoft.steering.aloha.app.HelperDataManager;
import cern.accsoft.steering.aloha.app.HelperDataManagerListener;
import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManager;
import cern.accsoft.steering.aloha.meas.data.HelperDataType;
import cern.accsoft.steering.util.gui.CompUtils;
import cern.accsoft.steering.util.gui.panels.TableFilterPanel;
import cern.accsoft.steering.util.gui.table.SelectionSetTableModel;
import cern.accsoft.steering.util.gui.table.TableModelSelectionAdapter;

/**
 * this class represents a panel, which enables the display of all the
 * HelperData
 * 
 * @author kfuchsbe
 * 
 */
public class HelperDataPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/** the manager, which provides all the loaded measurements */
	private HelperDataManager helperDataManager;

	/** The displaysetManager which we use to add a new displayset. */
	private DisplaySetManager displaySetManager;

	/** the table models for the elements-table and for the element-table. */
	private HelperDataTableModel tableModel = null;

	private MeasurementsSelectionAdapter selectionAdapter = null;

	/**
	 * the constructor
	 */
	public HelperDataPanel() {
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

		tableModel = new HelperDataTableModel();
		table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.selectionAdapter = new MeasurementsSelectionAdapter(table);
		table.getSelectionModel().addListSelectionListener(
				this.selectionAdapter);
		tableModel
				.setTableModelSelectionAdapter(new TableModelSelectionAdapter(
						table));
		JScrollPane scrollPane = CompUtils.createScrollPane(table);

		/*
		 * the filter for the measurements-table
		 */
		constraints.gridwidth = 2;
		listPanel.add(new TableFilterPanel(table), constraints);

		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy++;
		listPanel.add(scrollPane, constraints);

		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = 2;
		JButton btn = new JButton("refresh");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DisplaySet displaySet = getDisplaySetManager()
						.getActiveDisplaySet();
				if (displaySet != null) {
					displaySet.refresh();
				}
			}
		});
		listPanel.add(btn, constraints);

		add(listPanel, BorderLayout.CENTER);

	}

	/**
	 * This class is the implementation of a listener to change the selected
	 * elements in the elements-table.
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private class MeasurementsSelectionAdapter implements ListSelectionListener {
		private JTable table = null;

		public MeasurementsSelectionAdapter(JTable table) {
			this.table = table;
		}

		@Override
		public void valueChanged(ListSelectionEvent event) {
			if (event.getSource() == table.getSelectionModel()) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					setCurrentData(getSelectedDataIndex());
				}
			}
		}

		public int getSelectedDataIndex() {
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
	private class HelperDataTableModel extends SelectionSetTableModel {
		private static final long serialVersionUID = 1L;

		private final static int COLUMN_COUNT = 1;

		private final static int COL_TYPE = 0;

		@Override
		public int getColumnCount() {
			return COLUMN_COUNT;
		}

		@Override
		public int getRowCount() {
			if (getHelperDataManager() == null) {
				return 0;
			}
			return getHelperDataManager().getDataTypes().size();
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
			if (getHelperDataManager() == null) {
				return null;
			}

			HelperDataType type = getHelperDataManager().getDataTypes().get(row);
			switch (col) {
			case COL_TYPE:
				return type.toString();
			default:
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int col) {
			switch (col) {
			case COL_TYPE:
				return String.class;
			default:
				return null;
			}
		}

		@Override
		public String getColumnName(int col) {
			switch (col) {
			case COL_TYPE:
				return "type";
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
	private void setCurrentData(int index) {
		if ((index >= 0) && (getHelperDataManager() != null)) {
			if (index < getHelperDataManager().getDataTypes().size()) {
				HelperDataType dataType = getHelperDataManager().getDataTypes().get(
						index);
				getHelperDataManager().setActiveHelperData(dataType);
			}
		}
	}

	public void setDisplaySetManager(DisplaySetManager displaySetManager) {
		this.displaySetManager = displaySetManager;
	}

	public DisplaySetManager getDisplaySetManager() {
		return displaySetManager;
	}

	public void setHelperDataManager(HelperDataManager helperDataManager) {
		this.helperDataManager = helperDataManager;
		this.helperDataManager = helperDataManager;
		this.helperDataManager.addListener(new HelperDataManagerListener() {

			@Override
			public void putData(HelperDataType dataType) {
				tableModel.fireTableDataChanged();
			}

			@Override
			public void changedActiveHelperData(HelperDataType activeDataType) {
				/* do nothing */
			}
		});
	}

	public HelperDataManager getHelperDataManager() {
		return helperDataManager;
	}

}
