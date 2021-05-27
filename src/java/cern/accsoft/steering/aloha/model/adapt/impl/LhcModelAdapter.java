/**
 *
 */
package cern.accsoft.steering.aloha.model.adapt.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cern.accsoft.steering.aloha.model.adapt.JMadModelAdapter;
import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.util.acc.BeamNumber;
import com.google.common.collect.ImmutableMap;

/**
 * This is the model-adapter for LHC models.
 *
 * @author kfuchsbe
 */
public class LhcModelAdapter implements JMadModelAdapter {

    private static final Map<String, BeamNumber> SEQUENCE_BEAMS = ImmutableMap.of( //
            "lhcb1", BeamNumber.BEAM_1, //
            "lhcb2", BeamNumber.BEAM_2 //
    );

    @Override
    public boolean appliesTo(JMadModel model) {
        /*
         * XXX maybe to simple decision
         */
        if (lowerModelName(model).startsWith("lhc")) {
            return true;
        } else {
            return false;
        }
    }

    private String lowerModelName(JMadModel model) {
        return model.getName().toLowerCase();
    }

    @Override
    public List<String> getMonitorRegexps() {
        List<String> regexps = new ArrayList<String>();
        regexps.add("BPM.*");
        return regexps;
    }

    @Override
    public BeamNumber beamNumberFor(SequenceDefinition sequenceDefinition) {
        String sequenceName = sequenceDefinition.getName();
        checkState(SEQUENCE_BEAMS.containsKey(sequenceName), "unknown sequence: %s", sequenceName);
        return SEQUENCE_BEAMS.get(sequenceName);
    }

    @Override
    public RangeDefinition defaultRangeDefinitionFor(JMadModelDefinition modelDefinition, BeamNumber beam) {
        return modelDefinition.getSequenceDefinition("lhcb" + beam.getNumber()).getDefaultRangeDefinition();
    }
}
