/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import java.util.List;

import org.apache.log4j.Logger;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.machine.AbstractMachineElement;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.meas.data.AbstractDynamicData;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateException;
import cern.accsoft.steering.aloha.model.ModelDelegateListener;
import cern.accsoft.steering.aloha.util.JMadUtil;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.tools.response.FastResponseMatrixTool;
import cern.accsoft.steering.jmad.tools.response.FullResponseMatrixTool;
import cern.accsoft.steering.jmad.tools.response.ResponseMatrixTool;
import cern.accsoft.steering.jmad.tools.response.ResponseRequest;
import cern.accsoft.steering.jmad.tools.response.ResponseRequestImpl;

/**
 * the implementation of kick-response data calculated from the model.
 * 
 * @author kfuchsbe
 * 
 */
public class ModelKickResponseDataImpl extends AbstractDynamicData implements
		ModelKickResponseData {

	/** the logger for the class */
	private final static Logger logger = Logger
			.getLogger(ModelKickResponseDataImpl.class);

	/** The tool, which calculates the response matrix */
	private ResponseMatrixTool responseMatrixTool = new FullResponseMatrixTool(); 
	
	/** class that holds the kick-strengths */
	private KickConfiguration kickConfiguration;

	/** the model-delegate to use for the calculations */
	private ModelDelegate modelDelegate;

	/** the latest calculated response-matrix */
	private Matrix responseMatrix = new Matrix(1, 1);

	@Override
	public Matrix getResponseMatrix() {
		ensureUpToDate();
		return this.responseMatrix;
	}

	@Override
	protected void calc() {
		this.responseMatrix = calcResponseMatrix();
	}

	/**
	 * calculates the actual response-matrix
	 * 
	 * @return the response-matrix
	 * @throws ModelDelegateException
	 * @throws CalculatorException
	 */
	private Matrix calcResponseMatrix() {
		if ((getModelDelegate() == null) || (getResponseMatrixTool() == null)) {
			return null;
		}
		logger.debug("calculating model response-matrix...");

		getModelDelegate().setSuppressEvents(true);

		/* create diagonal-matrix with monitor-gains */
		List<Double> monitorGains = getMachineElementsManager()
				.getActiveMonitorGains();
		int monitorCount = monitorGains.size();
		Matrix monitorGainsMatrix = new Matrix(monitorCount, monitorCount);
		for (int i = 0; i < monitorCount; i++) {
			monitorGainsMatrix.set(i, i, monitorGains.get(i));
		}

		/* create diagonal-matrix of corrector-gains */
		List<Double> correctorGains = getMachineElementsManager()
				.getActiveCorrectorGains();
		int correctorCount = getActiveCorrectors().size();
		Matrix correctorGainsMatrix = new Matrix(correctorCount, correctorCount);
		for (int i = 0; i < correctorCount; i++) {
			correctorGainsMatrix.set(i, i, correctorGains.get(i));
		}

		ResponseRequest request = createResponseRequest();
		Matrix responseMatrix = null;
		/*
		 * calculate response-matrix from model and apply monitor-gains.
		 */
		try {
			responseMatrix = monitorGainsMatrix.times(
					getResponseMatrixTool().calcResponseMatrix(
							getModelDelegate().getJMadModel(), request)).times(
					correctorGainsMatrix);
		} catch (JMadModelException e) {
			logger
					.error("Model was unable to calculate the ResponseMatrix.",
							e);
		}

		getModelDelegate().setSuppressEvents(false);
		logger.debug("... finished.");
		return responseMatrix;
	}

	/**
	 * creates the response-request used to call madx
	 */
	private ResponseRequest createResponseRequest() {
		ResponseRequestImpl responseRequest = new ResponseRequestImpl();

		/*
		 * add all monitors
		 */
		List<Monitor> monitors = getActiveMonitors();
		for (AbstractMachineElement monitor : monitors) {
			responseRequest.addMonitor(monitor.getName(), JMadUtil
					.convertPlane(monitor.getPlane()));
		}

		/*
		 * add all correctors as strengthes with adjusted kicks according to the
		 * gains.
		 */
		List<Corrector> correctors = getActiveCorrectors();

		responseRequest.clearCorrectors();
		for (int i = 0; i < correctors.size(); i++) {
			Corrector corrector = correctors.get(i);
			Double kick = getKickConfiguration().getCorrectorKick(corrector);
			if (kick == null) {
				kick = 0.0;
			}
			responseRequest.addCorrector(corrector.getName(), ((kick * corrector
					.getGain()) / 2), JMadUtil.convertPlane(corrector.getPlane()));
		}

		/*
		 * add all the monitor reg-expressions
		 */
		for (String regexp : getModelDelegate().getMonitorRegexps()) {
			responseRequest.addMonitorRegexp(regexp);
		}
		return responseRequest;
	}

	public void setResponseMatrixTool(ResponseMatrixTool responseMatrixTool) {
		this.responseMatrixTool = responseMatrixTool;
	}

	private ResponseMatrixTool getResponseMatrixTool() {
		return responseMatrixTool;
	}

	public void setKickConfiguration(KickConfiguration kickConfiguration) {
		this.kickConfiguration = kickConfiguration;
	}

	private KickConfiguration getKickConfiguration() {
		return kickConfiguration;
	}

	public void setModelDelegate(ModelDelegate modelDelegate) {
		this.modelDelegate = modelDelegate;
		if (modelDelegate != null) {
			modelDelegate.addListener(new ModelDelegateListener() {

				@Override
				public void becameDirty() {
					setDirty(true);
				}
			});
		}
	}

	public ModelDelegate getModelDelegate() {
		return modelDelegate;
	}

}
