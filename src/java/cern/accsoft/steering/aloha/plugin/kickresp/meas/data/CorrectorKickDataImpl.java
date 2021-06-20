/*
 * $Id: CorrectorKickDataImpl.java,v 1.1 2008-12-02 20:57:43 kfuchsbe Exp $
 *
 * $Date: 2008-12-02 20:57:43 $
 * $Revision: 1.1 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import cern.accsoft.steering.jmad.tools.response.DeflectionSign;
import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.meas.data.ElementKeyUtil;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.yasp.ReadingDataImpl;

/**
 * @author kfuchsbe
 */
public class CorrectorKickDataImpl extends ReadingDataImpl implements CorrectorKickData {

    /**
     * the plane of the kicked corrector
     */
    private String planeToken = null;

    /**
     * the sign of the kicked corrector
     */
    private String signToken = null;

    /**
     * the name of the kicked corrector
     */
    private String correctorName = null;

    /**
     * the beam to which this element belongs to
     */
    private BeamNumber beamNumber = null;

    /**
     * @param planeToken the planeToken to set
     */
    public final void setPlaneToken(String planeToken) {
        this.planeToken = planeToken;
    }

    /**
     * @param signToken the signToken to set
     */
    public final void setSignToken(String signToken) {
        this.signToken = signToken;
    }

    /**
     * @param correctorName the correctorName to set
     */
    public final void setCorrectorName(String correctorName) {
        this.correctorName = correctorName;
    }

    //
    // methods of interface CorrectorKickData
    //

    @Override
    public String getCorrectorKey() {
        return ElementKeyUtil.composeKey(this.correctorName, Plane.fromTag(this.planeToken));
    }

    @Override
    public String getCorrectorName() {
        return this.correctorName;
    }

    @Override
    public boolean isVertical() {
        return (planeToken != null && planeToken.equals(Plane.VERTICAL.getTag()));
    }

    @Override
    public boolean isHorizontal() {
        return (planeToken != null && planeToken.equals(Plane.HORIZONTAL.getTag()));
    }

    @Override
    public boolean isPlus() {
        return (signToken != null && signToken.equals(DeflectionSign.PLUS.getTag()));
    }

    @Override
    public boolean isMinus() {
        return (signToken != null && signToken.equals(DeflectionSign.MINUS.getTag()));
    }

    @Override
    public BeamNumber getBeamNumber() {
        return beamNumber;
    }

    public void setBeamNumber(BeamNumber beamNumber) {
        this.beamNumber = beamNumber;
    }

}
