package cern.accsoft.steering.aloha.display;

import cern.accsoft.steering.aloha.meas.data.Data;

import java.util.List;

public interface LabelMapper<T extends Data> {

	public List<String> getLabels(T data);
}
