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

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.trim.meas.TrimMeasurement;
import cern.accsoft.steering.aloha.plugin.trim.meas.data.TrimData;

/**
 * this class represents a panel, which provides buttons to apply trims to the
 * model.
 * 
 * @author kfuchsbe
 * 
 */
public class TrimDataPanel extends JPanel {
	private static final long serialVersionUID = 978358494322205794L;

	/** the logger for the class */
	private final static Logger logger = Logger.getLogger(TrimDataPanel.class);

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
			private static final long serialVersionUID = -2655964987670987110L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (getModelDelegate() == null) {
					return;
				}
				if (getTrimData() == null) {
					logger.warn("No trim data loaded. Nothing to do!");
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
