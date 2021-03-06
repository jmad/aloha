/**
 *
 */
package cern.accsoft.steering.aloha.read.yasp;

/**
 * some special methods/constants related to yasp
 *
 * @author kfuchsbe
 */
public final class YaspUtil {

    private YaspUtil() {
        throw new UnsupportedOperationException("static only");
    }

    /**
     * the conversion factor from positions in yasp [um] to positions in the
     * model. [m]
     */
    public static final double TO_MODEL_CONVERSION_FACTOR = 1e-6;

    /**
     * converts the given value to units of the model.
     *
     * @param value the value to convert
     * @return the converted value
     */
    public static double toModel(double value) {
        return TO_MODEL_CONVERSION_FACTOR * value;
    }
}
