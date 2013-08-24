package cern.accsoft.steering.aloha.meas.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManagerListener;

/**
 * this class is the superclass for all Data-classes, which need to be aware of
 * the actually selected correctors/monitors in order to (for example) produce
 * matrices or vectors in the correct dimensions.
 * 
 * @author kfuchsbe
 * 
 */
public abstract class AbstractDynamicData implements DynamicData,
		MachineElementsManagerAware {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(AbstractDynamicData.class);

	/** the manager, which keeps track of actual active elements. To be injected */
	private MachineElementsManager machineElementsManager;

	/**
	 * indicates, if the actually calculated data is compatible with the
	 * actually selected monitors/correctors.
	 */
	private boolean dirty = true;

	/** the listeners */
	private List<DynamicDataListener> listeners = new ArrayList<DynamicDataListener>();

	/**
	 * @return true, if the actual response-matrix is not corresponding to the
	 *         actual selected monitors/corrector, false otherwise.
	 */
	protected final boolean isDirty() {
		return this.dirty;
	}

	/**
	 * sets the dirty-flag to the given value.
	 * 
	 * @param dirty
	 *            the new value for the dirty-flag
	 */
	protected final void setDirty(boolean dirty) {
		this.dirty = dirty;
		if (this.dirty) {
			fireChangedData();
		}
	}

	/**
	 * ensures, that the lists are up to date
	 * 
	 * This method should be called before any value, which needs calculation is
	 * returned
	 */
	protected final void ensureUpToDate() {
		if (isDirty()) {
			try {
				calc();
			} catch (InconsistentDataException e) {
				logger.error("Error while calculating new values.", e);
			}
			setDirty(false);
		}
	}

	/**
	 * This method must be implemented by subclasses to calc new values which
	 * then can be retrieved.
	 * 
	 * @throws InconsistentDataException
	 */
	protected abstract void calc() throws InconsistentDataException;

	/**
	 * @return the active correctors
	 */
	protected final List<Corrector> getActiveCorrectors() {
		if (getMachineElementsManager() == null) {
			logger
					.warn("machineElementsManager not set. Maybe configuratione error!");
			return new ArrayList<Corrector>();
		}
		return getMachineElementsManager().getActiveCorrectors();
	}

	/**
	 * @return the active Monitors
	 */
	protected final List<Monitor> getActiveMonitors() {
		if (getMachineElementsManager() == null) {
			logger
					.warn("machineElementsManager not set. Maybe configuratione error!");
			return new ArrayList<Monitor>();
		}
		return getMachineElementsManager().getActiveMonitors();
	}

	/**
	 * @param machineElementsManager
	 *            the machineElementsManager to set
	 */
	@Override
	public final void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
		this.machineElementsManager
				.addListener(new MachineElementsManagerListener() {

					@Override
					public void changedActiveElements() {
						setDirty(true);
					}

					@Override
					public void changedActiveCorrector(int number,
							Corrector corrector) {
						/* do nothing */
					}

					@Override
					public void changedActiveMonitor(int number, Monitor monitor) {
						/* do nothing */
					}

					@Override
					public void changedElements() {
						setDirty(true);
					}

					@Override
					public void changedCorrectorGains() {
						setDirty(true);
					}

					@Override
					public void changedMonitorGains() {
						setDirty(true);
					}
				});
	}

	/**
	 * @return the machineElementsManager
	 */
	protected final MachineElementsManager getMachineElementsManager() {
		if (this.machineElementsManager == null) {
			logger.warn(MachineElementsManager.class.toString()
					+ " not set! - ought to be injected.");
		}
		return machineElementsManager;
	}

	/**
	 * notify all listeners, that the data has changed.
	 */
	private final void fireChangedData() {
		for (DynamicDataListener listener : this.listeners) {
			listener.becameDirty();
		}
	}

	@Override
	public final void addListener(DynamicDataListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public final void removeListener(DynamicDataListener listener) {
		this.removeListener(listener);
	}

}