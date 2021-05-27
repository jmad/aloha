package cern.accsoft.steering.aloha.model.adapt.impl;

import static java.lang.Integer.parseInt;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cern.accsoft.steering.aloha.model.adapt.JMadModelAdapter;
import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.util.acc.BeamNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PsbModelAdapter implements JMadModelAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PsbModelAdapter.class);

    @Override
    public boolean appliesTo(JMadModel model) {
        String modelName = model.getName().toLowerCase();
        return modelName.startsWith("psb") || modelName.contains("btbtp") || modelName.contains("btbtm");
    }

    @Override
    public List<String> getMonitorRegexps() {
        return Collections.singletonList(".*BPM.*");
    }

    private static Optional<BeamNumber> beamNumberByName(SequenceDefinition sequenceDefinition) {
        Pattern psbBeamPattern = Pattern.compile("(psb|bt|bi)(\\d)", Pattern.CASE_INSENSITIVE);
        String sequenceName = sequenceDefinition.getName();
        Matcher matcher = psbBeamPattern.matcher(sequenceName);
        if (matcher.find()) {
            BeamNumber beamNumber = BeamNumber.fromInt(parseInt(matcher.group(2)));
            return Optional.of(beamNumber);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public BeamNumber beamNumberFor(SequenceDefinition sequenceDefinition) {
        return beamNumberByName(sequenceDefinition).orElse(BeamNumber.BEAM_1);
    }

    @Override
    public RangeDefinition defaultRangeDefinitionFor(JMadModelDefinition modelDefinition, BeamNumber beam) {
        for (SequenceDefinition sequenceDefinition : modelDefinition.getSequenceDefinitions()) {
            Optional<BeamNumber> sequenceBeam = beamNumberByName(sequenceDefinition);
            if (sequenceBeam.isPresent() && sequenceBeam.get() == beam) {
                LOGGER.info("Using sequence {} for {}.", sequenceDefinition.getName(), beam);
                return sequenceDefinition.getDefaultRangeDefinition();
            }
        }
        LOGGER.warn("No specific sequence found for {} in model {} !", beam, modelDefinition.getName());
        return modelDefinition.getDefaultRangeDefinition();
    }
}
