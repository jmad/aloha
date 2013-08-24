/*
 * $Id: TextExporterPanel.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ 
 * $Revision: 1.2 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.gui.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import cern.accsoft.steering.aloha.export.TextExporter;
import cern.accsoft.steering.util.gui.CompUtils;

/**
 * A panel, which just displays the text, that then can be copied.
 * 
 * @author kfuchsbe
 * 
 */
public class TextExporterPanel extends JPanel {
	private static final long serialVersionUID = -979851588126505826L;

	private final static Dimension PREFERRED_SIZE = new Dimension(400, 400);

	/** the text-exporter to use */
	private TextExporter textExporter;

	private JTextArea txtExportedText;
	private JTextPane txtFractionDigits;

	public TextExporterPanel() {
		initComponents();
	}

	private void initComponents() {
		setPreferredSize(PREFERRED_SIZE);
		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		txtExportedText = new JTextArea();

		JScrollPane scrollPane = CompUtils.createScrollPane(txtExportedText);
		add(scrollPane, constraints);

		constraints.gridy++;
		constraints.weighty = 0;
		constraints.gridwidth = 1;
		JLabel label;
		label = new JLabel("Max fraction digits: ");
		add(label, constraints);

		constraints.gridx++;
		txtFractionDigits = new JTextPane();
		add(txtFractionDigits, constraints);

		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weighty = 0;
		constraints.gridwidth = 2;
		JButton btn;
		btn = new JButton("compose");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (getTextExporter() == null) {
					return;
				}

				getTextExporter().setMaxFractionDigits(
						Integer.parseInt(txtFractionDigits.getText()));
				txtExportedText.setText(getTextExporter().createExportText());
			}
		});
		add(btn, constraints);
	}

	private void initValues() {
		if (getTextExporter() == null) {
			return;
		}

		txtFractionDigits.setText(Integer.toString(getTextExporter()
				.getMaxFractionDigits()));
	}

	/**
	 * @param textExporter
	 *            the textExporter to set
	 */
	public void setTextExporter(TextExporter textExporter) {
		this.textExporter = textExporter;
		initValues();
	}

	/**
	 * @return the textExporter
	 */
	public TextExporter getTextExporter() {
		return textExporter;
	}
}
