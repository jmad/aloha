package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import Jama.Matrix;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.meas.data.InconsistentDataException;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;
import cern.accsoft.steering.jmad.tools.response.DeflectionSign;
import cern.accsoft.steering.util.TMatrix;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * this interface provides methods to access measured kick-response data.
 * 
 * @author kfuchsbe
 * 
 */
public interface KickResponseData extends KickConfiguration {
	/**
	 * @return the responding response-matrix corresponding to the actually
	 *         active correctors/monitors. (Calculates prior to return, if it is
	 *         not consistent with the actual selected monitors/correctors)
	 */
	public abstract Matrix getResponseMatrix();

	/**
	 * forces a recalculation of the response matrix.
	 * 
	 * @throws InconsistentDataException
	 */
	public abstract void calc() throws InconsistentDataException;

	/**
	 * sets the elements of the response-matrix, which correspond to different
	 * planes, to zero.
	 */
	public abstract void decouple();

	/**
	 * @return the validity-matrix, which indicates, which entries in the
	 *         response-matrix are valid, and which are not.
	 */
	public abstract TMatrix<Boolean> getValidityMatrix();

	/**
	 * retrieves the corrector kick data, which was used to create the response
	 * matrix for one corrector.
	 * 
	 * @param corrector
	 *            the corrector for which to retrieve the data
	 * @param sign
	 *            the sign of the deflection (plus/minus)
	 */
	public abstract CorrectorKickData getCorrectorKickData(Corrector corrector,
			DeflectionSign sign);

	public TMatrix<Boolean> getTrajectoryValidity(Plane plane,
			DeflectionSign sign);

	public Matrix getTrajectoryMatrix(Plane plane, DeflectionSign sign);

	public Matrix getRelativeRmsValues();

	public TrajectoryData getStabilityData();

}