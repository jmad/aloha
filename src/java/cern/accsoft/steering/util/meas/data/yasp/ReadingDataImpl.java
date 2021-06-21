/*
 * $Id: ReadingDataImpl.java,v 1.3 2009-03-16 16:38:12 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:12 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.util.meas.data.yasp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cern.accsoft.steering.util.meas.data.ElementKeyUtil;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * this is the basic implementation of the {@link ReadingData} interface.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class ReadingDataImpl implements ReadingData {

    /** the values for the correctors */
    private Map<String, CorrectorValue> correctors = new LinkedHashMap<>();

    /** the values for the monitors */
    private Map<String, MonitorValue> monitors = new LinkedHashMap<>();

    /** the header of this reading data */
    private YaspHeader yaspHeader;

    @Override
    public CorrectorValue getCorrectorValue(String key) {
        return this.correctors.get(key);
    }

    @Override
    public Collection<CorrectorValue> getCorrectorValues() {
        return this.correctors.values();
    }

    @Override
    public MonitorValue getMonitorValue(String key) {
        return this.monitors.get(key);
    }

    @Override
    public Collection<MonitorValue> getMonitorValues() {
        return this.monitors.values();
    }

    /**
     * adds the given monitorValue to the ReadingData
     * 
     * @param monitorValue the {@link MonitorValue} to add
     */
    public void add(MonitorValue monitorValue) {
        monitors.put(monitorValue.getKey(), monitorValue);
    }

    /**
     * adds a correctorValue to the ReadingData
     * 
     * @param correctorValue
     */
    public void add(CorrectorValue correctorValue) {
        correctors.put(correctorValue.getKey(), correctorValue);
    }

    @Override
    public List<CorrectorValue> getCorrectorValues(List<String> correctorNames, Plane plane) {
        List<CorrectorValue> correctorValues = new ArrayList<>(correctorNames.size());
        for (String name : correctorNames) {
            String key = ElementKeyUtil.composeKey(name, plane);
            /*
             * be aware: this may add NULL - values. (on perpose, since the length shall be correct!)
             */
            correctorValues.add(getCorrectorValue(key));
        }
        return correctorValues;
    }

    @Override
    public List<MonitorValue> getMonitorValues(List<String> monitorNames, Plane plane) {
        List<MonitorValue> monitorValues = new ArrayList<>(monitorNames.size());
        for (String name : monitorNames) {
            String key = ElementKeyUtil.composeKey(name, plane);
            /*
             * be aware: this may add NULL - values. (on perpose, since the length shall be correct!)
             */
            monitorValues.add(getMonitorValue(key));
        }
        return monitorValues;
    }

    @Override
    public YaspHeader getHeader() {
        return this.yaspHeader;
    }
    
    public void setHeaderData(YaspHeader header) {
        this.yaspHeader = header;
    }
}
