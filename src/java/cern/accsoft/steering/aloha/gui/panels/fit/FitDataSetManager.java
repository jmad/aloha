package cern.accsoft.steering.aloha.gui.panels.fit;

import cern.accsoft.steering.aloha.calc.Calculator;
import cern.accsoft.steering.aloha.calc.CalculatorListener;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;
import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManagerListener;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.util.gui.dv.ds.AbstractJmadDataSet;
import cern.accsoft.steering.util.gui.dv.ds.ListDataSet;
import cern.accsoft.steering.util.gui.dv.ds.MatrixColumnDataSet;
import cern.jdve.data.DataSet;

import java.util.HashMap;

/**
 * this class manages some datasets for displaying the fit data
 * 
 * @author kfuchsbe
 */
public class FitDataSetManager {

    private MachineElementsManager machineElementsManager;

    private final HashMap<DS, DataSet> dataSets = new HashMap<>();

    //
    // the sources for all the data (to be injected)
    //

    private Calculator calculator;
    private ModelDelegate modelDelegate;
    private SensitivityMatrixManager sensitivityMatrixManager;
    private VariationData variationData;

    /**
     * This is the enum for all available 2D - DataSets
     */
    public enum DS {
        DIFFERENCE_VECTOR, DELTA_PARAMETER_VALUES, //
        CORRECTOR_GAINS, MONITOR_GAINS, VARIATION_PARAMETER_VALUES, //
        VARIATION_PARAMETER_INITIAL_VALUES, VARIATION_PARAMETER_CHANGES, VARIATION_PARAMETER_RELATIVE_CHANGES;
    }

    /**
     * getter method for 2D - DataSets. Lazy loading.
     * 
     * @param ds the type of the 2D - DataSet
     * @return the DataSet.
     */
    public DataSet getDataSet(DS ds) {
        if (ds == null) {
            return null;
        }

        if (dataSets.get(ds) == null) {
            dataSets.put(ds, createDataSet(ds));
        }
        return dataSets.get(ds);
    }

    /**
     * Factory - method for 2D - DataSets
     * 
     * @param ds the type of DataSet to create
     * @return the DataSet
     */
    private DataSet createDataSet(DS ds) {
        DsAdapter<? extends DataSet> adapter;
        switch (ds) {
        case DIFFERENCE_VECTOR:
            adapter = new DsAdapter<MatrixColumnDataSet>(new MatrixColumnDataSet("Difference - Vector")) {
                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setMatrix(getSensitivityMatrixManager().getActiveDifferenceVector());
                }

            };

            break;

        case DELTA_PARAMETER_VALUES:
            adapter = new DsAdapter<MatrixColumnDataSet>(new MatrixColumnDataSet("delta Parameter-Values")) {
                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setMatrix(calculator.getDeltaParameterValues());
                }

            };
            break;

        case CORRECTOR_GAINS:
            adapter = new DsAdapter<ListDataSet>(new ListDataSet("Corrector gains")) {

                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setValues(null, getMachineElementsManager().getActiveCorrectorGains(),
                            getMachineElementsManager().getActiveCorrectorGainErrors(), null);
                    dataSet.setLabels(getMachineElementsManager().getActiveCorrectorNames());
                }
            };
            break;

        case MONITOR_GAINS:
            adapter = new DsAdapter<ListDataSet>(new ListDataSet("Monitor gains")) {
                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setValues(null, getMachineElementsManager().getActiveMonitorGains(),
                            getMachineElementsManager().getActiveMonitorGainErrors(), null);
                    dataSet.setLabels(getMachineElementsManager().getActiveMonitorNames());
                }

            };
            break;

        case VARIATION_PARAMETER_VALUES:
            adapter = new DsAdapter<ListDataSet>(new ListDataSet("Additional variation-parameter values")) {
                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setValues(null, getVariationData().getVariationParameterValues(), getVariationData()
                            .getVariationParameterValueErrors(), null);
                    dataSet.setLabels(getVariationData().getVariationParameterNames());
                }
            };
            break;

        case VARIATION_PARAMETER_INITIAL_VALUES:
            adapter = new DsAdapter<ListDataSet>(new ListDataSet("Additional variation-parameter values")) {
                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setYValues(getVariationData().getVariationParameterInitialValues());
                    dataSet.setLabels(getVariationData().getVariationParameterNames());
                }
            };
            break;

        case VARIATION_PARAMETER_CHANGES:
            adapter = new DsAdapter<ListDataSet>(new ListDataSet("Additional variation-parameter values")) {
                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setValues(null, getVariationData().getVariationParameterChanges(), getVariationData()
                            .getVariationParameterValueErrors(), null);
                    dataSet.setLabels(getVariationData().getVariationParameterNames());
                }
            };
            break;

        case VARIATION_PARAMETER_RELATIVE_CHANGES:
            adapter = new DsAdapter<ListDataSet>(new ListDataSet("Additional variation-parameter values")) {
                @Override
                public void changedCalculatedValues(Calculator calculator) {
                    dataSet.setValues(null, getVariationData().getVariationParameterRelativeChanges(),
                            getVariationData().getVariationParameterRelativeErrors(), null);
                    dataSet.setLabels(getVariationData().getVariationParameterNames());
                }
            };
            break;

        default:
            return null;
        }
        return adapter.getDataSet();
    }

    /**
     * @param modelDelegate the modelDelegate to set
     */
    public void setModelDelegate(ModelDelegate modelDelegate) {
        this.modelDelegate = modelDelegate;
    }

    /**
     * @return the modelDelegate
     */
    public ModelDelegate getModelDelegate() {
        return modelDelegate;
    }

    /**
     * @param sensitivityMatrixManager the sensityMatrixManager to set
     */
    public void setSensitivityMatrixManager(SensitivityMatrixManager sensitivityMatrixManager) {
        this.sensitivityMatrixManager = sensitivityMatrixManager;
    }

    /**
     * @return the sensityMatrixManager
     */
    public SensitivityMatrixManager getSensitivityMatrixManager() {
        return sensitivityMatrixManager;
    }

    /**
     * @param variationData the variationData to set
     */
    public void setVariationData(VariationData variationData) {
        this.variationData = variationData;
    }

    /**
     * @return the variationData
     */
    public VariationData getVariationData() {
        return variationData;
    }

    /**
     * @param calculatorManager the calculator to set
     */
    public void setCalculator(Calculator calculatorManager) {
        this.calculator = calculatorManager;
    }

    /**
     * @return the calculator
     */
    public Calculator getCalculator() {
        return calculator;
    }

    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    public MachineElementsManager getMachineElementsManager() {
        return machineElementsManager;
    }

    /*
     * TODO: also listen on measurement changes, to update the values wrt to the active model
     */
    private class DsAdapter<T extends DataSet> implements CalculatorListener, MachineElementsManagerListener {
        protected T dataSet;

        private DsAdapter(T dataSet) {
            this.dataSet = dataSet;
            init();
            if (getCalculator() != null) {
                getCalculator().addListener(this);
            }
        }

        /**
         * calls all functions once to initialize the values.
         */
        private void init() {
            if (getCalculator() != null) {
                changedCalculatedValues(getCalculator());
            }
        }

        public T getDataSet() {
            return dataSet;
        }

        /*
         * following methods are not foreseen to be overridden.
         */
        @Override
        public void changedActiveElements() {
            if (dataSet instanceof AbstractJmadDataSet) {
                ((AbstractJmadDataSet) dataSet).refresh();
            }
        }

    }
}
