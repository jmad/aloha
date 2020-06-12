package cern.accsoft.steering.util.meas.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * the enum type, representing a transverse plane.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public enum Plane implements Serializable {
    HORIZONTAL(0, "H"),
    VERTICAL(1, "V");

    /** map to speedup searching by tag */
    private final static Map<String, Plane> tagMap = new HashMap<String, Plane>();

    /** a map to find by integer value */
    private final static Map<Integer, Plane> intMap = new HashMap<Integer, Plane>();

    static {
        for (Plane plane : Plane.values()) {
            if (plane.getTag() != null) {
                tagMap.put(plane.getTag(), plane);
            }
            if (plane.getIntValue() != null) {
                intMap.put(plane.getIntValue(), plane);
            }
        }
    }

    /** the tag (which is used e.g. in the database) */
    private final String tag;

    /** The integer value which can represent this plane */
    private final Integer intValue;

    /** the constructor, which enforces to provide a tag */
    private Plane(Integer intValue, String tag) {
        this.intValue = intValue;
        this.tag = tag;
    }

    /**
     * @return the tag (which e.g. is used in the database) of the plane
     */
    public String getTag() {
        return tag;
    }

    public Integer getIntValue() {
        return this.intValue;
    }

    @Override
    public String toString() {
        return getTag();
    }

    /**
     * compares the plane with the tag
     * 
     * @param tag the tag to compare to
     * @return true, if the tag corresponds to the plane, false if not
     */
    public boolean equals(String tag) {
        return getTag().equals(tag);
    }

    /**
     * returns the plane, which corresponds to the given tag
     * 
     * @param tag the tag for which to find the plane
     * @return the plane
     */
    public final static Plane fromTag(String tag) {
        return tagMap.get(tag);
    }

    /**
     * returns the plane which corresponds to the given int value
     * 
     * @param intValue the integer value for which to get the plane
     * @return the plane
     */
    public final static Plane fromInt(int intValue) {
        return intMap.get(intValue);
    }

}