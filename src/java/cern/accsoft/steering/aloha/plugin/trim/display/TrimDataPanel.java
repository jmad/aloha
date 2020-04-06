/*
 * $Id: TrimDataPanel.java,v 1.1 2009-01-15 11:46:24 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:24 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.trim.display;

import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.trim.meas.TrimMeasurement;
import cern.accsoft.steering.aloha.plugin.trim.meas.data.TrimData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * this class represents a panel, which provides buttons to apply trims to the
 * model.
 * 
 * @author kfuchsbe
 * 
 */
public class TrimDataPanel extends JPanel {
	private final static Logger LOGGER = LoggerFactory.getLogger(TrimDataPanel.class);

	/** the measurement this panel belongs to */
	private TrimMeasurement measurement;

	public TrimDataPanel(TrimMeasurement measurement) {
		super();
		this.measurement = measurement;
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
				if (getTrimData() == null) {
					LOGGER.warn("No trim data loaded. Nothing to do!");
					return;
				}
				getModelDelegate().applyTrim(getTrimData());
			}
		});
		add(btn);
	}

	/**
	 * retrieves the actual trimdata from the working set. (may be null!)
	 * 
	 * @return the actual trim data
	 */
	private TrimData getTrimData() {
		return getMeasurement().getData();
	}

	/**
	 * @return the modelDelegate
	 */
	private ModelDelegate getModelDelegate() {
		return getMeasurement().getModelDelegate();
	}

	private TrimMeasurement getMeasurement() {
		return measurement;
	}
}
