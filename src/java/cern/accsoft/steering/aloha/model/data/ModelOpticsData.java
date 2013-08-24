/*
 * $Id: ModelOpticsData.java,v 1.3 2009-02-25 18:48:43 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:43 $ 
 * $Revision: 1.3 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.model.data;

import java.util.List;

import cern.accsoft.steering.aloha.meas.data.DynamicData;
import cern.accsoft.steering.util.meas.data.Plane;

/**
 * this interface shall provide data of the model, consistent with the actual
 * selected monitors/correctors
 * 
 * @author kfuchsbe
 * 
 */
public interface ModelOpticsData extends DynamicData {

	/**
	 * @param plane
	 *            the plane for which to retrieve the values
	 * @return the dispersion values for the actually active monitors
	 */
	public List<Double> getMonitorDispersions(Plane plane);

	/**
	 * @return the dispersion-values for both planes
	 */
	public List<Double> getMonitorDispersions();

	/**
	 * @param plane
	 *            the plane for which to retrieve the values
	 * @return the beta-values for the actually active monitors
	 */
	public List<Double> getMonitorBetas(Plane plane);

	/**
	 * @return the beta-values for both planes
	 */
	public List<Double> getMonitorBetas();

	/**
	 * @param plane
	 *            the plane for which to retrieve the values
	 * @return the phase-values for the actually active monitors
	 */
	public List<Double> getMonitorPhases(Plane plane);

	/**
	 * @param plane
	 *            the plane for which to retrieve the transverse positions
	 * @return the transvers position for the given plane
	 */
	public List<Double> getMonitorPos(Plane plane);

	/**
	 * @return the positions at the monitors for both planes (H+V)
	 */
	public List<Double> getMonitorPos();

	/**
	 * the dispersion values for all points
	 * 
	 * @param plane
	 *            the plane for which to retrieve the dispersion-values
	 * @return the dispersion values
	 */
	public List<Double> getAllDispersions(Plane plane);

	/**
	 * @param plane
	 *            the plane for which to retrieve the values
	 * @return the beta-values for the actually active monitors
	 */
	public List<Double> getAllBetas(Plane plane);

	/**
	 * @param plane
	 *            the plane for which to retrieve the values
	 * @return the phase-values for the actually active monitors
	 */
	public List<Double> getAllPhases(Plane plane);

	/**
	 * @param the
	 *            plane for which to retrieve the data
	 * @return plane the transvers positions fo all elements
	 */
	public List<Double> getAllPos(Plane plane);

	/**
	 * retrieves the phase for one element.
	 * 
	 * @param elementName
	 *            the name of the element for which to get the phase
	 * @param plane
	 *            the plane for which to return the phase
	 * @return the phase of the given element
	 */
	public Double getPhase(String elementName, Plane plane);

	/**
	 * @return the positions of all elements
	 */
	public List<Double> getAllSPositions();

	/**
	 * @return the position of the element with given name
	 */
	public Double getSPosition(String elementName);

	/**
	 * the s-positions for the monitors in the given plane
	 * 
	 * @param plane
	 * @return
	 */
	public List<Double> getMonitorSPositions(Plane plane);

	/**
	 * returns a list with the names of all elements
	 * 
	 * @return
	 */
	public List<String> getAllNames();
}
