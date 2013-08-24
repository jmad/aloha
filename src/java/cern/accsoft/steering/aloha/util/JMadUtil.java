/**
 * 
 */
package cern.accsoft.steering.aloha.util;

import cern.accsoft.steering.jmad.domain.types.enums.JMadPlane;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * @author kfuchsbe
 * 
 */
public class JMadUtil {

	private JMadUtil() {
		/* only static methods */
	}

	/**
	 * converts the aloha-plane to the plane enum of the model
	 * 
	 * @param plane
	 *            the plane to convert
	 * @return the model plane
	 */
	public final static JMadPlane convertPlane(Plane plane) {
		return ((plane == Plane.HORIZONTAL) ? JMadPlane.H : JMadPlane.V);
	}

}
