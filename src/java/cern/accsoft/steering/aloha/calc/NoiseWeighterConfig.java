package cern.accsoft.steering.aloha.calc;

public interface NoiseWeighterConfig {

	/**
	 * @param activeNoise the activeNoise to set
	 */
	public abstract void setActiveNoise(boolean activeNoise);

	/**
	 * @return the activeNoise
	 */
	public abstract boolean isActiveNoise();

	/**
	 * @param noiseLimit the noiseLimit to set
	 */
	public abstract void setNoiseLimit(double noiseLimit);

	/**
	 * @return the noiseLimit
	 */
	public abstract double getNoiseLimit();

}