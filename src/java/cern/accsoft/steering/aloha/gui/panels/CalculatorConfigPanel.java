package cern.accsoft.steering.aloha.gui.panels;

import cern.accsoft.steering.aloha.app.Preferences;
import cern.accsoft.steering.aloha.calc.NoiseWeighterConfig;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManagerConfig;
import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.calc.solve.SolverManager;
import cern.accsoft.steering.aloha.gui.panels.solve.SolverConfigPanel;
import cern.accsoft.steering.aloha.gui.panels.solve.SolverConfigPanelManager;
import cern.accsoft.steering.util.gui.panels.Applyable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CalculatorConfigPanel extends JPanel implements Applyable {
    /**
     * the manager, which keeps track of the actually active Solver
     */
    private SolverManager solverManager;

    /**
     * the preferences whic define also the number format
     */
    private Preferences preferences;

    /**
     * the configuration interface for the sensity-matrix manager
     */
    private SensitivityMatrixManagerConfig sensityMatrixManagerConfig;

    /**
     * the config interface for the noise-weighter
     */
    private NoiseWeighterConfig noiseWeighterConfig;

    /* the input fields */
    private JFormattedTextField txtMinNorm;
    private JFormattedTextField txtNoiseLimit;
    private JCheckBox chkVaryMonitorGains;
    private JCheckBox chkVaryCorrectorGains;
    private JCheckBox chkActiveNoise;
    private JComboBox<Solver> cboSolvers;

    /**
     * the placeholder - panel for the solverPanel
     */
    private JPanel solverPanel;

    /**
     * the config panel for the active solver
     */
    private SolverConfigPanel activeSolverConfigPanel;

    /**
     * The manager which keeps track of all the available config-panels for
     * solvers.
     */
    private SolverConfigPanelManager solverConfigPanelManager;

    @Override
    public boolean apply() {
        getNoiseWeighterConfig().setNoiseLimit(
                (Double) txtNoiseLimit.getValue());
        getNoiseWeighterConfig().setActiveNoise(chkActiveNoise.isSelected());

        getSensityMatrixManagerConfig().setMinNorm(
                (Double) txtMinNorm.getValue());
        getSensityMatrixManagerConfig().setVaryMonitorGains(
                chkVaryMonitorGains.isSelected());
        getSensityMatrixManagerConfig().setVaryCorrectorGains(
                chkVaryCorrectorGains.isSelected());

        solverManager.setActiveSolver((Solver) cboSolvers.getSelectedItem());
        if (this.activeSolverConfigPanel != null) {
            this.activeSolverConfigPanel.apply();
        }

        return true;
    }

    @Override
    public void cancel() {
        /* nothing to do */
    }

    @Override
    public Dimension getPreferredSize() {
        return null;
    }

    public void init() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel label;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        label = new JLabel("Norm minimum: ");
        add(label, constraints);

        constraints.gridx += 1;
        txtMinNorm = new JFormattedTextField(getPreferences().getNumberFormat());
        txtMinNorm.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        txtMinNorm.setValue(getSensityMatrixManagerConfig().getMinNorm());
        txtMinNorm
                .setToolTipText("If a norm for perturbed response-matrix below this level is calculated, "
                        + "then a warning is logged and the perturbed matrix is not normalized.");
        add(txtMinNorm, constraints);

        constraints.gridx = 0;
        constraints.gridy += 1;
        label = new JLabel("Noise limit: ");
        add(label, constraints);

        constraints.gridx += 1;
        txtNoiseLimit = new JFormattedTextField(getPreferences()
                .getNumberFormat());
        txtNoiseLimit.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        txtNoiseLimit.setValue(getNoiseWeighterConfig().getNoiseLimit());
        txtNoiseLimit.setToolTipText("A Noise-Value [m] below this limit is considered as zero noise.");
        add(txtNoiseLimit, constraints);

        constraints.gridx = 0;
        constraints.gridy += 1;
        chkVaryMonitorGains = new JCheckBox();
        chkVaryMonitorGains.setText("Vary Monitor gains");
        chkVaryMonitorGains
                .setToolTipText("When checked, the monitor gains are varied by the calculator, otherwise not. (default=true)");
        chkVaryMonitorGains.setSelected(getSensityMatrixManagerConfig()
                .isVaryMonitorGains());
        add(chkVaryMonitorGains, constraints);

        constraints.gridx = 0;
        constraints.gridy += 1;
        chkVaryCorrectorGains = new JCheckBox();
        chkVaryCorrectorGains.setText("Vary Corrector gains");
        chkVaryCorrectorGains
                .setToolTipText("When checked, the corrector gains are varied by the calculator, else not. (default=true)");
        chkVaryCorrectorGains.setSelected(getSensityMatrixManagerConfig()
                .isVaryCorrectorGains());
        add(chkVaryCorrectorGains, constraints);

        constraints.gridx = 0;
        constraints.gridy += 1;
        chkActiveNoise = new JCheckBox();
        chkActiveNoise.setText("Use Noise");
        chkActiveNoise
                .setToolTipText("When checked, then Responses are normalized by corresponding pickup noise. (default=true)");
        chkActiveNoise.setSelected(getNoiseWeighterConfig().isActiveNoise());
        add(chkActiveNoise, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;

        JPanel solverBorder = new JPanel(new BorderLayout());
        Border etched = BorderFactory.createEtchedBorder();
        Border titled = BorderFactory.createTitledBorder(etched, "Solver");
        solverBorder.setBorder(titled);

        solverPanel = new JPanel(new BorderLayout());
        solverBorder.add(solverPanel, BorderLayout.CENTER);

        cboSolvers = new JComboBox<Solver>(getSolverManager().getSolvers().toArray(new Solver[0]));
        cboSolvers.addActionListener(evt -> {
            solverPanel.removeAll();
            activeSolverConfigPanel = getSolverConfigPanelManager()
                    .getConfigPanel((Solver) cboSolvers.getSelectedItem());
            if (activeSolverConfigPanel != null) {
                solverPanel.add(activeSolverConfigPanel,
                        BorderLayout.CENTER);
            }
            solverPanel.validate();
        });
        solverBorder.add(cboSolvers, BorderLayout.NORTH);
        add(solverBorder, constraints);

        /* set the active solver to the default one. */
        cboSolvers.setSelectedItem(getSolverManager().getActiveSolver());
    }

    /**
     * @param sensityMatrixManagerConfig the sensityMatrixManagerConfig to set
     */
    public void setSensityMatrixManagerConfig(
            SensitivityMatrixManagerConfig sensityMatrixManagerConfig) {
        this.sensityMatrixManagerConfig = sensityMatrixManagerConfig;
    }

    /**
     * @return the sensityMatrixManagerConfig
     */
    private SensitivityMatrixManagerConfig getSensityMatrixManagerConfig() {
        return sensityMatrixManagerConfig;
    }

    /**
     * @param noiseWeighterConfig the noiseWeighterConfig to set
     */
    public void setNoiseWeighterConfig(NoiseWeighterConfig noiseWeighterConfig) {
        this.noiseWeighterConfig = noiseWeighterConfig;
    }

    /**
     * @return the noiseWeighterConfig
     */
    private NoiseWeighterConfig getNoiseWeighterConfig() {
        return noiseWeighterConfig;
    }

    /**
     * @param solverManager the solverManager to set
     */
    public void setSolverManager(SolverManager solverManager) {
        this.solverManager = solverManager;
    }

    /**
     * @return the solverManager
     */
    private SolverManager getSolverManager() {
        return solverManager;
    }

    public void setSolverConfigPanelManager(
            SolverConfigPanelManager solverConfigPanelManager) {
        this.solverConfigPanelManager = solverConfigPanelManager;
    }

    private SolverConfigPanelManager getSolverConfigPanelManager() {
        return solverConfigPanelManager;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    private Preferences getPreferences() {
        return preferences;
    }

}
