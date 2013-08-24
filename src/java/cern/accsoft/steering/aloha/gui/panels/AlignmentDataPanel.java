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

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.app.HelperDataManager;
import cern.accsoft.steering.aloha.meas.data.HelperDataType;
import cern.accsoft.steering.aloha.meas.data.align.AlignmentData;
import cern.accsoft.steering.aloha.model.ModelDelegate;

/**
 * this class represents a panel, which provides buttons to apply alignments to
 * the model.
 * 
 * @author kfuchsbe
 * 
 */
public class AlignmentDataPanel extends JPanel {
	private static final long serialVersionUID = 978358494322205794L;

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(AlignmentDataPanel.class);

	/** the workingSet, which contains all loaded data */
	private HelperDataManager workingSet;

	/** our interface to the model */
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
			private static final long serialVersionUID = -2655964987670987110L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (getModelDelegate() == null) {
					return;
				}
				if (getAlignmentData() == null) {
					logger.warn("No alignment data loaded. Nothing to do!");
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
	 * @param workingSet
	 *            the workingSet to set
	 */
	public void setWorkingSet(HelperDataManager workingSet) {
		this.workingSet = workingSet;
	}

	/**
	 * @return the workingSet
	 */
	private HelperDataManager getWorkingSet() {
		if (this.workingSet == null) {
			logger.warn("woorkingSet not set. Maybe config error.");
		}
		return workingSet;
	}

	/**
	 * @param modelDelegate
	 *            the modelDelegate to set
	 */
	public void setModelDelegate(ModelDelegate modelDelegate) {
		this.modelDelegate = modelDelegate;
	}

	/**
	 * @return the modelDelegate
	 */
	private ModelDelegate getModelDelegate() {
		if (this.modelDelegate == null) {
			logger.warn("modelDelegate not set. MAybe config error!");
		}
		return modelDelegate;
	}
}
