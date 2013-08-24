package cern.accsoft.steering.aloha.display;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.Data;

public interface ValidityMapper<T extends Data> {

	public List<Boolean> getValidityValues(T data);
}
