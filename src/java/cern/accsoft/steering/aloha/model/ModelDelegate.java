/*
 * $Id: ModelDelegate.java,v 1.3 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.model;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.align.AlignmentData;
import cern.accsoft.steering.aloha.model.data.ModelOpticsData;
import cern.accsoft.steering.aloha.plugin.trim.meas.data.TrimData;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.meas.read.filter.impl.NameListReadSelectionFilter;

/**
 * this is the interface for an abstraction layer between aloha and the model. It provides matrices and vectors in the
 * correct dimensions, according the actually selected monitors/correctors. Additionaly it is possible, to set the
 * gain-factors for correctors and monitors from outside.
 * 
 * @author kfuchsbe
 */
public interface ModelDelegate {

    /**
     * resets the model
     */
    void reset();

    /**
     * clean up the model
     */
    void cleanup();

    /**
     * @param listener the listener to add
     */
    void addListener(ModelDelegateListener listener);

    /**
     * @param listener the listener to remove
     */
    void removeListener(ModelDelegateListener listener);

    /**
     * creates a selection filter for reading yasp-files
     * 
     * @return the selection filter
     */
    NameListReadSelectionFilter createReadSelectionFilter(BeamNumber beamNumber);

    /**
     * @return true, if the model is initialized
     */
    boolean isInitialized();

    /**
     * sets the Correctors of the model to the values given in {@link TrimData}
     * 
     * @param trimData the trim-data to apply to the model
     */
    void applyTrim(TrimData trimData);

    /**
     * sets the misalignments to the elements, as they are given in the alignmentData
     * 
     * @param alignmentData
     */
    void applyAlignment(AlignmentData alignmentData);

    /**
     * @return the jmad-model instance used by this delegate
     */
    JMadModel getJMadModel();

    /**
     * @return an object containing all kind of information of the current model-optic.
     */
    ModelOpticsData getModelOpticsData();

    /**
     * @return a list of regexpressions representing the monitors, or an empty list. This can be used e.g. for
     *         calculating the response matrix.
     */
    List<String> getMonitorRegexps();

    /**
     * if this is set to true, then the listener event (becameDirty) are not fired. This is useful if on is modifying
     * the model several times during a complicated calculation (e.g. calculating the response matrix) and one wants to
     * avoid that all clients of the ModelDelegate get notified that the model bacame dirty and therefore recalculate
     * their values, although the model did not change permanently, but only temporarily.
     * <p>
     * NOTE: The client is responsible to restore the same state of the model as before!
     * 
     * @param suppressEvents true to suppress the event, false for nominal behaviour.
     */
    void setSuppressEvents(boolean suppressEvents);

}
