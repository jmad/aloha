package cern.accsoft.steering.aloha.gui.panels.fit;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;

import cern.accsoft.steering.aloha.gui.icons.Icon;
import cern.accsoft.steering.aloha.gui.menus.CalcActionHandler;
import cern.accsoft.steering.aloha.gui.panels.fit.ResultExportActionHandler.ExportData;

public class CalcButtonsPanel extends JPanel {
    private CalcActionHandler calcActionHandler;
    private ResultExportActionHandler resultExportActionHandler;

    /**
     * the number of iterations
     */
    private int iterations = 1;

    /**
     * init method called by spring
     */
    public void init() {
        initComponents();
    }

    /**
     * adds all components to the panel
     */
    private void initComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(3, 3, 3, 3);
        constraints.fill = GridBagConstraints.BOTH;

        JButton btn;

        constraints.gridy++;
        btn = new JButton(createResetAction());
        add(btn, constraints);

        constraints.gridy++;
        btn = new JButton(createResetModelAction());
        add(btn, constraints);

        constraints.gridy++;
        btn = new JButton(createCalcAction());
        btn.setIcon(Icon.CALC_ONCE.getImageIcon());
        btn.setDisabledIcon(Icon.CALC_ONCE_DISABLED.getImageIcon());
        add(btn, constraints);

        constraints.gridy++;
        constraints.gridwidth = 1;
        JLabel lbl = new JLabel("Iterations: ");
        add(lbl, constraints);

        constraints.gridx++;
        JFormattedTextField txtIterations = new JFormattedTextField(iterations);
        txtIterations.addActionListener(e -> iterations = (Integer) txtIterations.getValue());
        add(txtIterations, constraints);

        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy++;
        add(Box.createVerticalStrut(30), constraints);
        for (ExportData exportData : ExportData.values()) {
            constraints.gridy++;
            btn = new JButton(createExportAction(exportData));
            add(btn, constraints);
        }
    }

    /**
     * creates the action, which starts the calculation
     *
     * @return the action
     */
    private Action createCalcAction() {
        AbstractAction calcAction = new AbstractAction("calculate") {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcActionHandler.calc(iterations);
            }
        };
        calcAction.putValue(Action.SHORT_DESCRIPTION, "Performs the given steps of calculation.");
        return calcAction;
    }

    private Action createResetAction() {
        AbstractAction calcAction = new AbstractAction("reset") {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcActionHandler.reset();
            }
        };
        calcAction.putValue(Action.SHORT_DESCRIPTION, "Resets the Calculator to its initial state.");
        return calcAction;

    }

    private Action createResetModelAction() {
        AbstractAction calcAction = new AbstractAction("reset models") {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcActionHandler.resetModels();
            }
        };
        calcAction.putValue(Action.SHORT_DESCRIPTION, "Resets all the models to their initial state.");
        return calcAction;

    }

    private Action createExportAction(ExportData exportData) {
        AbstractAction calcAction = new AbstractAction("export " + exportData.getDisplayName()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Export " + exportData.getDisplayName());
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
                fileChooser.setAcceptAllFileFilterUsed(false);
                int choice = fileChooser.showSaveDialog(CalcButtonsPanel.this);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    resultExportActionHandler.exportToCsv(exportData, fileChooser.getSelectedFile());
                }
            }
        };
        calcAction.putValue(Action.SHORT_DESCRIPTION, "Export the displayed results to CSV files.");
        return calcAction;
    }

    public void setCalcActionHandler(CalcActionHandler calcActionHandler) {
        this.calcActionHandler = calcActionHandler;
    }

    public void setResultExportActionHandler(
            ResultExportActionHandler resultExportActionHandler) {
        this.resultExportActionHandler = resultExportActionHandler;
    }
}
