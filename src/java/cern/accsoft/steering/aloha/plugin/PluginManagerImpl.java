/**
 *
 */
package cern.accsoft.steering.aloha.plugin;

import cern.accsoft.steering.aloha.analyzer.AnalyzerManager;
import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.calc.algorithm.AlgorithmManager;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributorFactoryManager;
import cern.accsoft.steering.aloha.calc.solve.SolverManager;
import cern.accsoft.steering.aloha.gui.display.DisplaySetManager;
import cern.accsoft.steering.aloha.gui.panels.solve.SolverConfigPanelManager;
import cern.accsoft.steering.aloha.plugin.api.AlgorithmFactory;
import cern.accsoft.steering.aloha.plugin.api.AlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.AnalyzerFactory;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;
import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import cern.accsoft.steering.aloha.plugin.api.SolverConfigPanelFactory;
import cern.accsoft.steering.aloha.plugin.api.SolverProvider;
import cern.accsoft.steering.aloha.plugin.disp.DispersionPlugin;
import cern.accsoft.steering.aloha.plugin.kickresp.KickResponsePlugin;
import cern.accsoft.steering.aloha.plugin.msolve.MatrixSolverPlugin;
import cern.accsoft.steering.aloha.plugin.multiturn.MultiturnPlugin;
import cern.accsoft.steering.aloha.plugin.ssolve.SimpleSolverPlugin;
import cern.accsoft.steering.aloha.plugin.traj.TrajectoryPlugin;
import cern.accsoft.steering.aloha.plugin.trim.TrimPlugin;
import cern.accsoft.steering.aloha.plugin.xxx.XXXPlugin;
import cern.accsoft.steering.aloha.read.ReaderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the only implementation of the aloha plugin-manager.
 *
 * It searches in the classpath for all non abstract classes, that implement the
 * interface {@link AlohaPlugin}. These then are instantiated and depending on
 * the interfaces they implement, their provided components are added to the
 * according managers.
 *
 * @author kfuchsbe
 *
 */
public class PluginManagerImpl implements PluginManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(PluginManagerImpl.class);

    /** all the plugins that where find. */
    private List<AlohaPlugin> plugins = new ArrayList<>();

    /** the manager for all the readers */
    private ReaderManager readerManager;

    /** the manager for all {@link SensitivityMatrixContributorFactory}s */
    private SensitivityMatrixContributorFactoryManager sensityMatrixContributorFactoryManager;

    /** the manager, which keeps track of the displaysets */
    private DisplaySetManager displaySetManager;

    /** The beanfactory, which we need to configure all the instantiated beans */
    private AlohaBeanFactory alohaBeanFactory;

    /** The manager which can create analyzers */
    private AnalyzerManager analyzerManager;

    /** The manager which keeps track of all the algorithms */
    private AlgorithmManager algorithmManager;

    /** The manager for all the solvers */
    private SolverManager solverManager;

    /** The manager which keeps track of all the solver-config-panels */
    private SolverConfigPanelManager solverConfigPanelManager;

    /**
     * this is the init method, called by spring
     */
    public final void init() {
        /*
         * first we look for all plugins available in the classpath
         */
        LOGGER.info("Searching for aloha plugins...");
        List<AlohaPlugin> plugins = getHardcodedPlugins();

        /*
         * then we configure each of it through the AlohaBeanFactory to ensure,
         * that all the required beans are set to it.
         *
         * Additionaly we add the plugins to the managers which need them,
         * depending on what interfaces they implement.
         */
        for (AlohaPlugin plugin : plugins) {
            getAlohaBeanFactory().configure(plugin);
            registerToManagers(plugin);
            LOGGER.info("Succesfully registered plugin '" + plugin.getName()
                    + "'.");
        }
    }

    /**
     * registers the plugin to all managers which need it, depending on the
     * interfaces which are implemented by the plugin.
     *
     * @param plugin
     */
    private void registerToManagers(AlohaPlugin plugin) {
        if (plugin instanceof ReaderProvider) {
            getReaderManager().addReaders((ReaderProvider) plugin);
        }
        if (plugin instanceof SensitivityMatrixContributorFactory) {
            getSensityMatrixContributorFactoryManager().addFactory(
                    (SensitivityMatrixContributorFactory) plugin);
        }
        if (plugin instanceof DisplaySetFactory) {
            getDisplaySetManager().addDisplaySetFactory(
                    (DisplaySetFactory) plugin);
        }
        if (plugin instanceof AnalyzerFactory) {
            getAnalyzerManager().addAnalyzerFactory((AnalyzerFactory) plugin);
        }
        if (plugin instanceof AlgorithmFactory) {
            getAlgorithmManager()
                    .addAlgorithmFactory((AlgorithmFactory) plugin);
        }
        if (plugin instanceof SolverProvider) {
            getSolverManager().addSolvers(
                    ((SolverProvider) plugin).getSolvers());
        }
        if (plugin instanceof SolverConfigPanelFactory) {
            getSolverConfigPanelManager().addFactory(
                    (SolverConfigPanelFactory) plugin);
        }
    }

    /**
     * XXX just a hack!!!!
     *
     * @return
     */
    private List<AlohaPlugin> getHardcodedPlugins() {
        List<AlohaPlugin> plugins = new ArrayList<>();
        plugins.add(new KickResponsePlugin());
        plugins.add(new DispersionPlugin());
        plugins.add(new TrajectoryPlugin());
        plugins.add(new MultiturnPlugin());
        plugins.add(new SimpleSolverPlugin());
        plugins.add(new MatrixSolverPlugin());
        plugins.add(new TrimPlugin());
        plugins.add(new XXXPlugin());
        return plugins;
    }

    @Override
    public List<AlohaPlugin> getPlugins() {
        return this.plugins;
    }

    /*
     * getters and setters
     */

    public void setReaderManager(ReaderManager readerManager) {
        this.readerManager = readerManager;
    }

    private ReaderManager getReaderManager() {
        return readerManager;
    }

    public void setSensityMatrixContributorFactoryManager(
            SensitivityMatrixContributorFactoryManager sensityMatrixContributorFactoryManager) {
        this.sensityMatrixContributorFactoryManager = sensityMatrixContributorFactoryManager;
    }

    private SensitivityMatrixContributorFactoryManager getSensityMatrixContributorFactoryManager() {
        return sensityMatrixContributorFactoryManager;
    }

    public void setDisplaySetManager(DisplaySetManager displaySetManager) {
        this.displaySetManager = displaySetManager;
    }

    private DisplaySetManager getDisplaySetManager() {
        return displaySetManager;
    }

    public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
        this.alohaBeanFactory = alohaBeanFactory;
    }

    private AlohaBeanFactory getAlohaBeanFactory() {
        return alohaBeanFactory;
    }

    public void setAnalyzerManager(AnalyzerManager analyzerManager) {
        this.analyzerManager = analyzerManager;
    }

    private AnalyzerManager getAnalyzerManager() {
        return analyzerManager;
    }

    public void setSolverManager(SolverManager solverManager) {
        this.solverManager = solverManager;
    }

    private SolverManager getSolverManager() {
        return solverManager;
    }

    public void setAlgorithmManager(AlgorithmManager algorithmManager) {
        this.algorithmManager = algorithmManager;
    }

    private AlgorithmManager getAlgorithmManager() {
        return algorithmManager;
    }

    public void setSolverConfigPanelManager(
            SolverConfigPanelManager solverConfigPanelManager) {
        this.solverConfigPanelManager = solverConfigPanelManager;
    }

    private SolverConfigPanelManager getSolverConfigPanelManager() {
        return solverConfigPanelManager;
    }

}
