package cern.accsoft.steering.util.meas.read.filter.impl;

import java.util.List;

import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.meas.data.yasp.CorrectorValue;
import cern.accsoft.steering.util.meas.data.yasp.MonitorValue;
import cern.accsoft.steering.util.meas.read.filter.ReadSelectionFilter;

/**
 * This class decides on a list of names, if the values for monitors and correctors shall be loaded. Additionally, it
 * allows to laod all correctors or monitors by setting the properties {@link #allBeamCorrectors} or
 * {@link #allBeamMonitors}, respectively.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class NameListReadSelectionFilter implements ReadSelectionFilter {

    /**
     * which corrector names to load. Also the order they are loaded is determined by these values.
     */
    private List<String> correctorNames;

    /** load all correctors for the beam, instead of filtering by names */
    private boolean allBeamCorrectors = false;

    /**
     * which monitor names to load. Also the order they are loaded is determined by these values.
     */
    private List<String> monitorNames;

    /** load all monitors for the beam, instead of filtering by names */
    private boolean allBeamMonitors = false;

    /** the beam number of interest */
    private BeamNumber beamNumber;

    /**
     * the constructor, which requires the elements to load
     * 
     * @param correctorNames
     * @param monitorNames
     * @param beamNumber
     */
    public NameListReadSelectionFilter(List<String> correctorNames, List<String> monitorNames, BeamNumber beamNumber) {
        this.correctorNames = correctorNames;
        this.monitorNames = monitorNames;
        this.beamNumber = beamNumber;
    }

    /**
     * @return the correctorNames
     */
    public List<String> getCorrectorNames() {
        return correctorNames;
    }

    /**
     * @return the monitorNames
     */
    public List<String> getMonitorNames() {
        return monitorNames;
    }

    @Override
    public boolean isOfInterest(MonitorValue monitorValue) {
        if (this.allBeamMonitors || (getMonitorNames().contains(monitorValue.getName()))) {
            return (beamNumber == monitorValue.getBeam());
        } else {
            return false;
        }
    }

    @Override
    public boolean isOfInterest(CorrectorValue correctorValue) {
        if (this.allBeamCorrectors || (getCorrectorNames().contains(correctorValue.getName()))) {
            return (beamNumber == correctorValue.getBeam());
        } else {
            return false;
        }

    }

    /**
     * @param allBeamCorrectors the allBeamCorrectors to set
     */
    public void setAllBeamCorrectors(boolean allBeamCorrectors) {
        this.allBeamCorrectors = allBeamCorrectors;
    }

    /**
     * @param allBeamMonitors the allBeamMonitors to set
     */
    public void setAllBeamMonitors(boolean allBeamMonitors) {
        this.allBeamMonitors = allBeamMonitors;
    }

}
