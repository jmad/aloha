/**
 * 
 */
package cern.accsoft.steering.aloha.meas.data;

/**
 * This is the interface which should be implememented by any helper-data which can be
 * loaded into aloha.
 * 
 * @author kfuchsbe
 * 
 */
public interface HelperData extends Data {

	/** the type of the data as enum */
	public HelperDataType getType();

}
