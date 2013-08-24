/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas;

import cern.accsoft.steering.aloha.meas.GenericMeasurement;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDifferenceData;

/**
 * @author kfuchsbe
 * 
 */
public interface MultiturnMeasurement extends GenericMeasurement<MultiturnData> {

	public MultiturnDifferenceData getDiffData();

}
