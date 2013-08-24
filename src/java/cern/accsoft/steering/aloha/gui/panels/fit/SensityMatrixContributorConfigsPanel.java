/*
 * $Id: SensityMatrixContributorConfigsPanel.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.panels.fit;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributorState;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManagerConfig;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManagerListener;

/**
 * @author kfuchsbe
 * 
 */
public class SensityMatrixContributorConfigsPanel extends JPanel {
	private static final long serialVersionUID = -6886648253482998908L;

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(SensityMatrixContributorConfigsPanel.class);

	/** the config which we use to get the contributors */
	private SensitivityMatrixManagerConfig sensityMatrixManagerConfig;

	/** the table model */
	private ContributorConfigsTableModel tableModel;

	/**
	 * init-method for spring
	 */
	public void init() {
		initComponents();
	}

	/**
	 * initialize all components
	 */
	private void initComponents() {
		setLayout(new BorderLayout());
		this.tableModel = new ContributorConfigsTableModel();
		JTable table = new JTable(this.tableModel);
		JScrollPane scrollPane = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * this property sets the configs, which we will display.
	 * 
	 * @param sensityMatrixManagerConfig
	 */
	public void setSensityMatrixManagerConfig(
			SensitivityMatrixManagerConfig sensityMatrixManagerConfig) {
		this.sensityMatrixManagerConfig = sensityMatrixManagerConfig;
		this.sensityMatrixManagerConfig
				.addListener(new SensitivityMatrixManagerListener() {
					@Override
					public void changedContributors() {
						tableModel.fireTableDataChanged();
					}
				});
	}

	/**
	 * @return the actual contributors
	 */
	private List<SensitivityMatrixContributorState> getConfigs() {
		if (this.sensityMatrixManagerConfig != null) {
			return this.sensityMatrixManagerConfig.getContributorConfigs();
		} else {
			return new ArrayList<SensitivityMatrixContributorState>();
		}
	}

	/**
	 * the table model for the configs, which we will display
	 * 
	 * @author kfuchsbe
	 * 
	 */
	private class ContributorConfigsTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 5533864601962021714L;
		/*
		 * the indizes of the columns
		 */
		private final static int COL_INDEX_ACTIVE = 0;
		private final static int COL_INDEX_NAME = 1;
		private final static int COL_INDEX_FACTOR = 2;

		/** the amount of columns */
		private final static int COLUMN_COUNT = 3;

		@Override
		public int getColumnCount() {
			return COLUMN_COUNT;
		}

		@Override
		public int getRowCount() {
			return getConfigs().size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			SensitivityMatrixContributorState config = getConfigs().get(row);
			switch (col) {
			case COL_INDEX_ACTIVE:
				return config.isActive();
			case COL_INDEX_NAME:
				return config.getContributorName();
			case COL_INDEX_FACTOR:
				return config.getManualFactor();
			default:
				logger.warn("unknown column number " + col + "!");
			}
			return null;
		}

		@Override
		public Class<?> getColumnClass(int col) {
			switch (col) {
			case COL_INDEX_ACTIVE:
				return Boolean.class;
			case COL_INDEX_NAME:
				return String.class;
			case COL_INDEX_FACTOR:
				return Double.class;
			default:
				logger.warn("unknown column number " + col + "!");
			}
			return super.getColumnClass(col);
		}

		@Override
		public String getColumnName(int col) {
			switch (col) {
			case COL_INDEX_ACTIVE:
				return "active";
			case COL_INDEX_NAME:
				return "name";
			case COL_INDEX_FACTOR:
				return "factor";
			default:
				logger.warn("unknown column number " + col + "!");
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			switch (col) {
			case COL_INDEX_ACTIVE:
				return true;
			case COL_INDEX_NAME:
				return false;
			case COL_INDEX_FACTOR:
				return true;
			default:
				logger.warn("unknown column number " + col + "!");
			}
			return false;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			SensitivityMatrixContributorState config = getConfigs().get(row);
			switch (col) {
			case COL_INDEX_ACTIVE:
				config.setActive((Boolean) value);
				break;
			case COL_INDEX_NAME:
				/* not editable */
				break;
			case COL_INDEX_FACTOR:
				config.setManualFactor((Double) value);
				break;
			default:
				logger.warn("unknown column number " + col + "!");
				super.setValueAt(value, row, col);
				break;
			}
		}

	}
}
