/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import Jama.Matrix;
import cern.accsoft.steering.aloha.meas.data.DynamicData;

/**
 * this is an interface for a class calculating the response-matrix from a
 * jmad-model delegate
 * 
 * @author kfuchsbe
 * 
 */
public interface ModelKickResponseData extends DynamicData {

	/**
	 * return the calculated response-matrix according to the selected
	 * correctors and monitors. The recalculation is automatically triggered, if
	 * the model became dirty or the selected elements changed.
	 * 
	 * @return the last calculated response-matrix.
	 */
	public Matrix getResponseMatrix();
}
