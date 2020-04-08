/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas.data;

import cern.accsoft.steering.util.meas.data.AbstractDataValue;
import cern.accsoft.steering.util.meas.data.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * the implementation of a multiturn data - value
 * 
 * @author kfuchsbe
 * 
 */
public class MultiturnDataValueImpl extends AbstractDataValue implements
		MultiturnDataValue {

	private Map<MultiturnVar, Double> doubleValues = new HashMap<MultiturnVar, Double>();

	private Status status;

	public void setDoubleValue(MultiturnVar key, Double value) {
		this.doubleValues.put(key, value);
	}

	@Override
	public Double getValue(MultiturnVar column) {
		return this.doubleValues.get(column);
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public boolean isOk() {
		return Status.OK.equals(this.status);
	}

}
