/**
 * 
 */
package cern.accsoft.steering.aloha.display;

import cern.accsoft.steering.aloha.meas.data.Data;

import java.util.List;

/**
 * This interface defines mapping of data from displayable data to real
 * datasets. This mostly will be implemented by enums defining a concrete
 * dataset configuration.
 * <p>
 * The returned values should all be of the same length.
 * 
 * @author kfuchsbe
 * 
 */
public interface DataMapper<T extends Data> {

	/** return the values */
	public List<Double> getValues(T data);
}
