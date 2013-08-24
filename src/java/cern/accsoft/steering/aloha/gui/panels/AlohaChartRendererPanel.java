/*
 * $Id: AlohaChartRendererPanel.java,v 1.7 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.7 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManager;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManagerListener;
import cern.accsoft.steering.util.gui.dv.ds.AbstractJmadDataSet;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart.ChartRendererRole;
import cern.accsoft.steering.util.gui.dv.ds.Aloha2DChart.RendererType;
import cern.accsoft.steering.util.gui.panels.Titleable;
import cern.jdve.Chart;
import cern.jdve.ChartRenderer;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.viewer.DVView;
import cern.jdve.viewer.DataView;
import cern.jdve.viewer.DataViewer;

/**
 * this class represents a panel, on which some options for AlohaCharts are displayed. Basically we provide
 * possibilities to switch on/off certain renderers and to change the type of renderers for certain dataSets. This is
 * still not very beautiful, since there at least should be some checks, if the selected renderer is compatible with the
 * dataSet it should render ..
 * 
 * @author kfuchsbe
 */
public class AlohaChartRendererPanel extends JPanel implements DataViewsManager, Titleable {
    private static final long serialVersionUID = -4027681139311807434L;

    /** the preferred size of the table-scrollpane */
    private final static Dimension PREFERRED_TABLE_SIZE = new Dimension(400, 150);

    /** the preferred size for a boolean column */
    private final static int PREFERRED_WIDTH_BOOLEAN = 30;

    /** the currently selected Chart */
    private Chart chart = null;

    /** the tableModel, which provides the data */
    private RendererTableModel tableModel = new RendererTableModel();

    /** the table, where we display the data */
    private JTable table = new JTable(tableModel);

    /** the combo-box to switch on/off the display of the statistics - values */
    private JCheckBox chkStatistics;

    /** the combo-box to switch on/off the labels on the x-axis */
    private JCheckBox chkLabelsVisible;

    /** the combo box to switch on/off the visibility of the h/v indicators */
    private JCheckBox chkHVIndicatorsVisible;

    /**
     * the listener, which we add to all the DvViews to get notified, when they are activated.
     */
    private InternalFrameListener internalFrameListener = new InternalFrameAdapter() {
        @Override
        public void internalFrameActivated(InternalFrameEvent evt) {
            JInternalFrame source = evt.getInternalFrame();
            if (source instanceof DataView) {
                chart = ((DataView) evt.getSource()).getChart();
                refresh();
            }
        }
    };

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
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        JComboBox cboRenderers = new JComboBox(RendererType.values());

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int modelIndex = column.getModelIndex();
            if (RendererTableModel.COL_INDEX_VISIBLE == modelIndex) {
                column.setPreferredWidth(PREFERRED_WIDTH_BOOLEAN);
            }
            if (RendererTableModel.COL_INDEX_RENDERER == modelIndex) {
                column.setCellEditor(new DefaultCellEditor(cboRenderers));
            }
        }

        JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(PREFERRED_TABLE_SIZE);
        add(scrollPane, constraints);

        constraints.gridy++;
        constraints.weighty = 0;
        chkStatistics = new JCheckBox("disp. stat values");
        chkStatistics.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (getAlohaChart() == null) {
                    return;
                }
                getAlohaChart().setVisibleStatistics(chkStatistics.isSelected());
            }
        });
        add(chkStatistics, constraints);

        constraints.gridy++;
        constraints.weighty = 0;
        chkLabelsVisible = new JCheckBox("disp labels");
        chkLabelsVisible.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (getAlohaChart() == null) {
                    return;
                }
                getAlohaChart().setVisibleCategory(chkLabelsVisible.isSelected());
            }
        });
        add(chkLabelsVisible, constraints);

        constraints.gridy++;
        constraints.weighty = 0;
        chkHVIndicatorsVisible = new JCheckBox("disp H/V indicators");
        chkHVIndicatorsVisible.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (getAlohaChart() == null) {
                    return;
                }
                getAlohaChart().setVisibleHVIndicators(chkHVIndicatorsVisible.isSelected());

            }
        });
        add(chkHVIndicatorsVisible, constraints);

        constraints.gridy++;
        constraints.weighty = 0;
        JButton btn = new JButton(new AbstractAction("refresh") {
            private static final long serialVersionUID = -7286220111747715434L;

            @Override
            public void actionPerformed(ActionEvent evt) {
                refreshActiveChart();
            }
        });
        add(btn, constraints);
    }

    /**
     * refreshes the actual displayed data
     */
    private void refresh() {
        if (getAlohaChart() == null) {
            this.table.setEnabled(false);
            this.chkStatistics.setEnabled(false);
            this.chkLabelsVisible.setEnabled(false);
            this.chkHVIndicatorsVisible.setEnabled(false);
        } else {
            this.table.setEnabled(true);
            this.chkStatistics.setEnabled(true);
            this.chkLabelsVisible.setEnabled(true);
            this.chkHVIndicatorsVisible.setEnabled(true);
            this.chkStatistics.setSelected(getAlohaChart().isVisibleStatistics());
            this.chkLabelsVisible.setSelected(getAlohaChart().isVisibleCategory());
            this.chkHVIndicatorsVisible.setSelected(getAlohaChart().isVisibleHVIndicators());
        }
        this.tableModel.refresh();
    }

    /**
     * setter used by spring. This method adds a focus-listener to every DataView, so that we get noticed, if the
     * current DataView changes.
     */
    public void setDisplaySetManager(DisplaySetManager displaySetManager) {
        displaySetManager.addListener(new DisplaySetManagerListener() {

            @Override
            public void changedDisplaySet(DisplaySet oldDisplaySet, DisplaySet newDisplaySet) {
                if (oldDisplaySet != null) {
                    unregisterListeners(oldDisplaySet.getDvViews());
                }
                if (newDisplaySet != null) {
                    registerListeners(newDisplaySet.getDvViews());
                }
            }

        });

        DisplaySet activeDisplaySet = displaySetManager.getActiveDisplaySet();
        if (activeDisplaySet != null) {
            registerListeners(activeDisplaySet.getDvViews());
        }

    }

    private void registerListeners(List<DVView> dvViews) {
        for (DVView dvView : dvViews) {
            for (DataView view : dvView.getDataViews()) {
                view.addInternalFrameListener(internalFrameListener);
            }
        }
    }

    private void unregisterListeners(List<DVView> dvViews) {
        for (DVView dvView : dvViews) {
            for (DataView view : dvView.getDataViews()) {
                view.removeInternalFrameListener(internalFrameListener);
            }
        }
    }

    /**
     * @return the currently active chart
     */
    private Chart getActiveChart() {
        return this.chart;
    }

    /**
     * @return the (casted) active chart, if it is an aloha-chart, otherwise null;
     */
    private Aloha2DChart getAlohaChart() {
        if (getActiveChart() instanceof Aloha2DChart) {
            return (Aloha2DChart) getActiveChart();
        }
        return null;
    }

    /**
     * enforces a refresh to the active chart.
     */
    private void refreshActiveChart() {
        Chart chart = getActiveChart();
        if (chart == null) {
            return;
        }
        DataSource[] dataSources = chart.getDataSources();
        for (int i = 0; i < dataSources.length; i++) {
            DataSet[] dataSets = dataSources[i].getDataSets();
            for (int j = 0; j < dataSets.length; j++) {
                DataSet dataSet = dataSets[j];
                if (dataSet instanceof AbstractJmadDataSet) {
                    ((AbstractJmadDataSet) dataSet).refresh();
                }
            }
        }
    }

    /**
     * the table-model for the renderers-table
     * 
     * @author kfuchsbe
     */
    private class RendererTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -8594710275196399768L;

        /** the number of the columns */
        private final static int COL_COUNT = 3;

        /* the indizes for the different columns */
        public final static int COL_INDEX_VISIBLE = 0;
        public final static int COL_INDEX_ROLE = 1;
        public final static int COL_INDEX_RENDERER = 2;

        /**
         * sends a refresh-event to all listeners
         */
        public void refresh() {
            fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return COL_COUNT;
        }

        @Override
        public int getRowCount() {
            return ChartRendererRole.values().length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (getAlohaChart() == null) {
                return null;
            }

            ChartRendererRole role = ChartRendererRole.values()[row];
            ChartRenderer renderer = getAlohaChart().getRenderer(role);
            RendererType rendererType = getAlohaChart().getRendererType(role);

            Object value = null;
            switch (col) {
            case COL_INDEX_VISIBLE:
                value = renderer.isVisible();
                break;
            case COL_INDEX_ROLE:
                value = role.toString();
                break;
            case COL_INDEX_RENDERER:
                value = rendererType;
                break;
            }
            return value;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == COL_INDEX_VISIBLE) {
                return true;
            } else if (col == COL_INDEX_RENDERER) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            if (getAlohaChart() == null) {
                return;
            }

            ChartRendererRole role = ChartRendererRole.values()[row];
            ChartRenderer renderer = getAlohaChart().getRenderer(role);

            switch (col) {
            case COL_INDEX_VISIBLE:
                renderer.setVisible((Boolean) value);
                break;
            case COL_INDEX_RENDERER:
                getAlohaChart().setRendererType(role, (RendererType) value);
                break;
            }
        }

        @Override
        public Class<?> getColumnClass(int col) {
            switch (col) {
            case COL_INDEX_VISIBLE:
                return Boolean.class;
            case COL_INDEX_ROLE:
                return String.class;
            case COL_INDEX_RENDERER:
                return RendererType.class;
            default:
                return null;
            }
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
            case COL_INDEX_VISIBLE:
                return "visible";
            case COL_INDEX_ROLE:
                return "role";
            case COL_INDEX_RENDERER:
                return "renderer";
            default:
                return null;
            }
        }
    }

    @Override
    public String getTitle() {
        return "Data viewer options";
    }

    @Override
    public void registerDataViews(DataViewer dataViewer) {
        List<DVView> dvViews = Arrays.asList(dataViewer.getViews());
        registerListeners(dvViews);
    }
}
