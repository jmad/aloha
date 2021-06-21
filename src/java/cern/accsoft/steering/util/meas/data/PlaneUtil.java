package cern.accsoft.steering.util.meas.data;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

public final class PlaneUtil {
    private PlaneUtil() {
        throw new UnsupportedOperationException("static only");
    }

    public static <T> Map<Plane, T> planeMap(Supplier<T> supplier) {
        return Arrays.stream(Plane.values()) //
                .collect(toMap(identity(), p -> supplier.get()));
    }
}
