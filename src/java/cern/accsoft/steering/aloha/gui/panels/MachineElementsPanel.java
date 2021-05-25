package cern.accsoft.steering.aloha.gui.panels;

import static cern.accsoft.gui.frame.util.CompletableFutureTasks.backgroundTask;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.gui.components.DoubleTableCellRenderer;
import cern.accsoft.steering.aloha.machine.AbstractMachineElement;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManagerListener;
import cern.accsoft.steering.util.gui.menu.Checkable;
import cern.accsoft.steering.util.gui.menu.MousePopupListener;
import cern.accsoft.steering.util.gui.menu.TablePopupMenu;
import cern.accsoft.steering.util.gui.menu.ValueSetable;
import cern.accsoft.steering.util.gui.panels.Applyable;
import cern.accsoft.steering.util.gui.panels.TableFilterPanel;
import cern.accsoft.steering.util.gui.table.SelectionSetTableModel;
import cern.accsoft.steering.util.gui.table.TableModelSelectionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MachineElementsPanel extends JPanel implements Applyable, TableModelListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MachineElementsPanel.class);

    private final static Dimension PREFERRED_SIZE = new Dimension(200, 400);

    private MachineElementsManager machineElementsManager;

    private static final long serialVersionUID = 1L;

    private boolean dataChanged = false;
    private AbstractMachineElement[] elements;

    private ArrayList<Double> gainBuffer = new ArrayList<>();
    private ArrayList<Boolean> activeBuffer = new ArrayList<>();

    private ElementsTableModel tableModel;
    private JTable table;

    private Preferences preferences;

    private boolean displayGains = false;

    /**
     * the enum used to define, which elements to display.
     * 
     * @author kfuchsbe
     */
    public enum Type {
        CORRECTORS, MONITORS;
    }

    public void init() {
        getMachineElementsManager().addListener(new MachineElementsManagerListener() {
            @Override
            public void changedActiveElements() {
                refresh();
            }

            @Override
            public void changedElements() {
                fill();
            }
        });

        fill();
        initComponents();
    }

    private void fill() {
        this.elements = new AbstractMachineElement[] {};

        if (Type.MONITORS.equals(this.getType())) {
            this.elements = getMachineElementsManager().getAllMonitors().toArray(new AbstractMachineElement[] {});
        } else if (Type.CORRECTORS.equals(this.getType())) {
            this.elements = getMachineElementsManager().getAllCorrectors().toArray(new AbstractMachineElement[] {});
        } else {
            LOGGER.warn("unknown type '" + getType() + "'. Do not know what to do.");
            return;
        }
        refresh();
    }

    private void refresh() {
        gainBuffer.clear();
        activeBuffer.clear();

        for (AbstractMachineElement element : this.elements) {
            activeBuffer.add(element.isActive());
            gainBuffer.add(element.getInitialGain());
        }

        if (this.tableModel != null) {
            this.tableModel.fireTableDataChanged();
        }

        SwingUtilities.invokeLater(this::validate);
    }

    @Override
    public boolean apply() {
        if (dataChanged) {
            backgroundTask("setting active elements", () -> {
                getMachineElementsManager().setSuppressActiveElementsChangedEvent(true);
                for (int i = 0; i < elements.length; i++) {
                    AbstractMachineElement element = elements[i];
                    element.setActive(activeBuffer.get(i));
                    element.setInitialGain(gainBuffer.get(i));
                }
                getMachineElementsManager().setSuppressActiveElementsChangedEvent(false);
                dataChanged = false;
            });
        }
        return true;
    }

    @Override
    public void cancel() {
        /* nothing to do */
    }

    private void initComponents() {
        setPreferredSize(PREFERRED_SIZE);
        setLayout(new BorderLayout());

        tableModel = new ElementsTableModel();
        tableModel.addTableModelListener(this);

        table = new JTable(tableModel);
        table.setDefaultRenderer(Double.class, new DoubleTableCellRenderer(getPreferences()));
        tableModel.setTableModelSelectionAdapter(new TableModelSelectionAdapter(table) {

            @Override
            protected void selectionChanged(Integer selectedRow, List<Integer> selectedRows) {

                if (selectedRow == null) {
                    return;
                }
                if (Type.CORRECTORS.equals(getType())) {
                    Integer index = getMachineElementsManager().convertToActiveCorrectorIndex(selectedRow);
                    if (index != null) {
                        getMachineElementsManager().setActiveCorrectorNumber(index);
                    }
                } else if (Type.MONITORS.equals(getType())) {
                    Integer index = getMachineElementsManager().convertToActiveMonitorIndex(selectedRow);
                    if (index != null) {
                        getMachineElementsManager().setActiveMonitorNumber(index);
                    }

                }

            }
        });
        new MousePopupListener(table, new TablePopupMenu(tableModel));

        dataChanged = false;

        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        table.getColumnModel().getColumn(0).setMaxWidth(30);

        JCheckBox showGainsCheckbox = new JCheckBox("display initial gains");
        showGainsCheckbox.addActionListener(e -> setDisplayGains(showGainsCheckbox.isSelected()));

        add(new TableFilterPanel(table), BorderLayout.NORTH);
        add(scroller, BorderLayout.CENTER);
        add(showGainsCheckbox, BorderLayout.SOUTH);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        dataChanged = true;
    }

    protected MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    protected abstract Type getType();

    public void setDisplayGains(boolean displayGains) {
        this.displayGains = displayGains;
        if (tableModel != null && table != null) {
            tableModel.fireTableStructureChanged();
            table.getColumnModel().getColumn(0).setMaxWidth(30);
            if (displayGains) {
                table.getColumnModel().getColumn(2).setMaxWidth(50);
            }
        }
    }

    public boolean isDisplayGains() {
        return displayGains;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    protected Preferences getPreferences() {
        return preferences;
    }

    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    private class ElementsTableModel extends SelectionSetTableModel implements Checkable, ValueSetable {
        private static final long serialVersionUID = 1L;

        private final static int COLUMN_COUNT = 3;

        private final static int COL_ACTIVE = 0;
        private final static int COL_NAME = 1;
        private final static int COL_GAIN = 2;

        @Override
        public int getColumnCount() {
            if (displayGains) {
                return COLUMN_COUNT;
            } else {
                return COLUMN_COUNT - 1;
            }
        }

        @Override
        public int getRowCount() {
            return elements.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            AbstractMachineElement element = elements[row];
            switch (col) {
            case COL_ACTIVE:
                return activeBuffer.get(row);
            case COL_NAME:
                return element.toString();
            case COL_GAIN:
                return gainBuffer.get(row);
            default:
                return null;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
            case COL_ACTIVE:
                activeBuffer.set(row, (Boolean) value);
                fireTableCellUpdated(row, col);
                break;
            case COL_GAIN:
                gainBuffer.set(row, (Double) value);
                if (isDisplayGains()) {
                    fireTableCellUpdated(row, col);
                }
                break;
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if ((col == COL_ACTIVE) || (col == COL_GAIN)) {
                if (elements[row] instanceof Corrector) {
                    return elements[row].isOk();
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Class<?> getColumnClass(int col) {
            switch (col) {
            case COL_ACTIVE:
                return Boolean.class;
            case COL_NAME:
                return String.class;
            case COL_GAIN:
                return Double.class;
            default:
                return null;
            }
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
            case COL_ACTIVE:
                return "act";
            case COL_NAME:
                return "name";
            case COL_GAIN:
                return "gain";
            default:
                return null;
            }
        }

        @Override
        public void checkAllSelected() {
            setValueSelectedRows(true, COL_ACTIVE);
        }

        @Override
        public void uncheckAllSelected() {
            setValueSelectedRows(false, COL_ACTIVE);
        }

        @Override
        public boolean isCheckUncheckEnabled() {
            return isMultipleRowSetEnabled();
        }

        @Override
        public boolean isValueSetEnabled() {
            return isMultipleRowSetEnabled();
        }

        @Override
        public void setValueAllSelected(Double value) {
            setValueSelectedRows(value, COL_GAIN);
        }

        @Override
        public String getValueName() {
            return "initial gain";
        }

    }

}
