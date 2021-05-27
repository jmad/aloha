/*
 * $Id: BeamNumber.java,v 1.3 2008-09-19 16:49:41 kfuchsbe Exp $
 *
 * $Date: 2008-09-19 16:49:41 $ $Revision: 1.3 $ $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.util.acc;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;

import java.util.Map;

/**
 * This class is intended to specify the beam number in a typesafe way.
 *
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public enum BeamNumber {

    BEAM_1(1, "Beam 1"), BEAM_2(2, "Beam 2"), BEAM_3(3, "Beam 3"), BEAM_4(4, "Beam 4");

    private final static Map<Integer, BeamNumber> INT_MAP = stream(BeamNumber.values())
            .collect(toImmutableMap(BeamNumber::getNumber, identity()));

    /**
     * a more fancy name to display
     */
    private final String name;

    /**
     * simply the number as integer
     */
    private final int number;

    BeamNumber(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public static BeamNumber fromInt(int intValue) {
        BeamNumber beamNumber = INT_MAP.get(intValue);
        if (beamNumber == null) {
            throw new IllegalArgumentException("Beam for intValue " + intValue + " is not defined.");
        }
        return beamNumber;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public boolean equals(int number) {
        return (this.number == number);
    }

    public int getNumber() {
        return this.number;
    }
}
