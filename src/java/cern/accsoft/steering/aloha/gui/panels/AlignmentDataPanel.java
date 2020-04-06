/*
 * $Id: AlignmentDataPanel.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 *
 * $Date: 2009-03-16 16:38:11 $
 * $Revision: 1.2 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.panels;

import cern.accsoft.steering.aloha.app.HelperDataManager;
import cern.accsoft.steering.aloha.meas.data.HelperDataType;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentData;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * this class represents a panel, which provides buttons to apply alignments to
 * the model.
 *
 * @author kfuchsbe
 */
public class AlignmentDataPanel extends JPanel {
    private final static Logger LOGGER = LoggerFactory.getLogger(AlignmentDataPanel.class);

    /**
     * the workingSet, which contains all loaded data
     */
    private HelperDataManager workingSet;

    /**
     * our interface to the model
     */
    private ModelDelegate modelDelegate;

    /**
     * init-method used by spring
     */
    public void init() {
        initComponents();
    }

    /**
     * creates all components
     */
    private void initComponents() {
        setLayout(new GridBagLayout());

        JButton btn;
        btn = new JButton(new AbstractAction("apply") {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (getModelDelegate() == null) {
                    return;
                }
                if (getAlignmentData() == null) {
                    LOGGER.warn("No alignment data loaded. Nothing to do!");
                    return;
                }
                getModelDelegate().applyAlignment(getAlignmentData());
            }
        });
        add(btn);
    }

    /**
     * retrieves the actual alignment-data from the working set. (may be null!)
     *
     * @return the actual trim data
     */
    private AlignmentData getAlignmentData() {
        if (getWorkingSet() == null) {
            return null;
        }
        return (AlignmentData) getWorkingSet().getData(HelperDataType.ALIGNMENT);
    }

    /**
     * @param workingSet the workingSet to set
     */
    public void setWorkingSet(HelperDataManager workingSet) {
        this.workingSet = workingSet;
    }

    /**
     * @return the workingSet
     */
    private HelperDataManager getWorkingSet() {
        if (this.workingSet == null) {
            LOGGER.warn("WorkingSet not set. Maybe config error.");
        }
        return workingSet;
    }

    /**
     * @param modelDelegate the modelDelegate to set
     */
    public void setModelDelegate(ModelDelegate modelDelegate) {
        this.modelDelegate = modelDelegate;
    }

    /**
     * @return the modelDelegate
     */
    private ModelDelegate getModelDelegate() {
        if (this.modelDelegate == null) {
            LOGGER.warn("ModelDelegate not set. Maybe config error!");
        }
        return modelDelegate;
    }
}
