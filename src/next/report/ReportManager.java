/**
 * 
 */
package cern.accsoft.steering.aloha.report;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.log4j.Logger;

/**
 * This class handles the creation and display of aloha-specific reports.
 * 
 * @author kfuchsbe
 * 
 */
public class ReportManager {

	/** the logger for the class */
	private final static Logger logger = Logger.getLogger(ReportManager.class);

	/**
	 * displays a report of a specific type.
	 * 
	 * @param report
	 *            the report to display
	 */
	public void showReport(Report report, Map<?, ?> parameters,
			JRDataSource dataSource) {
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(report
					.getReport(), parameters, dataSource);
			JasperViewer.viewReport(jasperPrint);
		} catch (JRException e) {
			logger.error("Error while filling report '" + report.toString()
					+ "'", e);
		}
	}

}
