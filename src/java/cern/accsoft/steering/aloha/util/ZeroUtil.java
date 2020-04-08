/**
 * 
 */
package cern.accsoft.steering.aloha.util;

/**
 * simple util methods
 * 
 * @author kfuchsbe
 * 
 */
public class ZeroUtil {

	private final static double ZERO_LIMIT = 1e-10;

	private ZeroUtil() {
		/* only static methods */
	}

	public final static boolean isZero(double value) {
		return (Math.abs(value) < ZERO_LIMIT);
	}

	public final static boolean isZero(Double value) {
		return ((value != null) && isZero(value.doubleValue()));
	}
}
