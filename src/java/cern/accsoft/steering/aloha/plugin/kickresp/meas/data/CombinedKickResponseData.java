package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import java.util.List;

import Jama.Matrix;

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
	public Matrix getDifferenceVector();

	/**
	 * @return the difference matrix between measurement and model, normalized
	 *         by (kicks/noise)
	 */
	public Matrix getRelativeDiffMatrix();

	/**
	 * @return the difference matrix measurement-model
	 */
	public Matrix getDifferenceMatrix();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         monitor
	 */
	public List<Double> getMonitorDifferenceRms();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         corrector
	 */
	public List<Double> getCorrectorDifferenceRms();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         monitor normalized by (kicks/noise);
	 */
	public List<Double> getMonitorRelativeDiffRms();

	/**
	 * @return the rms of the difference between measurement-model for each
	 *         corrector normalized by (kicks/noise);
	 */
	public List<Double> getCorrectorRelativeDiffRms();

}
