package cern.accsoft.steering.aloha.gui.panels;

import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManager;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.meas.MeasurementManager;
import cern.accsoft.steering.aloha.meas.MeasurementManagerListener;
import cern.accsoft.steering.util.gui.CompUtils;
import cern.accsoft.steering.util.gui.panels.TableFilterPanel;
import cern.accsoft.steering.util.gui.table.SelectionSetTableModel;
import cern.accsoft.steering.util.gui.table.TableModelSelectionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * this class represents a panel, which enables the display of all measurements.
 *
 * @author kfuchsbe
 */
public class MeasurementsPanel extends JPanel {
    private final static Logger LOGGER = LoggerFactory.getLogger(MeasurementsPanel.class);

    /**
     * the manager, which provides all the loaded measurements
     */
    private MeasurementManager measurementManager;

    private DisplaySetManager displaySetManager;

    /**
     * the table models for the elements-table and for the element-table.
     */
    private MeasurementsTableModel tableModelMeasurements = null;

    private MeasurementsSelectionAdapter selectionAdapter = null;

    /**
     * the constructor
     */
    public MeasurementsPanel() {
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

        tableModelMeasurements = new MeasurementsTableModel();
        table = new JTable(tableModelMeasurements);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.selectionAdapter = new MeasurementsSelectionAdapter(table);
        table.getSelectionModel().addListSelectionListener(
                this.selectionAdapter);
        tableModelMeasurements
                .setTableModelSelectionAdapter(new TableModelSelectionAdapter(
                        table));
        JScrollPane elementsScrollPane = CompUtils.createScrollPane(table);

        /*
         * the filter for the measurements-table
         */
        constraints.gridwidth = 2;
        listPanel.add(new TableFilterPanel(table), constraints);

        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy++;
        listPanel.add(elementsScrollPane, constraints);

        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        JButton btn = new JButton("remove");
        btn.addActionListener(e -> getMeasurementManager().removeMeasurement(
                selectionAdapter.getSelectedMeasurementIndex()));
        listPanel.add(btn, constraints);

        constraints.gridx++;
        btn = new JButton("refresh");
        btn.addActionListener(e -> {
            DisplaySet displaySet = getDisplaySetManager().getActiveDisplaySet();
            if (displaySet != null) {
                displaySet.refresh();
                LOGGER.info("refresh finished.");
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
     */
    private class MeasurementsSelectionAdapter implements ListSelectionListener {
        private JTable table;

        public MeasurementsSelectionAdapter(JTable table) {
            this.table = table;
        }

        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (event.getSource() == table.getSelectionModel()) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    setCurrentMeasurement(getSelectedMeasurementIndex());
                }
            }
        }

        public int getSelectedMeasurementIndex() {
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
     */
    private class MeasurementsTableModel extends SelectionSetTableModel {
        private static final long serialVersionUID = 1L;

        private final static int COLUMN_COUNT = 2;

        private final static int COL_TYPE = 0;
        private final static int COL_NAME = 1;

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public int getRowCount() {
            if (getMeasurementManager() == null) {
                return 0;
            }
            return getMeasurementManager().getMeasurements().size();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
		}

        @Override
        public Object getValueAt(int row, int col) {
            if (getMeasurementManager() == null) {
                return null;
            }

            Measurement measurement = getMeasurementManager().getMeasurements()
                    .get(row);
            switch (col) {
                case COL_NAME:
                    return measurement.getName();
                case COL_TYPE:
                    return measurement.getType().toString();
                default:
                    return null;
            }
        }

        @Override
        public Class<?> getColumnClass(int col) {
            switch (col) {
                case COL_NAME:
                    return String.class;
                case COL_TYPE:
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
    private void setCurrentMeasurement(int index) {
        if ((index >= 0) && (getMeasurementManager() != null)) {
            if (index < getMeasurementManager().getMeasurements().size()) {
                // getMeasurementManager().getActiveMeasurement().removeListener(
                // measurementListener);

                // Measurement measurement = getMeasurementManager()
                // .getMeasurements().get(index);
                // txtMeasurementName.setText(measurement.getName());
                // tableModelKnobOffsets.setMeasurement(measurement);
                // measurement.addListener(measurementListener);
                getMeasurementManager().setActiveMeasurement(index);
                getDisplaySetManager().display(
                        getMeasurementManager().getActiveMeasurement());
            }
        }
    }

    /**
     * used to inject the {@link MeasurementManager}
     *
     * @param measurementManager
     */
    public void setMeasurementManager(MeasurementManager measurementManager) {
        this.measurementManager = measurementManager;
        this.measurementManager.addListener(new MeasurementManagerListener() {

            @Override
            public void changedActiveMeasurement(Measurement activeMeasurement) {
                /* do nothing */
            }

            @Override
            public void addedMeasurement(Measurement newMeasurement) {
                tableModelMeasurements.fireTableDataChanged();
            }

            @Override
            public void removedMeasurement(Measurement removedMeasurement) {
                tableModelMeasurements.fireTableDataChanged();
            }
        });
    }

    public MeasurementManager getMeasurementManager() {
        return measurementManager;
    }

    public void setDisplaySetManager(DisplaySetManager displaySetManager) {
        this.displaySetManager = displaySetManager;
    }

    public DisplaySetManager getDisplaySetManager() {
        return displaySetManager;
    }

}
