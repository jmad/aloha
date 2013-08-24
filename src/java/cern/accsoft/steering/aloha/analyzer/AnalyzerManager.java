/**
 * 
 */
package cern.accsoft.steering.aloha.analyzer;

import java.util.List;

import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.AnalyzerFactory;

/**
 * The interface of a class, that keeps track of all {@link AnalyzerFactory}s
 * and uses them to create Analyzers for all the measurement.
 * 
 * @author kfuchsbe
 * 
 */
public interface AnalyzerManager {

	/**
	 * add ana {@link AnalyzerFactory} to the manager
	 * 
	 * @param factory
	 *            the factory to add
	 */
	public void addAnalyzerFactory(AnalyzerFactory factory);

	/**
	 * creates all available analyzers
	 */
	public List<Analyzer> createAnalyzers(Measurement measurement);
}
