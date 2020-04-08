/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas.data;

import cern.accsoft.steering.util.meas.data.DataValue;
import cern.accsoft.steering.util.meas.data.Status;

/**
 * A data-value for multiturn data.
 * 
 * @author kfuchsbe
 * 
 */
public interface MultiturnDataValue extends DataValue {
	
	public Double getValue(MultiturnVar column);
	
	public Status getStatus();
	
	public boolean isOk();

}
