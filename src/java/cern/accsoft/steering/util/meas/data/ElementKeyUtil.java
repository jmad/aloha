package cern.accsoft.steering.util.meas.data;

public class ElementKeyUtil {

    private ElementKeyUtil() {
        // only static methods
    }

    public static String composeKey(String name, Plane plane) {
        return name + "." + plane.toString();
    }

}
