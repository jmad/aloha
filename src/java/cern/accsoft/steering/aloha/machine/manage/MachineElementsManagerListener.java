package cern.accsoft.steering.aloha.machine.manage;

import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;

public interface MachineElementsManagerListener {

	/**
	 * fired when the active Elements changed
	 */
	public void changedActiveElements();

	/**
	 * fired when the elements changed.
	 */
	public void changedElements();

	/**
	 * fired, when the active corrector has changed.
	 * 
	 * @param number
	 *            the number of the corrector (column in Response-matrix)
	 * @param corrector
	 *            the Corrector-instance.
	 */
	public void changedActiveCorrector(int number, Corrector corrector);

	/**
	 * fired, when the active monitor has changed
	 * 
	 * @param number
	 *            the number of the monitor (row in Response-Matrix)
	 * @param monitor
	 *            the Monitor-instance
	 */
	public void changedActiveMonitor(int number, Monitor monitor);

	/**
	 * fired when at least one corrector gain changed
	 */
	public void changedCorrectorGains();

	/**
	 * fired when at least one monitor gain changed
	 */
	public void changedMonitorGains();
}
