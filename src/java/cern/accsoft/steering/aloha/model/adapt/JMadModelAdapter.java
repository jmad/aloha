/**
 * 
 */
package cern.accsoft.steering.aloha.model.adapt;

import cern.accsoft.steering.jmad.model.JMadModel;

import java.util.List;

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
    public boolean appliesTo(JMadModel model);

    /**
     * @return A list of regular expressions which represents all the monitors in the model.
     */
    public List<String> getMonitorRegexps();

}
