/**
 * 
 */
package cern.accsoft.steering.aloha.analyzer;

import cern.accsoft.steering.aloha.meas.Measurement;

/**
 * This is the generic version of an analyze. it provides an additional method
 * to set the measurement of a specific type.
 * 
 * @author kfuchsbe
 * 
 */
public interface GenericAnalyzer<T extends Measurement> extends Analyzer {

	/**
	 * set the measurement to analyze.
	 * 
	 * @param measurement
	 */
	public void setMeasurement(T measurement);
}
