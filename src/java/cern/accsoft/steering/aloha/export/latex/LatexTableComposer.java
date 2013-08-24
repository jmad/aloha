package cern.accsoft.steering.aloha.export.latex;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cern.accsoft.steering.util.TMatrix;

/**
 * simple class to compose a latex table by setting single elements and then
 * retrieving the final string.
 * 
 * The columns are centerd by default.
 * 
 * @author kfuchsbe
 * 
 */
class LatexTableComposer {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(LatexTableComposer.class);

	private final static String LATEX_TABLE_HEADER_A = //
	"% \\usepackage{booktabs}\n" + //
			"\\begin{table}[ht]\n" + //
			"  \\begin{center}\n" + //
			"    \\begin{tabular}{";

	private final static String LATEX_TABLE_HEADER_B = //
	"}\n" + // 
			"      \\toprule\n";

	private final static String LATEX_TABLE_FOOTER = //
	"    \\bottomrule\n" + //
			"    \\end{tabular}\n" + //
			"  \\end{center}\n" + //
			"  \\caption{xxxCaptionxxx}\n" + //
			"  \\label{tab:xxxLabelxxx}\n" + //
			"\\end{table}\n";

	private final static String LATEXT_TABLE_INTENT = "      ";

	private final static String LATEX_MIDRULE = "\\midrule \n";

	private final static String LATEX_LINE_SEPARATOR = "\\\\ \n";

	private final static String LATEX_CELL_SEPARATOR = " & ";

	private final static String LATEX_COLUMN_ALIGNMENT = "c";

	/** the matrix with the entries */
	private TMatrix<String> entries = new TMatrix<String>(0, 0, null);

	/** the columnTitles of the table */
	private List<String> columnTitles = new ArrayList<String>();

	/**
	 * creates a new TableComposer with the given amount of rows and columns
	 * 
	 * @param rows
	 *            the amount of rows
	 * @param cols
	 *            the amount of columns
	 */
	public LatexTableComposer(int rows, int cols) {
		this.entries = new TMatrix<String>(rows, cols, null);
		for (int i = 0; i < cols; i++) {
			this.columnTitles.add(null);
		}

	}

	/**
	 * @return the string representing the final latex-table.
	 */
	public String compose() {
		StringBuffer buf = new StringBuffer();

		/*
		 * create the header
		 */
		buf.append(LATEX_TABLE_HEADER_A);
		int columnCount = this.entries.getColumnDimension();
		for (int i = 0; i < columnCount; i++) {
			buf.append(LATEX_COLUMN_ALIGNMENT);
		}
		buf.append(LATEX_TABLE_HEADER_B);

		/*
		 * the titles of the columns
		 */
		if (getColumnTitles() != null) {
			buf.append(LATEXT_TABLE_INTENT);
			for (int i = 0; i < getColumnTitles().size(); i++) {
				String title = getColumnTitles().get(i);
				if (title != null) {
					buf.append(title);
				}
				if (i < (columnCount - 1)) {
					buf.append(LATEX_CELL_SEPARATOR);
				}
			}
			buf.append(LATEX_LINE_SEPARATOR);
			buf.append(LATEXT_TABLE_INTENT + LATEX_MIDRULE);
		}

		/*
		 * add the entries
		 */
		for (int row = 0; row < this.entries.getRowDimension(); row++) {
			buf.append(LATEXT_TABLE_INTENT);
			for (int col = 0; col < columnCount; col++) {
				String entry = this.entries.get(row, col);
				if (entry != null) {
					buf.append(entry);
				}
				if (col < (columnCount - 1)) {
					buf.append(LATEX_CELL_SEPARATOR);
				}
			}
			buf.append(LATEX_LINE_SEPARATOR);
		}

		/*
		 * finally the footer
		 */
		buf.append(LATEX_TABLE_FOOTER);
		return buf.toString();
	}

	/**
	 * sets the entry at row/col to the given value
	 * 
	 * @param row
	 * @param col
	 * @param value
	 */
	public void setEntry(int row, int col, String value) {
		this.entries.set(row, col, value);
	}

	/**
	 * @param columnTitles
	 *            the columnTitles to set
	 */
	public void setColumnTitle(int col, String title) {
		if (col < this.columnTitles.size()) {
			this.columnTitles.set(col, title);
		} else {
			logger.warn("Cannot set title for column " + col
					+ " since this latex table will only have "
					+ this.columnTitles.size() + " columns");
		}
	}

	/**
	 * @return the columnTitles
	 */
	private List<String> getColumnTitles() {
		return columnTitles;
	}

}