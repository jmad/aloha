package cern.accsoft.steering.aloha.display;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.Data;

public interface LabelMapper<T extends Data> {

	public List<String> getLabels(T data);
}
