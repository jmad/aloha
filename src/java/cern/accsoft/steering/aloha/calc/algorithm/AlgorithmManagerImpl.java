/**
 *
 */
package cern.accsoft.steering.aloha.calc.algorithm;

import cern.accsoft.steering.aloha.calc.solve.Solver;
import cern.accsoft.steering.aloha.plugin.api.AlgorithmFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation of an {@link AlgorithmManager}.
 *
 * @author kfuchsbe
 *
 */
public class AlgorithmManagerImpl implements AlgorithmManager {

    /** The logger for the class */
    private final static Logger logger = LoggerFactory.getLogger(AlgorithmManagerImpl.class);

    /** all the factories */
    private List<AlgorithmFactory> factories = new ArrayList<>();

    /** all the already instantiated algorithms */
    private Map<Solver, Algorithm> algorithms = new HashMap<>();

    @Override
    public void addAlgorithmFactory(AlgorithmFactory factory) {
        this.factories.add(factory);
    }

    /**
     * creates a allgorithm from the factories
     *
     * @param solver
     * @return
     */
    private Algorithm createAlgorithm(Solver solver) {
        for (AlgorithmFactory factory : this.factories) {
            Algorithm calculator = factory.createAlgorithm(solver);
            /*
             * the first factory that returns a non-null calculator wins.
             */
            if (calculator != null) {
                return calculator;
            }
        }

        logger.warn("No calculator-factory found for solver'" + solver + "'.");
        return null;
    }

    @Override
    public Algorithm getAlgorithm(Solver solver) {
        Algorithm algorithm = this.algorithms.get(solver);
        if (algorithm == null) {
            algorithm = createAlgorithm(solver);
            this.algorithms.put(solver, algorithm);
        }
        return algorithm;
    }

}
