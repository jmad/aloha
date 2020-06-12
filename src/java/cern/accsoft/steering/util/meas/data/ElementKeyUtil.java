package cern.accsoft.steering.util.meas.data;

import cern.accsoft.steering.util.acc.BeamNumber;

public class ElementKeyUtil {

    private ElementKeyUtil() {
        // only static methods
    }

    public final static String composeKey(String name, Plane plane, BeamNumber beam) {
        return name + "." + plane.toString() + "." + beam.toString();
    }

    public final static String composeKey(String name, Plane plane) {
        return name + "." + plane.toString();
    }

}
