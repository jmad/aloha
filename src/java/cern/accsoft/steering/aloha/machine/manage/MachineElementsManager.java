/*
 * $Id: MachineElementsManager.java,v 1.3 2009-02-25 18:48:41 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:41 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.machine.manage;

import java.util.Collection;
import java.util.List;

import cern.accsoft.steering.aloha.conf.MonitorSelection;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.meas.data.Data;
import cern.accsoft.steering.aloha.model.adapt.JMadModelAdapter;
import cern.accsoft.steering.jmad.gui.mark.MarkerXProvider;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.util.meas.data.DataValue;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.yasp.MeasuredData;

/**
 * this is the interface for a class, that keeps track of the active elements (correctors and monitors)
 * 
 * @author kfuchsbe
 */
public interface MachineElementsManager extends Data {

    //
    // monitors:
    //

    /**
     * converts an index in the all-monitor list to an index which is valid in the active monitors list. if the monitor
     * of the given index is not active, then it returns null.
     * 
     * @param allMonitorIndex the index in the all-monitors list
     * @return the index related to active monitors if the corrector is a
     */
    Integer convertToActiveMonitorIndex(int allMonitorIndex);

    /**
     * @return all available Monitors
     */
    List<Monitor> getAllMonitors();

    /**
     * collects all working monitors and returns an ArrayList.
     * 
     * @return the functional monitors.
     */
    List<Monitor> getActiveMonitors();

    /**
     * @return the gains of the active monitors
     */
    List<Double> getActiveMonitorGains();

    /**
     * @return the errors on the gains of the active monitors
     */
    List<Double> getActiveMonitorGainErrors();

    /**
     * @return the names of all active monitors
     */
    List<String> getActiveMonitorNames();

    /**
     * @param plane the plane for which to return the active monitor names
     * @return the active monitor names for that plane
     */
    List<String> getActiveMonitorNames(Plane plane);

    /**
     * counts the monitors which have an OK status.
     * 
     * @return the number of monitors with status OK.
     */
    int getActiveMonitorsCount();

    /**
     * @param plane the plane for which to retrieve the count
     * @return the amount of active monitors in the given plane
     */
    int getActiveMonitorsCount(Plane plane);

    /**
     * determines, which monitors are actually selected and returns the keys in an object.
     * 
     * @return The object containing all the selected keys.
     */
    MonitorSelection getActiveMonitorSelection();

    /**
     * activates all monitors, whose key is contained in the {@link MonitorSelection} object. All other monitors will be
     * deselected.
     * 
     * @param monitorSelection the object from which to take the active keys.
     */
    void apply(MonitorSelection monitorSelection);

    //
    // correctors
    //

    /**
     * converts an index in the all-correcors list to an index which is valid in the active correctors list. if the
     * corrector of the given index is not active, then it returns null.
     * 
     * @param allCorrectorIndex the index in the all-correctors list
     * @return the index related to active correctors if the corrector is a
     */
    Integer convertToActiveCorrectorIndex(int allCorrectorIndex);

    /**
     * @return all available Correctors
     */
    List<Corrector> getAllCorrectors();

    /**
     * Collects all active correctors in an ArrayList.
     * 
     * @return the active Correctors
     */
    List<Corrector> getActiveCorrectors();

    /**
     * determines the number of correctors which were activated during the measurement. (= number of StearingFiles)
     * 
     * @return
     */
    int getActiveCorrectorsCount();

    /**
     * @param plane the plane for which to retrieve the count
     * @return the amount of active correctors in the given plane
     */
    int getActiveCorrectorsCount(Plane plane);

    /**
     * @return the names of all active correctors
     */
    List<String> getActiveCorrectorNames();

    /**
     * @return the gains of the active correctors
     */
    List<Double> getActiveCorrectorGains();

    /**
     * @return the errors of the gains of the active correctors
     */
    List<Double> getActiveCorrectorGainErrors();

    /**
     * @param plane the plane for which to get the monitor names
     * @return the names of the active monitors for the given plane
     */
    List<String> getActiveCorrectorNames(Plane plane);

    /**
     * @return true, if the manager already has its correctors, false, if not.
     */
    boolean isFilled();

    /**
     * fills the lists with the initial values from the model
     * 
     * @param model the {@link JMadModel} to use for filling the lists
     */
    void fill(JMadModel model, JMadModelAdapter jMadModelAdapter);

    /**
     * removes all the element-entries.
     */
    void clear();

    //
    // listener-handling
    //

    /**
     * adds a {@link MachineElementsManagerListener}
     * 
     * @param listener the listener to add
     */
    void addListener(MachineElementsManagerListener listener);

    /**
     * removes a {@link MachineElementsManagerListener}
     * 
     * @param listener the listener to remove
     */
    void removeListener(MachineElementsManagerListener listener);

    /**
     * @return the actual displayed Corrector - number (out of the active Correctors)
     */
    int getActiveCorrectorNumber();

    /**
     * @return the actual displayed monitor - number
     */
    int getActiveMonitorNumber();

    /**
     * set the actually displayed corrector-number
     * 
     * @param activeCorrectorNumber
     */
    void setActiveCorrectorNumber(int activeCorrectorNumber);

    /**
     * set the actually displayed monitor-number
     * 
     * @param activeMonitorNumber
     */
    void setActiveMonitorNumber(int activeMonitorNumber);

    /**
     * @return a provider for aloha-charts, which indicates the border between x and y monitors
     */
    MarkerXProvider getMonitorHVBorderProvider();

    /**
     * @return a provider for aloha-charts, which indicates the border between x and y correctors
     */
    MarkerXProvider getCorrectorHVBorderProvider();

    /**
     * if this property is set to true, then no listener is notified, that one of the elements changed its state. This
     * is useful, if a group of states is changed at once. When the value is set back to false, then one event is sent
     * to all listeners.
     * 
     * @param suppressActiveElementsChangedEvent
     */
    void setSuppressActiveElementsChangedEvent(boolean suppressActiveElementsChangedEvent);

    /**
     * sets the status of the monitors correctly and deactivates the monitors, which are not available in at least one
     * dataset.
     * 
     * @param readingDatas the data read from files
     */
    <T extends DataValue> void deactivateUnavailableMonitors(Collection<? extends MeasuredData<T>> readingDatas);

    /**
     * @param plane
     * @return the active monitors for the given plane
     */
    List<Monitor> getActiveMonitors(Plane plane);

    /**
     * @param plane
     * @return the active correctors for the given plane
     */
    List<Corrector> getActiveCorrectors(Plane plane);

    /**
     * resets the gains of all monitors and correctors to their initial value.
     */
    void resetAllGains();

    void setSuppressChangedCorrectorGainsEvent(boolean suppressChangedCorrectorGainsEvent);

    void setSuppressChangedMonitorGainsEvent(boolean suppressChangedMonitorGainsEvent);

}
