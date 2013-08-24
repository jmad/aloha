/**
 * 
 */
package cern.accsoft.steering.aloha.gui.display;

/**
 * this is the implementation of a DisplaySet which only contains analyzers
 * 
 * It is created by default for a measurement, if no dedicated displayset for a
 * measurement is provided by a factory.
 * 
 * @author kfuchsbe
 * 
 */
public class DefaultDisplaySet extends AbstractDisplaySet {

	/*
	 * this set is the simplest displayset, which contains only analyzers
	 */

	@Override
	protected void doRefresh() {
		/* nothing to do */
	}

}
