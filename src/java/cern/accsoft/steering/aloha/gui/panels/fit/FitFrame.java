/**
 *
 */
package cern.accsoft.steering.aloha.gui.panels.fit;

import cern.accsoft.gui.beans.MultiSplitLayout;
import cern.accsoft.gui.beans.MultiSplitPane;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * This is the panel to configure and start fits
 *
 * @author kfuchsbe
 *
 */
public class FitFrame extends JFrame {
    /** the panel, which displays the variation-parameters */
    private JPanel variationParameterPanel;

    /** the panel, which displays the fixed parameters */
    private JPanel fixedParameterPanel;

    /** the panel to configure the calculator */
    private JPanel calculatorConfigPanel;

    /** the info-panel of the calculator */
    private JPanel calculatorInfoPanel;

    /** the panel to configure the contributors to the sensitivity matrix */
    private JPanel sensitivityMatrixContributorConfigsPanel;

    /** the panel which contains the buttons to start the fits */
    private JPanel calcButtonsPanel;

    /** the panel to operate model details */
    private JPanel modelOperationPanel;

    private JPanel fitDataViewerPanel;

    public void init() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(1024, 768);
        setTitle("Aloha Fitting");

        /*
         * The multisplit layout and the multisplit-pane, which contains all
         * others.
         */
        String layoutDef = "(COLUMN (ROW weight=0.5 "
                + "(LEAF name=top.left weight=0.2) (LEAF name=top.center weight=0.4) (LEAF name=top.right weight=0.4)) "
                + "(LEAF name=bottom weight=0.5))";
        MultiSplitLayout.Node layoutModel = MultiSplitLayout
                .parseModel(layoutDef);
        MultiSplitPane multiSplitPane = new MultiSplitPane();
        multiSplitPane.getMultiSplitLayout().setModel(layoutModel);
        multiSplitPane.getMultiSplitLayout().setDividerSize(5);
        add(multiSplitPane, BorderLayout.CENTER);

        JPanel calcButtonsPanel = getCalcButtonsPanel();
        calcButtonsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        multiSplitPane.add(calcButtonsPanel, "top.left");

        /* Calculator Panels */
        JTabbedPane tabbedPaneCalculator = new JTabbedPane();
        tabbedPaneCalculator.setBorder(new BevelBorder(BevelBorder.LOWERED));
        multiSplitPane.add(tabbedPaneCalculator, "top.center");

        tabbedPaneCalculator.addTab("Configure", getCalculatorConfigPanel());
        tabbedPaneCalculator.addTab("Contributors",
                getSensitivityMatrixContributorConfigsPanel());
        tabbedPaneCalculator.addTab("varied parameters",
                getVariationParameterPanel());
        tabbedPaneCalculator.addTab("fixed parameters",
                getFixedParameterPanel());

        JPanel modelOperationsPanel = getModelOperationPanel();
        modelOperationsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        multiSplitPane.add(modelOperationsPanel, "top.right");

        if (getFitDataViewerPanel() != null) {
            getFitDataViewerPanel().setBorder(
                    new BevelBorder(BevelBorder.LOWERED));
            multiSplitPane.add(getFitDataViewerPanel(), "bottom");
        }
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public JPanel getVariationParameterPanel() {
        return variationParameterPanel;
    }

    public void setVariationParameterPanel(JPanel variationParameterPanel) {
        this.variationParameterPanel = variationParameterPanel;
    }

    public JPanel getCalculatorConfigPanel() {
        return calculatorConfigPanel;
    }

    public void setCalculatorConfigPanel(JPanel calculatorConfigPanel) {
        this.calculatorConfigPanel = calculatorConfigPanel;
    }

    public JPanel getCalculatorInfoPanel() {
        return calculatorInfoPanel;
    }

    public void setCalculatorInfoPanel(JPanel calculatorInfoPanel) {
        this.calculatorInfoPanel = calculatorInfoPanel;
    }

    public JPanel getSensitivityMatrixContributorConfigsPanel() {
        return sensitivityMatrixContributorConfigsPanel;
    }

    public void setSensitivityMatrixContributorConfigsPanel(
            JPanel sensitivityMatrixContributorConfigsPanel) {
        this.sensitivityMatrixContributorConfigsPanel = sensitivityMatrixContributorConfigsPanel;
    }

    public void setCalcButtonsPanel(JPanel calcButtonsPanel) {
        this.calcButtonsPanel = calcButtonsPanel;
    }

    public JPanel getCalcButtonsPanel() {
        return calcButtonsPanel;
    }

    public void setModelOperationPanel(JPanel modelOperationPanel) {
        this.modelOperationPanel = modelOperationPanel;
    }

    public JPanel getModelOperationPanel() {
        return modelOperationPanel;
    }

    public void setFixedParameterPanel(JPanel fixedParameterPanel) {
        this.fixedParameterPanel = fixedParameterPanel;
    }

    public JPanel getFixedParameterPanel() {
        return fixedParameterPanel;
    }

    public void setFitDataViewerPanel(JPanel fitDataViewerPanel) {
        this.fitDataViewerPanel = fitDataViewerPanel;
    }

    private JPanel getFitDataViewerPanel() {
        return fitDataViewerPanel;
    }
}
