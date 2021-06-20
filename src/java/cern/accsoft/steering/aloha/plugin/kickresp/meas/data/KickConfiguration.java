/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import cern.accsoft.steering.aloha.machine.Corrector;

/**
 * just to retrieve the kick strengthes used in the measurement.
 * 
 * @author kfuchsbe
 * 
 */
public interface KickConfiguration {
	
	Double getCorrectorKick(Corrector corrector);
}
