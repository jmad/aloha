/*
 * $Id: TableModelLatexTextExporter.java,v 1.1 2009-02-25 18:48:42 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:42 $ 
 * $Revision: 1.1 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.export.latex;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import cern.accsoft.steering.aloha.export.TextExporter;

/**
 * this class can export (parts of) a table model to a latex table.
 * 
 * @author kfuchsbe
 * 
 */
public class TableModelLatexTextExporter implements TextExporter {

	/** the default formatter */
	private final static NumberFormat formatter = NumberFormat.getInstance();
	static {
		formatter.setMinimumFractionDigits(1);
		formatter.setMaximumFractionDigits(6);
	}

	/** The table model from which to retrieve the data */
	private TableModel tableModel = null;

	/** the column indizes of the model, which will be exported */
	private List<Integer> columnIndizes = new ArrayList<Integer>();

	/**
	 * the default constructor, which needs a table model
	 * 
	 * @param tableModel
	 */
	public TableModelLatexTextExporter(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * add an index to the indizes that shall be exported.
	 * 
	 * @param index
	 *            the index to add
	 */
	public void addColumnIndex(int index) {
		this.columnIndizes.add(index);
	}

	@Override
	public String createExportText() {

		int rows = this.tableModel.getRowCount();
		int indexCount = this.columnIndizes.size();

		LatexTableComposer composer = new LatexTableComposer(rows, indexCount);

		/* set the titles */
		for (int i = 0; i < indexCount; i++) {
			int modelCol = this.columnIndizes.get(i);
			composer.setColumnTitle(i, this.tableModel.getColumnName(modelCol));
		}

		/*
		 * add the entries
		 */
		for (int row = 0; row < rows; row++) {
			for (int i = 0; i < indexCount; i++) {
				int modelCol = this.columnIndizes.get(i);
				Object value = this.tableModel.getValueAt(row, modelCol);
				if (value != null) {
					String strValue = null;
					if (value instanceof Double) {
						strValue = formatter.format(value);
					} else if (value instanceof String) {
						strValue = (String) value;
					} else if (value instanceof Boolean) {
						strValue = ((Boolean) value ? "true" : "false");
					}
					composer.setEntry(row, i, strValue);
				}
			}
		}

		return composer.compose();
	}

	@Override
	public void setMaxFractionDigits(int digits) {
		formatter.setMaximumFractionDigits(digits);
	}

	@Override
	public int getMaxFractionDigits() {
		return formatter.getMaximumFractionDigits();
	}

}
