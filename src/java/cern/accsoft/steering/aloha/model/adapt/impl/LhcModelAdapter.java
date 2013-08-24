/**
 * 
 */
package cern.accsoft.steering.aloha.model.adapt.impl;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.model.adapt.JMadModelAdapter;
import cern.accsoft.steering.jmad.model.JMadModel;

/**
 * This is the model-adapter for LHC models.
 * 
 * @author kfuchsbe
 * 
 */
public class LhcModelAdapter implements JMadModelAdapter {

	@Override
	public boolean appliesTo(JMadModel model) {
		/*
		 * XXX maybe to simple decision
		 */
		if (model.getName().toLowerCase().startsWith("lhc")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> getMonitorRegexps() {
		List<String> regexps = new ArrayList<String>();
		regexps.add("BPM.*");
		return regexps;
	}

}
