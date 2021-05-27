/**
 * 
 */
package cern.accsoft.steering.aloha.model.adapt.impl;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.aloha.model.adapt.JMadModelAdapter;
import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.util.acc.BeamNumber;

/**
 * The specific datas for transfer line models
 * 
 * @author kaifox
 */
public class TiModelAdapter implements JMadModelAdapter {

    @Override
    public boolean appliesTo(JMadModel model) {
        /*
         * XXX maybe to simple decision
         */
        String modelName = model.getName().toLowerCase();
        if (modelName.startsWith("ti") || modelName.startsWith("longti")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> getMonitorRegexps() {
        List<String> regexps = new ArrayList<>();
        regexps.add("BPK.*");
        regexps.add("BPM.*");
        regexps.add("BPCK.*");
        return regexps;
    }

    @Override
    public BeamNumber beamNumberFor(SequenceDefinition sequenceDefinition) {
        return BeamNumber.BEAM_1;
    }

    @Override
    public RangeDefinition defaultRangeDefinitionFor(JMadModelDefinition modelDefinition, BeamNumber beam) {
        return modelDefinition.getDefaultRangeDefinition();
    }
}
