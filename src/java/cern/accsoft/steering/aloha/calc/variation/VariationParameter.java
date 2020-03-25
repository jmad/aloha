package cern.accsoft.steering.aloha.calc.variation;

/**
 * the interface, which represents a parameter, which is varied by the fitting algorithm. It stores an offset to the
 * original parameters. There is no method to set the actual value directly. This is because the actual value may be
 * different for different models.
 * 
 * @author kfuchsbe
 */
public interface VariationParameter {

    /**
     * resets the actual value to the initial one.
     */
    public abstract void reset();

    /**
     * reverts the actual value to the last one. this only works once!
     */
    public abstract void undo();

    /**
     * @return the actual value
     */
    public abstract double getActualOffset();

    /**
     * set the actual offset to the initial value
     * 
     * @param actualOffset the value to set
     */
    public abstract void setActualOffset(double actualOffset);

    /**
     * returns the key, for storage in an HashMap
     * 
     * @return the key for this parameter
     */
    public abstract String getKey();

    /**
     * @return the name
     */
    public abstract String getName();

    /**
     * @return the initial value for this parameter
     */
    public abstract double getInitialOffset();

    /**
     * @return the total value for the actively selected measurement
     */
    public abstract Double getActiveMeasurementAbsoluteValue();

    public abstract Double getActiveMeasurementInitialValue();

    public abstract double getOffsetChange();

    public abstract Double getActiveMeasurementRelativeChange();

    /**
     * sets the initial value
     * 
     * @param initialOffset the value to set
     */
    public abstract void setInitialOffset(double initialOffset);

    /**
     * get the delta, which is used by a Calculator to vary the parameter in order to calculate the differential
     * quotient.
     * 
     * @return the delta
     */
    public abstract double getDelta();

    /**
     * sets the delta
     * 
     * @param delta the new delta
     * @see #getDelta()
     */
    public abstract void setDelta(double delta);

    /**
     * adds the given value to the parameter. (may be scaled internally)
     * 
     * @param value the value that shall be added
     */
    public abstract void addValueScaled(double value);

    /**
     * adds the delta-value to the parameter. This is used, when calculating the gradient. It has to be used together
     * with undo().
     */
    public abstract void addDelta();

    /**
     * sets the factor which is applied when updating the data for this parameter. A lower factor means slower
     * convergence. a factor 1 means applying the whole calculated value.
     * 
     * @param factor
     */
    public abstract void setUpdateFactor(double factor);

    /**
     * @return the update factor
     */
    public abstract double getUpdateFactor();

    /**
     * The sensity of the parameter during the last fit-iteration.
     * 
     * @param sensity the sensity to set
     */
    public abstract void setSensity(double sensity);

    /**
     * @return the sensity of the parameter
     */
    public abstract double getSensity();

    /**
     * set the error for the parameter from the last calculatione
     * 
     * @param error the error from the last calculation
     */
    public abstract void setError(double error);

    /**
     * @return the error resulting from the last fit iteration
     */
    public abstract double getError();

}