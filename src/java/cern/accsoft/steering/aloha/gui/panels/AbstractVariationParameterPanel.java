package cern.accsoft.steering.aloha.gui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.calc.variation.VariationDataListener;
import cern.accsoft.steering.aloha.calc.variation.VariationParameter;
import cern.accsoft.steering.aloha.export.latex.TableModelLatexTextExporter;
import cern.accsoft.steering.aloha.gui.components.DoubleTableCellRenderer;
import cern.accsoft.steering.util.gui.dialog.SimplePanelDialog;
import cern.accsoft.steering.util.gui.table.TableModelSelectionAdapter;

public abstract class AbstractVariationParameterPanel extends JPanel {
    private VariationParametersTableModel parametersTableModel = new VariationParametersTableModel();

    private VariationData variationData = null;

    private JTable table = null;

    private SimplePanelDialog exportDialog = null;

    private Preferences preferences = null;

    /**
     * the selection-adapter keeps track of all selected items and returns the indizes of the table model
     */
    private TableModelSelectionAdapter selectionAdapter = null;

    /**
     * init method used by spring
     */
    public void init() {
        initComponents();
    }

    /**
     * create all components
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        table = new JTable(parametersTableModel);
        table.setDefaultRenderer(Double.class, new DoubleTableCellRenderer(getPreferences()));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setAutoCreateRowSorter(true);
        selectionAdapter = new TableModelSelectionAdapter(table);

        JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        TextExporterPanel exportPanel = new TextExporterPanel();
        TableModelLatexTextExporter exporter = new TableModelLatexTextExporter(parametersTableModel);

        /*
         * what columns to export, when exporting
         */
        exporter.addColumnIndex(VariationParametersTableModel.COL_NAME);
        exporter.addColumnIndex(VariationParametersTableModel.COL_ACTUAL_VALUE);
        exporter.addColumnIndex(VariationParametersTableModel.COL_ERROR);
        exportPanel.setTextExporter(exporter);

        exportDialog = new SimplePanelDialog();
        exportDialog.setPanel(exportPanel);
        exportDialog.pack();

        JPanel btnPanel = new JPanel(new GridBagLayout());
        add(btnPanel, BorderLayout.SOUTH);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        btnPanel.add(new JButton(createRemoveAction()), constraints);
        constraints.gridx++;
        btnPanel.add(new JButton(createExportToLatexAction()), constraints);

        List<Action> additionalActions = getAdditionalButtonActions();
        for (Action action : additionalActions) {
            constraints.gridx++;
            btnPanel.add(new JButton(action), constraints);
        }
    }

    protected VariationData getVariationData() {
        return this.variationData;
    }

    /**
     * method used to inject the variation-data
     * 
     * @param variationData the variation-data to use
     */
    public void setVariationData(VariationData variationData) {
        this.variationData = variationData;
        variationData.addListener(new VariationDataListener() {
            @Override
            public void changedVariationParameters() {
                parametersTableModel.fireTableDataChanged();
            }
        });
        parametersTableModel.fireTableDataChanged();
    }

    /**
     * to be overriden
     * 
     * @return the variation parameters of interest
     */
    protected abstract List<VariationParameter> getVariationParameters();

    /**
     * @return actions for additional buttons
     */
    protected abstract List<Action> getAdditionalButtonActions();

    private class VariationParametersTableModel extends AbstractTableModel {
        private final static int COLUMN_COUNT = 9;

        private final static int COL_NAME = 0;
        private final static int COL_DELTA = 1;
        private final static int COL_CHANGE_FACTOR = 2;
        private final static int COL_INITIAL_VALUE = 3;
        private final static int COL_SENSITIVITY = 4;
        private final static int COL_ACTUAL_VALUE = 5;
        private final static int COL_DIFFERENCE = 6;
        private final static int COL_REL_DIFFERENCE = 7;
        private final static int COL_ERROR = 8;

        private boolean enabledRefresh = true;

        public void removeRow(int row) {
            if ((row >= 0) && (row < getRowCount())) {
                VariationParameter parameter = getVariationParameters().get(row);
                variationData.removeVariationParameter(parameter.getKey());
                if (this.enabledRefresh) {
                    fireTableDataChanged();
                }
            }
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public int getRowCount() {
            return getVariationParameters().size();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if ((col == COL_ACTUAL_VALUE) || (col == COL_DELTA) || (col == COL_CHANGE_FACTOR)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Object getValueAt(int row, int col) {
            VariationParameter parameter = getVariationParameters().get(row);
            switch (col) {
            case COL_NAME:
                return parameter.getName();
            case COL_DELTA:
                return parameter.getDelta();
            case COL_CHANGE_FACTOR:
                return parameter.getUpdateFactor();
            case COL_SENSITIVITY:
                if (parameter.getSensitivity() != 0) {
                    return parameter.getSensitivity();
                } else {
                    return null;
                }
            case COL_INITIAL_VALUE:
                return parameter.getActiveMeasurementInitialValue();
            case COL_ACTUAL_VALUE:
                return parameter.getActiveMeasurementAbsoluteValue();
            case COL_DIFFERENCE:
                return parameter.getOffsetChange();
            case COL_REL_DIFFERENCE:
                return parameter.getActiveMeasurementRelativeChange();
            case COL_ERROR:
                return parameter.getError();
            default:
                return null;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            VariationParameter parameter = getVariationParameters().get(row);
            switch (col) {
            case COL_CHANGE_FACTOR:
                parameter.setUpdateFactor((Double) value);
                fireTableCellUpdated(row, col);
                break;
            case COL_ACTUAL_VALUE:
                parameter.setActualOffset((Double) value);
                fireTableCellUpdated(row, col);
                break;
            case COL_DELTA:
                parameter.setDelta((Double) value);
                fireTableCellUpdated(row, col);
                break;
            }
        }

        @Override
        public Class<?> getColumnClass(int col) {
            switch (col) {
            case COL_NAME:
                return String.class;
            case COL_DELTA:
                return Double.class;
            case COL_CHANGE_FACTOR:
                return Double.class;
            case COL_SENSITIVITY:
                return Double.class;
            case COL_INITIAL_VALUE:
                return Double.class;
            case COL_ACTUAL_VALUE:
                return Double.class;
            case COL_DIFFERENCE:
                return Double.class;
            case COL_REL_DIFFERENCE:
                return Double.class;
            case COL_ERROR:
                return Double.class;
            default:
                return null;
            }
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
            case COL_NAME:
                return "parameter";
            case COL_DELTA:
                return "variation delta";
            case COL_CHANGE_FACTOR:
                return "change factor";
            case COL_SENSITIVITY:
                return "sensitivity";
            case COL_INITIAL_VALUE:
                return "initial value";
            case COL_ACTUAL_VALUE:
                return "fitted value";
            case COL_DIFFERENCE:
                return "difference (actual-initial)";
            case COL_REL_DIFFERENCE:
                return "relative difference";
            case COL_ERROR:
                return "error";
            default:
                return null;
            }
        }

    }

    private Action createRemoveAction() {
        Action removeAction = new AbstractAction("Remove") {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO multiple removal. be careful!
                Integer index = selectionAdapter.getSelectedRowIndex();
                if (index == null) {
                    return;
                }
                parametersTableModel.removeRow(index);
            }
        };
        removeAction.putValue(Action.SHORT_DESCRIPTION, "Remove selected variation Parameter.");
        return removeAction;
    }

    private Action createExportToLatexAction() {
        Action exportAction = new AbstractAction(">> Latex") {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent evt) {
                exportDialog.setVisible(true);
            }
        };
        exportAction.putValue(Action.SHORT_DESCRIPTION,
                "Opens a dialog to create a latex string, which represents a table in Latex");
        return exportAction;
    }

    protected TableModelSelectionAdapter getSelectionAdapter() {
        return this.selectionAdapter;
    }

    protected String getSelectedParameterKey() {
        Integer index = getSelectionAdapter().getSelectedRowIndex();
        if (index == null) {
            return null;
        }
        if ((index >= 0) && (index < getVariationParameters().size())) {
            VariationParameter parameter = getVariationParameters().get(index);
            return parameter.getKey();
        } else {
            return null;
        }
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    private Preferences getPreferences() {
        return preferences;
    }
}
