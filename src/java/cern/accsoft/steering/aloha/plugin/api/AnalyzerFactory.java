/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.api;

import java.util.List;

import cern.accsoft.steering.aloha.analyzer.Analyzer;
import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.jdve.viewer.DVView;

/**
 * This is the interface for a class, that can create Analyzers. An Analyzer is
 * in principle a small plugin component that defines one {@link DVView} in a
 * {@link DisplaySet}.
 * 
 * @author kfuchsbe
 * 
 */
public interface AnalyzerFactory extends AlohaPlugin {

	/**
	 * an implementation of this method can return an arbitrary amount of
	 * analyzers for the given measurement. They all will be plugged into every
	 * {@link DisplaySet} for this measurment.
	 * 
	 * @param measurement
	 * @return
	 */
	public List<Analyzer> createAnalyzers(Measurement measurement);

}
