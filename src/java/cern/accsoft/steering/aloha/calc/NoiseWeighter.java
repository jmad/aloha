/*
 * $Id: NoiseWeighter.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ $Revision: 1.1 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc;

import java.util.List;

/**
 * simple interface which calculates a value weighted by the noise, according to the given configuration
 * 
 * @author kfuchsbe
 */
public interface NoiseWeighter {

    /**
     * calculates the value weighted by the given noise.
     * 
     * @param value the value for which to calculate the noise
     * @param noise the noise for the value
     * @return the value weighted by the noise.
     */
    double calcNoisyValue(double value, double noise);

    boolean isActiveNoise();

    List<Double> calcNoisyValues(List<Double> values, List<Double> noiseValues);

}
