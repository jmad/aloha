package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import Jama.Matrix;

import java.util.List;

/**
 * provides methods to get information for combined data from model and
 * measurement
 * 
 * @author kfuchsbe
 * 
 */
public interface CombinedKickResponseData {

	/**
	 * @return the actual difference vector between measurement and the
	 *         (probably fitted) model.
	 */
	Matrix getDifferenceVector();

	/**
	 * @return the difference matrix between measurement and model, normalized
	 *         by (kicks/noise)
	 */
	Matrix getRelativeDiffMatrix();

	/**
	 * @return the difference matrix measurement-model
	 */
	Matrix getDifferenceMatrix();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         monitor
	 */
	List<Double> getMonitorDifferenceRms();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         corrector
	 */
	List<Double> getCorrectorDifferenceRms();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         monitor normalized by (kicks/noise);
	 */
	List<Double> getMonitorRelativeDiffRms();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         corrector normalized by (kicks/noise);
	 */
	List<Double> getCorrectorRelativeDiffRms();

}
