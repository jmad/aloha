/**
 * 
 */
package cern.accsoft.steering.aloha.model.adapt.impl;

import cern.accsoft.steering.aloha.model.adapt.JMadModelAdapter;
import cern.accsoft.steering.jmad.model.JMadModel;

import java.util.ArrayList;
import java.util.List;

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
        List<String> regexps = new ArrayList<String>();
        regexps.add("BPK.*");
        regexps.add("BPM.*");
        regexps.add("BPCK.*");
        return regexps;
    }

}
