package cern.accsoft.steering.aloha.machine.manage;

import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;

public interface MachineElementsManagerListener {

    /**
     * fired when the active Elements changed
     */
    default void changedActiveElements() {
    }

    /**
     * fired when the elements changed.
     */
    default void changedElements() {
    }

    /**
     * fired, when the active corrector has changed.
     *
     * @param number    the number of the corrector (column in Response-matrix)
     * @param corrector the Corrector-instance.
     */
    default void changedActiveCorrector(int number, Corrector corrector) {
    }

    /**
     * fired, when the active monitor has changed
     *
     * @param number  the number of the monitor (row in Response-Matrix)
     * @param monitor the Monitor-instance
     */
    default void changedActiveMonitor(int number, Monitor monitor) {
    }

    /**
     * fired when at least one corrector gain changed
     */
    default void changedCorrectorGains() {
    }

    /**
     * fired when at least one monitor gain changed
     */
    default void changedMonitorGains() {
    }
}
