/*
 * $Id: NoiseWeighterImpl.java,v 1.1 2008-12-19 13:55:27 kfuchsbe Exp $
 * 
 * $Date: 2008-12-19 13:55:27 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kfuchsbe
 * 
 */
public class NoiseWeighterImpl implements NoiseWeighter, NoiseWeighterConfig {

	/** below this limit the noise is treated as zero */
	private double noiseLimit = 1e-5;

	/** use monitor-noise values to normalize the response (if available) */
	private boolean activeNoise = true;

	@Override
	public final double calcNoisyValue(double value, double noise) {
		if (isActiveNoise()) {
			if (noise > getNoiseLimit()) {
				return value / noise;
			} else {
				return value;
			}
		} else {
			return value;
		}
	}

	@Override
	public void setActiveNoise(boolean activeNoise) {
		this.activeNoise = activeNoise;
	}

	@Override
	public boolean isActiveNoise() {
		return activeNoise;
	}

	@Override
	public void setNoiseLimit(double noiseLimit) {
		this.noiseLimit = noiseLimit;
	}

	@Override
	public double getNoiseLimit() {
		return noiseLimit;
	}

	@Override
	public List<Double> calcNoisyValues(List<Double> values,
			List<Double> noiseValues) {
		if (values.size() != noiseValues.size()) {
			throw new IllegalArgumentException(
					"The two lists must be of same size!");
		}
		List<Double> result = new ArrayList<Double>(values.size());
		for (int i = 0; i < values.size(); i++) {
			result.add(calcNoisyValue(values.get(i), noiseValues.get(i)));
		}
		return result;
	}

}
