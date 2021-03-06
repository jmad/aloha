/**
 * 
 */
package cern.accsoft.steering.aloha.model.adapt;

import java.util.List;

import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.util.acc.BeamNumber;

/**
 * This interface provides some special knowledge of one (or more) specific jmad-model, which is not directly in the
 * model, but needed e.g. for aloha calculations.
 * 
 * @author kfuchsbe
 */
public interface JMadModelAdapter {

    /**
     * This method must return true, if this adapter is intended to be used with the given model.
     * 
     * @param model the model for which we want to have an adapter
     * @return true if applicable, false if not
     */
    boolean appliesTo(JMadModel model);

    /**
     * @return A list of regular expressions which represents all the monitors in the model.
     */
    List<String> getMonitorRegexps();

    /**
     * @return the BeamNumber for the given sequence
     */
    BeamNumber beamNumberFor(SequenceDefinition sequenceDefinition);

    /**
     * @return the default range for the given beam
     */
    RangeDefinition defaultRangeDefinitionFor(JMadModelDefinition modelDefinition, BeamNumber beam);
}
