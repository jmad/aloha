/*
 * $Id: MadXAlignmentConverter.java,v 1.5 2009-02-25 18:48:44 kfuchsbe Exp $
 *
 * $Date: 2009-02-25 18:48:44 $
 * $Revision: 1.5 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.model;

import cern.accsoft.steering.aloha.meas.data.align.AlignmentData;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentValue;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentValueType;
import cern.accsoft.steering.jmad.domain.misalign.Misalignment;
import cern.accsoft.steering.jmad.domain.misalign.MisalignmentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * converts aloha-alignment data to correct madx-misalignments
 *
 * @author kfuchsbe
 */
public class MadXAlignmentConverter {
    private final static Logger LOGGER = LoggerFactory.getLogger(MadXAlignmentConverter.class);

    /**
     * the minimum difference between two points
     */
    private final static double MIN_DIFF_S = 0.001;

    /**
     * creates all misalignments which correspond to measured values contained
     * in alignmentData
     *
     * @param alignmentData the data from which to create the misalignments
     * @return the Misalignments
     */
    public List<MisalignmentConfiguration> createMadXMisalignments(
            AlignmentData alignmentData) {
        List<MisalignmentConfiguration> misalignments = new ArrayList<MisalignmentConfiguration>();

        for (String elementName : alignmentData.getElementNames()) {
            AlignmentValue startValue = alignmentData.getAlignmentValue(
                    elementName, AlignmentValueType.START);
            if (startValue == null) {
                LOGGER.warn("start-value for element '"
                        + elementName
                        + "' could not be found. Cant create misalignment for this element.");
                continue;
            }

            AlignmentValue endValue = alignmentData.getAlignmentValue(
                    elementName, AlignmentValueType.END);
            if (endValue == null) {
                LOGGER.warn("end-value for element '"
                        + elementName
                        + "' could not be found. Can not create misalignment for this element.");
                continue;
            }

            MisalignmentConfiguration misalignmentConfiguration = new MisalignmentConfiguration(
                    elementName);

            fillMisalignment(misalignmentConfiguration.getMisalignment(),
                    startValue, endValue);
            misalignments.add(misalignmentConfiguration);
        }

        return misalignments;
    }

    /**
     * fills a misalignment-object from start and end - alignment values. We
     * assume these values to be the values for the beam bumps. (one has to take
     * care about this, when e.g. exporting data from geode: use points
     * faisceau, NOT points alesage)
     *
     * @param misalignment the misalignment to fill
     * @param startValue   the alignment-value near the start of the element
     * @param endValue     the alignment-value near the end of the element
     * @return the misalignment
     */
    private void fillMisalignment(Misalignment misalignment,
                                  AlignmentValue startValue, AlignmentValue endValue) {

        /* the measured difference between the two alignment points */
        double diffS = endValue.getS() - startValue.getS();
        if (diffS < MIN_DIFF_S) {
            LOGGER.warn("The minimum distance between two alignment-points of one element must be "
                    + " positive and greater than "
                    + MIN_DIFF_S
                    + ". This is not the case for element '"
                    + startValue.getElementName()
                    + "' (diffS = "
                    + diffS + "). -> Cannot fill misalignment.");
            return;
        }

        /* angle in x-s-plane */
        double deltaTheta = Math.asin((endValue.getDeltaX() - startValue
                .getDeltaX())
                / diffS);
        misalignment.setDeltaTheta(deltaTheta);

        /* angle in y-s-plane */
        double deltaPhi = Math.asin((endValue.getDeltaY() - startValue
                .getDeltaY())
                / diffS);
        /*
         * tilted down is plus for MADX! Therefore an additional minus sign:
         */
        misalignment.setDeltaPhi(-deltaPhi);

        /* x-misalignment */
        double deltaX = startValue.getDeltaX();
        misalignment.setDeltaX(deltaX);

        /* y-misalignment */
        double deltaY = startValue.getDeltaY();
        misalignment.setDeltaY(deltaY);

        /* the change in s-direction */
        double deltaS = (startValue.getDeltaS() + endValue.getDeltaS()) / 2;
        misalignment.setDeltaS(deltaS);

        if (startValue.getDeltaTilt() != endValue.getDeltaTilt()) {
            LOGGER.warn("Tilts for begin and end points of element '"
                    + startValue.getElementName()
                    + "' are not the same. begin: " + startValue.getDeltaTilt()
                    + "; end: " + endValue.getDeltaTilt());
        }
        double deltaTilt = (startValue.getDeltaTilt() + endValue.getDeltaTilt()) / 2;
        /*
         * tilted to the left is plus for SU, but minus for madx.
         */
        misalignment.setDeltaPsi(-deltaTilt);
    }
}
