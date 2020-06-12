package cern.accsoft.steering.util.meas.read.filter;

import cern.accsoft.steering.util.meas.data.yasp.CorrectorValue;
import cern.accsoft.steering.util.meas.data.yasp.MonitorValue;

public interface ReadSelectionFilter {

    /**
     * decides if we will use the given monitor
     * 
     * @param monitorValue the monitor-value to test
     * @return <code>true</code> if the monitor shall be loaded, false otherwise
     */
    boolean isOfInterest(MonitorValue monitorValue);

    /**
     * decides if we will use the given corrector
     * 
     * @param correctorValue the value in question
     * @return <code>true</code> if the the given value should be loaded, <code>false</code> otherwise
     */
    boolean isOfInterest(CorrectorValue correctorValue);

}