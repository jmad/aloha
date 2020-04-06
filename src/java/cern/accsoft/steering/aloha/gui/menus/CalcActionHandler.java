/*
 * $Id: CalcActionHandler.java,v 1.5 2009-02-25 18:48:43 kfuchsbe Exp $
 *
 * $Date: 2009-02-25 18:48:43 $ $Revision: 1.5 $ $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.menus;

import cern.accsoft.gui.frame.util.CompletableFutureTasks;
import cern.accsoft.steering.aloha.calc.Calculator;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * This singleton class handles actions related to calculations.
 *
 * @author kfuchsbe
 */
public class CalcActionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(CalcActionHandler.class);

    /**
     * the calculator-manager
     */
    private Calculator calculator;

    /**
     * the main panel in order to be able to disable it
     */
    private JPanel mainPanel;

    /**
     * the manager which keeps track of all instances of model-delegates.
     */
    private ModelDelegateManager modelDelegateManager;

    /**
     * perform the given steps of calculation
     *
     * @param iterations the number of iterations to calculate
     */
    public void calc(int iterations) {

        final int taskIterations = iterations;

        CompletableFutureTasks.backgroundTask("Calculating fit", () -> {
            LOGGER.info("Starting calculation.");
            try {
                for (int i = 0; i < taskIterations; i++) {
                    getCalculator().calc();
                }
            } catch (CalculatorException ex) {
                LOGGER.error("Error while calculating fit!", ex);
            }
        });
    }

    /**
     * recalc measurement and reset the calculator
     */
    public void reset() {
        try {
            getCalculator().reset();
        } catch (CalculatorException ex) {
            LOGGER.error("Error resetting calculator!", ex);
        }
    }

    /**
     * reset the model
     */
    public void resetModels() {
        for (ModelDelegate modelDelegate : getModelDelegateManager().getModelDelegates()) {
            modelDelegate.reset();
        }
    }

    /**
     * @param mainPanel the mainPanel to set
     */
    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    /**
     * @return the mainPanel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setModelDelegateManager(ModelDelegateManager modelDelegateManager) {
        this.modelDelegateManager = modelDelegateManager;
    }

    private ModelDelegateManager getModelDelegateManager() {
        return modelDelegateManager;
    }

    public void setCalculator(Calculator calculatorManager) {
        this.calculator = calculatorManager;
    }

    public Calculator getCalculator() {
        return calculator;
    }

}
