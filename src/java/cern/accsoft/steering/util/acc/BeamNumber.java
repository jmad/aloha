/*
 * $Id: BeamNumber.java,v 1.3 2008-09-19 16:49:41 kfuchsbe Exp $
 * 
 * $Date: 2008-09-19 16:49:41 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.util.acc;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is intended to specify the beam number in a typesafe way.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public enum BeamNumber {

    BEAM_1(1, ".B1", "BR1.", "B1", "Beam 1"),
    BEAM_2(2, ".B2", "BR2.", "B2", "Beam 2"),
    BEAM_3(3, ".B3", "BR3.", "B3", "Beam 3"),
    BEAM_4(4, ".B4", "BR4.", "B4", "Beam 4");

    private final static String DOROS_ELEMETS_SUFFIX = "D";

    /** a map to find a beamNumber by its integer value */
    private final static Map<Integer, BeamNumber> intMap = new HashMap<Integer, BeamNumber>();
    static {
        for (BeamNumber beamNumber : BeamNumber.values()) {
            intMap.put(beamNumber.getNumber(), beamNumber);
        }
    }

    /** the suffix of the monitor name, which indicates the baem */
    private final String elementNameSuffix;

    /** the potential prefix for an element name */
    /* XXX (KF) this is a hack for the booster elements */
    private final String elementNamePrefix;

    /** The string as used in lsa to identify this beam */
    private final String lsaName;

    /** a more fancy name to display */
    private final String name;

    /** simply the number as integer */
    private final int number;

    /**
     * the constructor to enforce to set the suffix.
     * 
     * @param elementNameSuffix
     */
    private BeamNumber(int number, String elementNameSuffix, String elementNamePrefix, String lsaName, String name) {
        this.number = number;
        this.elementNameSuffix = elementNameSuffix;
        this.elementNamePrefix = elementNamePrefix;
        this.lsaName = lsaName;
        this.name = name;
    }

    /**
     * find the correct enum-value depending on a monitor-name.
     * 
     * @param elementName the name for which to find the enum value
     * @return the enum value if found, otherwise null
     */
    public static BeamNumber fromElementName(String elementName) {
        for (BeamNumber beam : BeamNumber.values()) {
            if (elementName.toUpperCase().endsWith(beam.elementNameSuffix.toUpperCase())
                    || elementName.toUpperCase().endsWith(beam.elementNameSuffix.toUpperCase() + DOROS_ELEMETS_SUFFIX)
                    || elementName.toUpperCase().startsWith(beam.elementNamePrefix.toUpperCase())) {
                return beam;
            }
        }
        return null;
    }

    public final static BeamNumber fromInt(int intValue) {
        BeamNumber beamNumber = intMap.get(intValue);
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

    public String getLsaName() {
        return lsaName;
    }
}
