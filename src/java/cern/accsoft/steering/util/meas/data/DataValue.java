package cern.accsoft.steering.util.meas.data;

import cern.accsoft.steering.util.acc.BeamNumber;

/**
 * this interface represents a object, which is a datavalue, which is read e.g. from a file. In general it represents
 * one line in a datafile. It can correspond e.g. to values for monitors or correctors.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public interface DataValue {

    /**
     * This method returns a key for this data value. It should be created using {@link ElementKeyUtil}.
     * 
     * @return a unique key for this data-value.
     */
    public abstract String getKey();

    /**
     * @return the plane for this data value
     */
    public abstract Plane getPlane();

    /**
     * @return the name of the element of this dataValue
     */
    public abstract String getName();

    /**
     * @return the beam-number this value belongs to
     */
    public abstract BeamNumber getBeam();

    /**
     * @return the status of the element
     */
    public abstract Status getStatus();

}