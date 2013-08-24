/**
 * 
 */
package cern.accsoft.steering.aloha.conf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * represents a set of active monitors. This class is intended to be serialized to xml.
 * 
 * @author kaifox
 */
@XStreamAlias("monitor-selection")
public class MonitorSelection {

    /** contains the keys, which are currently active. */
    @XStreamAlias("active-keys")
    private Set<String> activeKeys = new HashSet<String>();

    /**
     * adds a new key to the active ones.
     * 
     * @param key the key to add
     */
    public void addActiveKey(String key) {
        this.activeKeys.add(key);
    }

    public Set<String> getActiveKeys() {
        return Collections.unmodifiableSet(activeKeys);
    }

}
