package cern.accsoft.steering.aloha.display;

import cern.accsoft.steering.aloha.meas.data.Data;

import java.util.List;

public interface ValidityMapper<T extends Data> {

	public List<Boolean> getValidityValues(T data);
}
