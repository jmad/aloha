/**
 * 
 */
package cern.accsoft.steering.aloha.analyzer;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.AnalyzerFactory;

/**
 * This is the implementation of the {@link AnalyzerManager}
 * 
 * @author kfuchsbe
 * 
 */
public class AnalyzerManagerImpl implements AnalyzerManager {

	/** all the available */
	private List<AnalyzerFactory> analyzerFactories = new ArrayList<AnalyzerFactory>();

	@Override
	public List<Analyzer> createAnalyzers(Measurement measurement) {
		List<Analyzer> analyzers = new ArrayList<Analyzer>();
		for (AnalyzerFactory factory : this.analyzerFactories) {
			analyzers.addAll(factory.createAnalyzers(measurement));
		}
		return analyzers;
	}

	@Override
	public void addAnalyzerFactory(AnalyzerFactory factory) {
		this.analyzerFactories.add(factory);
	}

}
