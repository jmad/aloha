/*
 * $Id: CalcActionHandler.java,v 1.5 2009-02-25 18:48:43 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:43 $ $Revision: 1.5 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.menus;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cern.accsoft.gui.frame.Task;
import cern.accsoft.steering.aloha.calc.Calculator;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;

/**
 * This singleton class handles actions related to calculations.
 * 
 * @author kfuchsbe
 */
public class CalcActionHandler {
    /** the logger for the class */
    private final static Logger logger = Logger.getLogger(CalcActionHandler.class);

    /** the calculator-manager */
    private Calculator calculator;

    /** the main panel in order to be able to disable it */
    private JPanel mainPanel;

    /** the manager which keeps track of all instances of model-delegates. */
    private ModelDelegateManager modelDelegateManager;

    /**
     * perform the given steps of calculation
     * 
     * @param iterations the number of iterations to calculate
     */
    public void calc(int iterations) {

        final int taskIterations = iterations;

        Task task = new Task() {
            @Override
            protected Object construct() {
                logger.info("Starting calculation.");
                try {
                    for (int i = 0; i < taskIterations; i++) {
                        getCalculator().calc();
                    }
                } catch (CalculatorException ex) {
                    logger.error("Error while calculating fit!", ex);
                }
                return null;
            }

        };
        task.setName("Calculating fit");
        task.setCancellable(true);
        task.start();
    }

    /**
     * recalc measurement and reset the calculator
     */
    public void reset() {
        try {
            getCalculator().reset();
        } catch (CalculatorException ex) {
            logger.error("Error resetting calculator!", ex);
        }
    }

    /**
     * reset the model
     */
    public void resetModels() {
        if (getModelDelegateManager() == null) {
            logger.warn("No modeldDelegateManager set. Maybe configuration error.");
        }

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
