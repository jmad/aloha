package cern.accsoft.steering.aloha.machine;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.meas.data.ElementKeyUtil;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.Status;

public abstract class AbstractMachineElement {

    private boolean active = true;

    private final String name;
    private final Plane plane;
    private final BeamNumber beamNumber;
    public double position = 0;

    private double initialGain = 1;

    private double gain = initialGain;
    private double gainError = 0.0;

    private Status status = Status.OK;

    /** the listeners to the elements */
    private List<MachineElementListener> listeners = new ArrayList<MachineElementListener>();

    public AbstractMachineElement(String name, Plane plane, BeamNumber beamNumber) {
        this.name = name;
        this.plane = plane;
        this.beamNumber = beamNumber;
    }

    @Override
    public String toString() {
        return getKey();
    }

    public String getKey() {
        return ElementKeyUtil.composeKey(this.name, this.plane, this.beamNumber);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        this.active = value;
        /* when an element becomes inactive we reset the gain, just to clean up. */
        if (!this.active) {
            this.resetGain();
        }
        fireChangedActiveState();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        /* per default we deactivate defect elements */
        if (!isOk()) {
            active = false;
        }
    }

    public boolean isOk() {
        return (status == Status.OK);
    }

    public double getInitialGain() {
        return initialGain;
    }

    public void setInitialGain(double initialGain) {
        this.initialGain = initialGain;
    }

    public String getName() {
        return this.name;
    }

    /**
     * notify all listeners, that the active-state changed
     */
    private void fireChangedActiveState() {
        for (MachineElementListener listener : listeners) {
            listener.changedActiveState(this);
        }
    }

    /**
     * notify all listeners that the gain changed
     */
    private void fireChangedGain() {
        for (MachineElementListener listener : listeners) {
            listener.changedGain(this);
        }
    }

    /**
     * adds a listener to the {@link AbstractMachineElement}
     * 
     * @param listener the listener to add
     */
    public void addListener(MachineElementListener listener) {
        this.listeners.add(listener);
    }

    /**
     * removes a listener from the {@link AbstractMachineElement}
     * 
     * @param listener the listener to remove
     */
    public void removeListener(MachineElementListener listener) {
        this.listeners.remove(listener);
    }

    public void resetGain() {
        setGain(getInitialGain());
        setGainError(0.0);
    }

    public void setGain(double gain) {
        this.gain = gain;
        fireChangedGain();
    }

    public double getGain() {
        return gain;
    }

    public double getGainError() {
        return this.gainError;
    }

    public void setGainError(double gainError) {
        this.gainError = gainError;
    }

    public Plane getPlane() {
        return plane;
    }

}
