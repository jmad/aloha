/*
 * $Id: ReadingData.java,v 1.2 2009-02-25 18:48:42 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:42 $ $Revision: 1.2 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.util.meas.data.yasp;

import java.util.Collection;
import java.util.List;

import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * This is the interface for all kinds of Reading data.
 * <p>
 * ReadingData can for example be:
 * <ul>
 * <li>trajectory-data,
 * <li>closed orbit-data or
 * <li>dispersion-data
 * </ul>
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public interface ReadingData extends MeasuredData<MonitorValue> {

    /**
     * returns the CorrectorValue for a given key.
     * 
     * @param key
     * @return the {@link CorrectorValue}
     */
    CorrectorValue getCorrectorValue(String key);

    /**
     * @return all available correctorValues
     */
    Collection<CorrectorValue> getCorrectorValues();

    /**
     * collects all corrector Values for the correctors of the given names and plane
     * 
     * @param correctorNames the names of the correctors for which to retrieve the values
     * @param plane the plane for which toretrieve the values
     * @return the corrector-values
     */
    List<CorrectorValue> getCorrectorValues(List<String> correctorNames, Plane plane);

    /**
     * collects all monitor values for monitors of the given names and plane
     * 
     * @param monitorNames the names of the monitors
     * @param plane the plane for which to retrieve the values
     * @return the corrector-values
     */
    List<MonitorValue> getMonitorValues(List<String> monitorNames, Plane plane);

    /**
     * returns all the corrector values for the plane
     * 
     * @param plane the plane for which to get the corrector values
     * @param beamNumber The beam number for which to get the values
     * @return the {@link CorrectorValue}s
     */
    List<CorrectorValue> getCorrectorValues(Plane plane, BeamNumber beamNumber);

    /**
     * returns all the {@link MonitorValue}s for one plane
     * 
     * @param plane the plane for which to return the {@link MonitorValue}s
     * @param beamNumber the beam number for which to get the values
     * @return all the {@link MonitorValue}s for the given plane
     */
    List<MonitorValue> getMonitorValues(Plane plane, BeamNumber beamNumber);

    /**
     * @return the yasp header data in this reading data
     */
    YaspHeader getHeader();
}
