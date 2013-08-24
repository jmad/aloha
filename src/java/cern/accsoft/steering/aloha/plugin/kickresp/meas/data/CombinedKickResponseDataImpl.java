/*
 * $Id: CombinedKickResponseDataImpl.java,v 1.4 2009-01-27 10:17:57 kfuchsbe Exp $
 * 
 * $Date: 2009-01-27 10:17:57 $ $Revision: 1.4 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.plugin.kickresp.meas.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import Jama.Matrix;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.calc.CalculatorException;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.machine.Corrector;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManagerListener;
import cern.accsoft.steering.aloha.meas.data.DynamicDataListener;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;
import cern.accsoft.steering.aloha.util.ArrayUtil;
import cern.accsoft.steering.aloha.util.ZeroUtil;
import cern.accsoft.steering.util.TMatrix;

/**
 * the implementation of the combined data from model and measurement
 * 
 * @author kfuchsbe
 */
public class CombinedKickResponseDataImpl implements CombinedKickResponseData, MachineElementsManagerAware,
        NoiseWeighterAware {

    /** the logger for the class */
    private final static Logger logger = Logger.getLogger(CombinedKickResponseDataImpl.class);

    /** the class, which takes care of the correct weighting of the noise */
    private NoiseWeighter noiseWeighter;

    /** the class which keeps track of selected machine-elements */
    private MachineElementsManager machineElementsManager;

    /** the kick-response data we belong to */
    private KickResponseData kickResponseData;

    /** the delegate to the model which we have to use for our calculations. */
    private ModelKickResponseData modelKickResponseData;

    /** true, if the data needs to be recalculated */
    private boolean dirty = true;

    /*
     * cached data fields
     */
    private Matrix differenceVector = new Matrix(1, 1);
    private Matrix differenceMatrix = new Matrix(1, 1);
    private Matrix relativeDiffMatrix = new Matrix(1, 1);

    private ArrayList<Double> monitorDifferenceRms = new ArrayList<Double>();
    private ArrayList<Double> correctorDifferenceRms = new ArrayList<Double>();
    private ArrayList<Double> monitorRelativeDiffRms = new ArrayList<Double>();
    private ArrayList<Double> correctorRelativeDiffRms = new ArrayList<Double>();

    /**
     * ensures, that the data is up to date
     */
    private void ensureUpToDate() {
        if (isDirty()) {
            calc();
            setDirty(false);
        }
    }

    /**
     * recalculates all value
     */
    private void calc() {
        calcDifferenceMatrix();
        Matrix vector = null;
        try {
            vector = calcDifferenceVector();
        } catch (CalculatorException e) {
            logger.error("Error while calculating difference vector.", e);
        }
        if (vector != null) {
            this.differenceVector = vector;
        }
        calcDifferenceRms();
        double fitQuality = calcFitQuality();

        logger.info("[Kick-response] norm of relative difference-matrix / sqrt(#elements) = " + fitQuality);
    }

    /**
     * calculates the Measurement-Model difference-vector
     * 
     * @return the vector
     * @throws CalculatorException
     */
    private Matrix calcDifferenceVector() throws CalculatorException {
        if (getKickResponseData() == null) {
            return null;
        }
        logger.debug("calculating difference - vector ...");
        // Matrix measurementResponse = getKickResponseData().getResponseMatrix();
        // Matrix responseMatrixModel = getModelKickResponseData()
        // .getResponseMatrix();
        // List<Double> monitorNoises = getMonitorNoises();
        // TMatrix<Boolean> validityMatrix = getKickResponseData()
        // .getValidityMatrix();
        //
        // if ((measurementResponse == null) || (responseMatrixModel == null)
        // || (monitorNoises == null)) {
        // throw new CalculatorException(
        // "One or more arguments are null! Cannot calculate Vector!");
        // }
        //
        // int monitorCount = measurementResponse.getRowDimension();
        // int correctorCount = measurementResponse.getColumnDimension();
        //
        // /* some checks */
        // if ((correctorCount != responseMatrixModel.getColumnDimension())
        // || (monitorCount != responseMatrixModel.getRowDimension())) {
        // throw new CalculatorException(
        // "Model- and Measurement- Response-matrices are not the same dimenion:"
        // + "measurement: " + monitorCount + "x"
        // + correctorCount + "; model: "
        // + responseMatrixModel.getRowDimension() + "x"
        // + responseMatrixModel.getColumnDimension() + ".");
        // }
        //
        // if (monitorCount != monitorNoises.size()) {
        // throw new CalculatorException(
        // "Number of Monitor noise-values is different from Row-dimension of Response-matrix.");
        // }
        //
        // /* define matrix with known initial capacity */
        // Matrix vector = new Matrix(responseMatrixModel.getColumnDimension()
        // * responseMatrixModel.getRowDimension(), 1);
        //
        // /* the actual calculation */
        // for (int i = 0; i < monitorCount; i++) {
        // double noise = monitorNoises.get(i);
        // for (int j = 0; j < correctorCount; j++) {
        //
        // /* do not take into account defect monitors/correctors */
        // if (!validityMatrix.get(i, j)) {
        // continue;
        // }
        //
        // double value = getNoiseWeighter().calcNoisyValue(
        // (measurementResponse.get(i, j) - responseMatrixModel
        // .get(i, j)), noise);
        // vector.set((i * correctorCount) + j, 0, value);
        // }
        // }

        /*
         * we simply flatten out the relative difference matrix
         */
        Matrix relDiffMatrix = this.relativeDiffMatrix;

        int monitorCount = relDiffMatrix.getRowDimension();
        int correctorCount = relDiffMatrix.getColumnDimension();

        Matrix vector = new Matrix(monitorCount * correctorCount, 1);
        for (int i = 0; i < monitorCount; i++) {
            for (int j = 0; j < correctorCount; j++) {
                vector.set((i * correctorCount) + j, 0, relDiffMatrix.get(i, j));
            }
        }

        logger.debug("   ... finished.");
        return vector;
    }

    /**
     * calculates the difference between the measurement and the actual model for each matrix-element.
     */
    public void calcDifferenceMatrix() {
        if ((getMonitorNoises() == null) || (getKickResponseData() == null)) {
            return;
        }

        List<Double> monitorNoises = getMonitorNoises();

        Matrix responseMatrixModel = getModelKickResponseData().getResponseMatrix();
        this.differenceMatrix = (getKickResponseData().getResponseMatrix().minus(responseMatrixModel));

        int monitorCount = this.differenceMatrix.getRowDimension();
        int correctorCount = this.differenceMatrix.getColumnDimension();

        /* set differences for defect correctors/monitors to zero: */
        TMatrix<Boolean> validityMatrix = getKickResponseData().getValidityMatrix();
        for (int i = 0; i < monitorCount; i++) {
            for (int j = 0; j < correctorCount; j++) {
                if (!validityMatrix.get(i, j)) {
                    differenceMatrix.set(i, j, 0);
                }
            }
        }

        /* calculate the differences relative to the noise */
        relativeDiffMatrix = new Matrix(monitorCount, correctorCount);
        List<Corrector> correctors = getMachineElementsManager().getActiveCorrectors();
        for (int i = 0; i < monitorCount; i++) {
            double noise = monitorNoises.get(i);
            for (int j = 0; j < correctorCount; j++) {
                Double kick = getKickResponseData().getCorrectorKick(correctors.get(j));
                if (validityMatrix.get(i, j) && (kick != null)) {
                    if (!ZeroUtil.isZero(kick)) {
                        relativeDiffMatrix.set(i, j, getNoiseWeighter().calcNoisyValue(this.differenceMatrix.get(i, j),
                                noise / kick));
                    }
                } else {
                    relativeDiffMatrix.set(i, j, 0);
                }
            }
        }
    }

    public double calcFitQuality() {
        if (getKickResponseData() == null) {
            return 0;
        }

        TMatrix<Boolean> validityMatrix = getKickResponseData().getValidityMatrix();

        /* count valid elements */
        int elementsCount = 0;
        for (int i = 0; i < validityMatrix.getRowDimension(); i++) {
            for (int j = 0; j < validityMatrix.getColumnDimension(); j++) {
                if (validityMatrix.get(i, j)) {
                    elementsCount++;
                }
            }
        }

        return relativeDiffMatrix.normF() / ((elementsCount > 1) ? Math.sqrt(elementsCount) : 1);
    }

    private void calcDifferenceRms() {
        monitorDifferenceRms.clear();
        monitorRelativeDiffRms.clear();
        correctorDifferenceRms.clear();
        correctorRelativeDiffRms.clear();

        int monitorCount = differenceMatrix.getRowDimension();
        int correctorCount = differenceMatrix.getColumnDimension();

        if (correctorCount > 1) {
            for (int i = 0; i < monitorCount; i++) {
                double normAbsolut = differenceMatrix.getMatrix(i, i, 0, correctorCount - 1).normF();
                monitorDifferenceRms.add(normAbsolut / Math.sqrt(correctorCount - 1));

                double normRelative = relativeDiffMatrix.getMatrix(i, i, 0, correctorCount - 1).normF();
                monitorRelativeDiffRms.add(normRelative / Math.sqrt(correctorCount - 1));
            }
        }

        if (monitorCount > 1) {
            for (int j = 0; j < correctorCount; j++) {
                double normAbsolute = differenceMatrix.getMatrix(0, monitorCount - 1, j, j).normF();
                correctorDifferenceRms.add(normAbsolute / Math.sqrt(monitorCount - 1));

                double normRelative = relativeDiffMatrix.getMatrix(0, monitorCount - 1, j, j).normF();
                correctorRelativeDiffRms.add(normRelative / Math.sqrt(monitorCount - 1));
            }
        }
    }

    private KickResponseData getKickResponseData() {
        return this.kickResponseData;
    }

    private TrajectoryData getStabilityData() {
        return getKickResponseData().getStabilityData();
    }

    private List<Double> getMonitorNoises() {
        if (getStabilityData() == null) {
            return ArrayUtil.createDefaultValueList(getMachineElementsManager().getActiveMonitorsCount(), 0.0);
        }
        return getStabilityData().getRmsValues();
    }

    /**
     * @param noiseWeighter the noiseWeighter to set
     */
    @Override
    public void setNoiseWeighter(NoiseWeighter noiseWeighter) {
        this.noiseWeighter = noiseWeighter;
    }

    /**
     * @return the noiseWeighter
     */
    private NoiseWeighter getNoiseWeighter() {
        return noiseWeighter;
    }

    /**
     * @param machineElementsManager the machineElementsManager to set
     */
    @Override
    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
        this.machineElementsManager.addListener(new MachineElementsManagerListener() {

            @Override
            public void changedActiveElements() {
                setDirty(true);
            }

            @Override
            public void changedActiveCorrector(int number, Corrector corrector) {
                /* do nothing */
            }

            @Override
            public void changedActiveMonitor(int number, Monitor monitor) {
                /* do nothing */
            }

            @Override
            public void changedElements() {
                setDirty(true);
            }

            @Override
            public void changedCorrectorGains() {
                /* do nothing */
            }

            @Override
            public void changedMonitorGains() {
                /* do nothing */
            }
        });
    }

    /**
     * @return the machineElementsManager
     */
    private MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    /**
     * @return the differenceVector
     */
    public Matrix getDifferenceVector() {
        ensureUpToDate();
        return differenceVector;
    }

    /**
     * @return the differenceMatrix
     */
    public Matrix getDifferenceMatrix() {
        ensureUpToDate();
        return differenceMatrix;
    }

    /**
     * @return the relativeDiffMatrix
     */
    public Matrix getRelativeDiffMatrix() {
        ensureUpToDate();
        return relativeDiffMatrix;
    }

    @Override
    public List<Double> getCorrectorDifferenceRms() {
        ensureUpToDate();
        return correctorDifferenceRms;
    }

    @Override
    public List<Double> getCorrectorRelativeDiffRms() {
        ensureUpToDate();
        return correctorRelativeDiffRms;
    }

    @Override
    public List<Double> getMonitorDifferenceRms() {
        ensureUpToDate();
        return monitorDifferenceRms;
    }

    @Override
    public List<Double> getMonitorRelativeDiffRms() {
        ensureUpToDate();
        return monitorRelativeDiffRms;
    }

    /**
     * @param dirty the dirty to set
     */
    private void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * @return the dirty
     */
    private boolean isDirty() {
        return dirty;
    }

    public void setKickResponseData(KickResponseData kickResponseData) {
        this.kickResponseData = kickResponseData;
    }

    public void setModelKickResponseData(ModelKickResponseData modelKickResponseData) {
        this.modelKickResponseData = modelKickResponseData;
        if (modelKickResponseData != null) {
            modelKickResponseData.addListener(new DynamicDataListener() {
                @Override
                public void becameDirty() {
                    setDirty(true);
                }
            });
        }
    }

    private ModelKickResponseData getModelKickResponseData() {
        return modelKickResponseData;
    }

}
