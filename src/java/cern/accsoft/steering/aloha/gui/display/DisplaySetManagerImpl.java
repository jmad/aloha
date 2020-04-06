/**
 *
 */
package cern.accsoft.steering.aloha.gui.display;

import cern.accsoft.steering.aloha.analyzer.AnalyzerManager;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This is the implementation of a {@link DisplaySetManager}.
 *
 * @author kfuchsbe
 *
 */
public class DisplaySetManagerImpl implements DisplaySetManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(DisplaySetManagerImpl.class);

    /**
     * the listeners to the {@link DisplaySetManager}.
     */
    private List<DisplaySetManagerListener> listeners = new ArrayList<DisplaySetManagerListener>();

    /** a class that keeps track of all creatable analyzers. */
    private AnalyzerManager analyzerManager;

    /**
     * All the available Factories. They will be injected by Spring. When
     * attempting to create a DisplaySet then they are looped through in the
     * injected order.
     */
    private List<DisplaySetFactory> displaySetFactories = new ArrayList<DisplaySetFactory>();

    /**
     * all the already created {@link DisplaySet}s. They are stored in a
     * {@link WeakHashMap} in order to ensure, that they are cleaned up, when
     * the measurement is deleted.
     */
    private Map<Measurement, DisplaySet> displaySets = new WeakHashMap<Measurement, DisplaySet>();

    /**
     * the active displayset, which can also be null
     */
    private WeakReference<DisplaySet> activeDisplaySet = new WeakReference<DisplaySet>(
            null);

    @Override
    public DisplaySet display(Measurement measurement) {
        if (measurement == null) {
            LOGGER.warn("measurement is null. Cannot find a DisplaySet.");
            setActiveDisplaySet(null);
            return null;
        }
        DisplaySet displaySet = this.displaySets.get(measurement);
        if (displaySet == null) {
            LOGGER.debug("Creating new displayset for measurement '"
                    + measurement.getName() + "'");
            displaySet = createDisplaySet(measurement);
            if (displaySet != null) {
                this.displaySets.put(measurement, displaySet);
            } else {
                LOGGER
                        .warn("No DisplaySet could be created for the measurement '"
                                + measurement.getName() + "'");
            }
        }
        setActiveDisplaySet(displaySet);
        return displaySet;
    }

    /**
     * creates a DisplaySet for a Measurement.
     *
     * @param measurement
     *            the measurement for which to create the {@link DisplaySet}
     * @return the new {@link DisplaySet}
     */
    private DisplaySet createDisplaySet(Measurement measurement) {
        DisplaySet displaySet = null;
        for (DisplaySetFactory factory : getDisplaySetFactories()) {
            displaySet = factory.createDisplaySet(measurement);
            /*
             * if we get one, which is not null, then we return it. So the first
             * factory who creates a valid model counts.
             */
            if (displaySet != null) {
                break;
            }

        }

        /*
         * if no special one is found for the measurement, then we at least
         * create the default one in order to be able to add analyzers.
         */
        if (displaySet == null) {
            displaySet = new DefaultDisplaySet();
        }

        /*
         * if the displayset offers the possibility to add additional analyzers,
         * then we add all analyzers which we can get for the given measurement.
         */
        if (displaySet != null) {
            if (displaySet instanceof AbstractDisplaySet) {
                ((AbstractDisplaySet) displaySet)
                        .addAnalyzers(getAnalyzerManager().createAnalyzers(
                                measurement));
            }
        }
        return displaySet;
    }

    //
    // getters and setters
    //

    @Override
    public void addDisplaySetFactory(DisplaySetFactory factory) {
        this.displaySetFactories.add(factory);
    }

    private List<DisplaySetFactory> getDisplaySetFactories() {
        return displaySetFactories;
    }

    //
    // listener stuff
    //

    private void fireChangedDisplaySet(DisplaySet oldDisplaySet,
                                       DisplaySet newDisplaySet) {
        for (DisplaySetManagerListener listener : listeners) {
            listener.changedDisplaySet(oldDisplaySet, newDisplaySet);
        }
    }

    @Override
    public void addListener(DisplaySetManagerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(DisplaySetManagerListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void setActiveDisplaySet(DisplaySet displaySet) {
        DisplaySet oldDisplaySet = getActiveDisplaySet();
        this.activeDisplaySet = new WeakReference<DisplaySet>(displaySet);
        fireChangedDisplaySet(oldDisplaySet, getActiveDisplaySet());
    }

    @Override
    public DisplaySet getActiveDisplaySet() {
        return this.activeDisplaySet.get();
    }

    public void setAnalyzerManager(AnalyzerManager analyzerManager) {
        this.analyzerManager = analyzerManager;
    }

    private AnalyzerManager getAnalyzerManager() {
        return analyzerManager;
    }

}
