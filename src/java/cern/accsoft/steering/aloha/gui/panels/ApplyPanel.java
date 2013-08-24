package cern.accsoft.steering.aloha.gui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import cern.accsoft.steering.util.gui.menu.ActionProvider;
import cern.accsoft.steering.util.gui.panels.Applyable;

public class ApplyPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JComponent applyPanelContent = null;

    public ApplyPanel() {
        super();
    }

    public ApplyPanel(JComponent applyPanelContent) {
        this.applyPanelContent = applyPanelContent;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.add(this.applyPanelContent, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 0;

        if (this.applyPanelContent instanceof Applyable) {
            JButton btn;
            btn = new JButton("Apply");
            btn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (ApplyPanel.this.applyPanelContent instanceof Applyable) {
                        ((Applyable) ApplyPanel.this.applyPanelContent).apply();
                    }
                }

            });
            constraints.weightx = 1;
            buttonPanel.add(btn, constraints);
            constraints.gridx++;
        }

        if (this.applyPanelContent instanceof ActionProvider) {
            List<Action> actions = ((ActionProvider) this.applyPanelContent).getActions();
            constraints.weightx = 0;
            for (Action action : actions) {
                JButton btn = new JButton(action);
                buttonPanel.add(btn, constraints);
                constraints.gridx++;
            }
        }

        if (constraints.gridx > 0) {
            this.add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    public void setApplyPanelContent(JComponent applyPanelContent) {
        this.applyPanelContent = applyPanelContent;
        initComponents();
    }

}