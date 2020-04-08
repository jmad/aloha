package cern.accsoft.steering.aloha.display.annotate;

import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.util.meas.data.Plane;

import java.util.List;

public enum DataLength {
	ACTIVE_MONITORS {
		@Override
		public List<String> getLabels(
				MachineElementsManager machineElementsManager,
				ModelOpticsData modelData, Plane plane) {
			return machineElementsManager.getActiveMonitorNames(plane);
		}

	},
	ACTIVE_CORRECTORS {
		@Override
		public List<String> getLabels(
				MachineElementsManager machineElementsManager,
				ModelOpticsData modelData, Plane plane) {
			return machineElementsManager.getActiveCorrectorNames(plane);
		}
	},
	ALL_ELEMENTS {
		@Override
		public List<String> getLabels(
				MachineElementsManager machineElementsManager,
				ModelOpticsData modelData, Plane plane) {
			return modelData.getAllNames();
		}
	};

	public abstract List<String> getLabels(
			MachineElementsManager machineElementsManager,
			ModelOpticsData modelData, Plane plane);
}
