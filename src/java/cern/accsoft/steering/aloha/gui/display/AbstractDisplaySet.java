/**
 * 
 */
package cern.accsoft.steering.aloha.gui.display;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import cern.accsoft.steering.aloha.analyzer.Analyzer;
import cern.jdve.viewer.DVView;

/**
 * This is the default implementation of a {@link DisplaySet}
 * 
 * @author kfuchsbe
 * 
 */
public abstract class AbstractDisplaySet implements DisplaySet {

	/** the panel for the measurement details */
	private JPanel detailPanel;

	/** all the data-viewer views */
	private List<DVView> dvViews = new ArrayList<DVView>();

	/** all additional analyzers which belong to this displayset. */
	private List<Analyzer> analyzers = new ArrayList<Analyzer>();

	@Override
	public final JPanel getDetailPanel() {
		return this.detailPanel;
	}

	@Override
	public final List<DVView> getDvViews() {
		return this.dvViews;
	}

	@Override
	public final void refresh() {
		for (Analyzer analyzer : analyzers) {
			analyzer.refresh();
		}
		doRefresh();
	}

	/**
	 * has to be implemented by subclass to perform the necessary actions to
	 * refresh the display
	 */
	protected abstract void doRefresh();

	protected final void addDvView(DVView dvView) {
		this.dvViews.add(dvView);
	}

	protected final void setDetailPanel(JPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	/**
	 * sets all the analyzers
	 * 
	 * @param analyzers
	 */
	public void addAnalyzers(List<Analyzer> analyzers) {
		this.analyzers.addAll(analyzers);
		for (Analyzer analyzer : analyzers) {
			addDvView(analyzer.getDVView());
		}
	}

}
