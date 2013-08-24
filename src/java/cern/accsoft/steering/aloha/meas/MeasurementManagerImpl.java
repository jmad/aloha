/**
 * 
 */
package cern.accsoft.steering.aloha.meas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;
import cern.accsoft.steering.aloha.model.ModelDelegateManagerListener;
import cern.accsoft.steering.jmad.model.manage.JMadModelManager;

/**
 * This is the implementation of a class, which keeps track of all loaded measurements in aloha.
 * 
 * @author kfuchsbe
 */
public class MeasurementManagerImpl implements MeasurementManager, ModelDelegateManager {

    /** this list contains all available measurements */
    private List<ModelAwareMeasurement> measurements = new ArrayList<ModelAwareMeasurement>();

    /** the hashmap to look up the indizes */
    private Map<ModelAwareMeasurement, Integer> indizes = new HashMap<ModelAwareMeasurement, Integer>();

    /** the listeners to this class */
    private List<MeasurementManagerListener> listeners = new ArrayList<MeasurementManagerListener>();

    /** the listeners to the {@link ModelDelegateManager} */
    private List<ModelDelegateManagerListener> mdmListeners = new ArrayList<ModelDelegateManagerListener>();

    /** the model manager, to which we set the actual model of the measurement */
    private JMadModelManager modelManager = null;

    /** the index of the actually active measurement */
    private int activeIndex = -1;

    @Override
    public void addMeasurement(ModelAwareMeasurement measurement) {
        boolean newDelegate = !(getModelDelegates().contains(measurement.getModelDelegate()));
        this.measurements.add(measurement);
        int index = this.measurements.size() - 1;
        this.indizes.put(measurement, index);
        fireAddedMeasurement(measurement);
        setActiveMeasurement(index);
        if (newDelegate) {
            fireAddedModelDelegate(measurement.getModelDelegate());
        }
    }

    @Override
    public List<ModelAwareMeasurement> getMeasurements() {
        return this.measurements;
    }

    @Override
    public void removeMeasurement(int index) {
        ModelAwareMeasurement measurement = this.measurements.get(index);

        this.measurements.remove(measurement);

        /* we have to update the indizes */
        this.indizes.clear();
        for (int i = 0; i < this.measurements.size(); i++) {
            this.indizes.put(this.measurements.get(i), i);
        }

        int newIndex = -1;
        if (this.activeIndex > 0) {
            this.activeIndex--;
        } else if (this.measurements.size() > 0) {
            newIndex = 0;
        }

        if (this.measurements.size() < 1) {
            measurement.getMachineElementsManager().clear();
        }

        fireRemovedMeasurement(measurement);

        this.activeIndex = newIndex;
        fireChangedActiveMeasurement();

        if (!(getModelDelegates().contains(measurement.getModelDelegate()))) {
            fireRemovedModelDelegate(measurement.getModelDelegate());
        }
    }

    @Override
    public void addListener(MeasurementManagerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(MeasurementManagerListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public ModelAwareMeasurement getActiveMeasurement() {
        if ((this.activeIndex >= 0) && (activeIndex < this.measurements.size())) {
            return this.measurements.get(this.activeIndex);
        }
        return null;
    }

    @Override
    public void setActiveMeasurement(int index) {
        if ((index >= 0) && (index < this.measurements.size())) {
            if (index != this.activeIndex) {
                this.activeIndex = index;
                if (getActiveMeasurement() != null) {
                    if (getModelManager() != null) {
                        getModelManager().setActiveModel(getActiveMeasurement().getModelDelegate().getJMadModel());
                    }
                }
                fireChangedActiveMeasurement();
            }
        }
    }

    /**
     * notifies the listeners, that a measurement was added.
     */
    private void fireAddedMeasurement(Measurement newMeasurement) {
        for (MeasurementManagerListener listener : listeners) {
            listener.addedMeasurement(newMeasurement);
        }
    }

    /**
     * notifies the listeners, that a measurement was removed.
     */
    private void fireRemovedMeasurement(Measurement removedMeasurement) {
        for (MeasurementManagerListener listener : listeners) {
            listener.removedMeasurement(removedMeasurement);
        }
    }

    /**
     * notifies all listeners, that the active measurement has changed.
     */
    private void fireChangedActiveMeasurement() {
        for (MeasurementManagerListener listener : listeners) {
            listener.changedActiveMeasurement(getActiveMeasurement());
        }
    }

    /**
     * notifies the listeners that a new model-delegate was added.
     * 
     * @param modelDelegate the new {@link ModelDelegate}
     */
    private void fireAddedModelDelegate(ModelDelegate modelDelegate) {
        for (ModelDelegateManagerListener listener : mdmListeners) {
            listener.addedModelDelegate(modelDelegate);
        }
    }

    /**
     * notifies all listeners, that a {@link ModelDelegate} was removed.
     * 
     * @param removedModelDelegate
     */
    private void fireRemovedModelDelegate(ModelDelegate removedModelDelegate) {
        for (ModelDelegateManagerListener listener : mdmListeners) {
            listener.removedModelDelegate(removedModelDelegate);
        }
    }

    @Override
    public void setActiveMeasurement(ModelAwareMeasurement measurement) {
        Integer index = this.indizes.get(measurement);
        if (index != null) {
            setActiveMeasurement(index);
        }
    }

    @Override
    public List<ModelDelegateInstance> getModelDelegateInstances() {
        Map<ModelDelegate, ModelDelegateInstance> modelDelegateInstances = new HashMap<ModelDelegate, ModelDelegateInstance>();
        for (ModelAwareMeasurement measurement : this.measurements) {
            ModelDelegate modelDelegate = measurement.getModelDelegate();
            ModelDelegateInstance instance = modelDelegateInstances.get(modelDelegate);
            if (instance == null) {
                instance = new ModelDelegateInstance(modelDelegate);
                modelDelegateInstances.put(modelDelegate, instance);
            }
            instance.addMeasurementName(measurement);
        }
        return new ArrayList<ModelDelegateInstance>(modelDelegateInstances.values());
    }

    @Override
    public List<ModelDelegate> getModelDelegates() {
        Collection<ModelDelegateInstance> instances = getModelDelegateInstances();
        List<ModelDelegate> delegates = new ArrayList<ModelDelegate>();
        for (ModelDelegateInstance instance : instances) {
            delegates.add(instance.getModelDelegate());
        }
        return delegates;
    }

    @Override
    public void addListener(ModelDelegateManagerListener listener) {
        this.mdmListeners.add(listener);
    }

    @Override
    public void removeListener(ModelDelegateManagerListener listener) {
        this.mdmListeners.remove(listener);
    }

    public void setModelManager(JMadModelManager modelManager) {
        this.modelManager = modelManager;
    }

    private JMadModelManager getModelManager() {
        return modelManager;
    }

    @Override
    public List<ModelAwareMeasurement> getMeasurements(MeasurementType type) {
        List<ModelAwareMeasurement> measurements = new ArrayList<ModelAwareMeasurement>();
        for (ModelAwareMeasurement measurement : getMeasurements()) {
            if (type.equals(measurement.getType())) {
                measurements.add(measurement);
            }
        }
        return measurements;
    }

    @Override
    public ModelDelegate getActiveModelDelegate() {
        ModelAwareMeasurement measurement = getActiveMeasurement();
        if (measurement != null) {
            return measurement.getModelDelegate();
        }
        return null;
    }
}
