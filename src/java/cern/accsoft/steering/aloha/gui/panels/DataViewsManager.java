/**
 * 
 */
package cern.accsoft.steering.aloha.gui.panels;

import cern.jdve.viewer.DataViewer;

/**
 * Keeps track of Available DataViews
 * 
 * @author kaifox
 */
public interface DataViewsManager {

    /**
     * register the dataViews so that they can be handled.
     * 
     * @param dataViewer the dataViewer from which to register the dataviews
     */
    public void registerDataViews(DataViewer dataViewer);

}
