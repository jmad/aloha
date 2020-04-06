/**
 * 
 */
package cern.accsoft.steering.aloha.calc;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.algorithm.Algorithm;
import cern.accsoft.steering.aloha.calc.algorithm.AlgorithmManager;
import cern.accsoft.steering.aloha.calc.iteration.IterationManager;
import cern.accsoft.steering.aloha.calc.solve.SolverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * The implementation of a class that keeps track of all available Calculators.
 * 
 * @author tbaer
 */
public class CalculatorImpl implements Calculator {
    private final static Logger LOGGER = LoggerFactory.getLogger(CalculatorImpl.class);

    /** the listeners to the calculator */
    private ArrayList<CalculatorListener> listeners = new ArrayList<>();

    /** The manager which we use to get/create algorithms for solvers */
    private AlgorithmManager algorithmManager;

    /** The class that keeps track of performed iterations */
    private IterationManager iterationManager;

    /** the changes in parameters during the last iteration */
    private Matrix deltaParameterValues;

    /** the manager from which we get to know the active solver */
    private SolverManager solverManager;

    protected void fireChangedCalculatedValues() {
        for (CalculatorListener listener : listeners) {
            listener.changedCalculatedValues(this);
        }
    }

    protected void fireChangedVariationParameters() {
        for (CalculatorListener listener : listeners) {
            listener.changedVariationParameters(this);
        }
    }

    @Override
    public void addListener(CalculatorListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(CalculatorListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void calc() throws CalculatorException {
        Algorithm activeAlgorithm = getActiveAlgorithm();
        if (activeAlgorithm == null) {
            LOGGER.warn("No active algorithm set. Cannot calculate.");
            return;
        }

        int iteration = getIterationManager().getLastIterationNumber();

        /*
         * if this will be the first iteration, then we record the initial state
         */
        if (iteration < 0) {
            getIterationManager().recordIteration();
        }

        /*
         * here we start the calculation and increase the iteration number
         */
        LOGGER.info("starting fit - iteration number " + (iteration + 1) + ".");
        this.deltaParameterValues = activeAlgorithm.calc();

        /*
         * store the new iteration values
         */
        getIterationManager().recordIteration();

        /*
         * After the calculation there might be a lot of mess around (e.g big unused matrices), so we try to force
         * garbage collection !
         */
        System.gc();

        /*
         * here we can notify the listeners, since there is new Data to be fetched
         */
        fireChangedCalculatedValues();
    }

    @Override
    public Matrix getDeltaParameterValues() {
        return deltaParameterValues;
    }

    @Override
    public void reset() throws CalculatorException {
        Algorithm activeAlgorithm = getActiveAlgorithm();
        if (activeAlgorithm == null) {
            LOGGER.warn("No active algorithm set. Cannot reset.");
            return;
        }
        activeAlgorithm.reset();
        getIterationManager().reset();
        fireChangedCalculatedValues();
    }

    /**
     * the init method used by spring to inject the {@link AlgorithmManager}
     * 
     * @param algorithmManager
     */
    public void setAlgorithmManager(AlgorithmManager algorithmManager) {
        this.algorithmManager = algorithmManager;
    }

    private AlgorithmManager getAlgorithmManager() {
        return algorithmManager;
    }

    /**
     * @return the algorithm which is uses the current solver.
     */
    private Algorithm getActiveAlgorithm() {
        return this.getAlgorithmManager().getAlgorithm(getSolverManager().getActiveSolver());
    }

    public void setSolverManager(SolverManager solverManager) {
        this.solverManager = solverManager;
    }

    private SolverManager getSolverManager() {
        return solverManager;
    }

    public void setIterationManager(IterationManager iterationManager) {
        this.iterationManager = iterationManager;
    }

    private IterationManager getIterationManager() {
        return iterationManager;
    }
}
