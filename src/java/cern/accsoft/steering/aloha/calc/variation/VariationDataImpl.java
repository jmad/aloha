/*
 * $Id: VariationDataImpl.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.2 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.variation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kfuchsbe
 */
public class VariationDataImpl implements VariationData {
    private final static Logger LOGGER = LoggerFactory.getLogger(VariationDataImpl.class);

    /** the additional parameters which will be varied during fitting */
    private Map<String, VariationParameter> variationParameters = new LinkedHashMap<>();

    /** the variation parameters which are not varied in the current fitting. */
    private Map<String, VariationParameter> fixedVariationParameters = new LinkedHashMap<>();

    /** the listeners to this data */
    private List<VariationDataListener> listeners = new ArrayList<>();

    @Override
    public void addVariationParameter(VariationParameter parameter) {
        variationParameters.put(parameter.getKey(), parameter);
        fireChangedVariationParameters();
    }

    @Override
    public VariationParameter getVariationParameter(String key) {
        return variationParameters.get(key);
    }

    @Override
    public List<Double> getVariationParameterInitialValues() {
        ArrayList<Double> values = new ArrayList<>();
        for (VariationParameter param : variationParameters.values()) {
            values.add(param.getActiveMeasurementInitialValue());
        }
        return values;
    }

    @Override
    public List<Double> getVariationParameterValues() {
        ArrayList<Double> values = new ArrayList<>();
        for (VariationParameter param : variationParameters.values()) {
            values.add(param.getActiveMeasurementAbsoluteValue());
        }
        return values;
    }

    @Override
    public List<Double> getVariationParameterValueErrors() {
        ArrayList<Double> errors = new ArrayList<>();
        for (VariationParameter param : variationParameters.values()) {
            errors.add(param.getError());
        }
        return errors;
    }

    @Override
    public List<VariationParameter> getVariationParameters() {
        return new ArrayList<>(variationParameters.values());
    }

    @Override
    public void removeVariationParameter(String key) {
        VariationParameter parameter = getVariationParameter(key);
        if (parameter == null) {
            return;
        }
        parameter.reset();
        variationParameters.remove(key);
        fixedVariationParameters.remove(key);
        fireChangedVariationParameters();
    }

    @Override
    public ArrayList<String> getVariationParameterNames() {
        ArrayList<String> names = new ArrayList<>();
        for (VariationParameter param : variationParameters.values()) {
            names.add(param.getName());
        }
        return names;
    }

    @Override
    public void reset() {
        for (VariationParameter parameter : variationParameters.values()) {
            parameter.reset();
        }
        for (VariationParameter parameter : fixedVariationParameters.values()) {
            parameter.reset();
        }
    }

    @Override
    public int getVariationParametersCount() {
        return this.variationParameters.size();
    }

    /**
     * notifies all listeners, that the number of variation parameters have changed.
     */
    private void fireChangedVariationParameters() {
        for (VariationDataListener listener : listeners) {
            listener.changedVariationParameters();
        }
    }

    @Override
    public void addListener(VariationDataListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(VariationDataListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void fixVariationParameter(String key) {
        moveParameter(key, this.variationParameters, this.fixedVariationParameters);
    }

    @Override
    public void unfixVariationParameter(String key) {
        moveParameter(key, this.fixedVariationParameters, this.variationParameters);
    }

    @Override
    public List<VariationParameter> getFixedVariationParameters() {
        return new ArrayList<>(this.fixedVariationParameters.values());
    }

    /**
     * moves the variation parameter of the given key from one map to the other.
     * 
     * @param key
     * @param source
     * @param dest
     */
    private void moveParameter(String key, Map<String, VariationParameter> source, Map<String, VariationParameter> dest) {
        VariationParameter parameter = source.get(key);
        if (parameter == null) {
            LOGGER.warn("No variation parameter with the key '" + key + "' found.");
            return;
        }
        source.remove(key);
        dest.put(key, parameter);
        fireChangedVariationParameters();
    }

    @Override
    public List<Double> getVariationParameterChanges() {
        ArrayList<Double> changes = new ArrayList<>();
        for (VariationParameter param : variationParameters.values()) {
            changes.add(param.getOffsetChange());
        }
        return changes;
    }

    @Override
    public List<Double> getVariationParameterRelativeChanges() {
        ArrayList<Double> changes = new ArrayList<>();
        for (VariationParameter param : variationParameters.values()) {
            changes.add(param.getActiveMeasurementRelativeChange());
        }
        return changes;
    }

    @Override
    public List<Double> getVariationParameterRelativeErrors() {
        ArrayList<Double> errors = new ArrayList<>();
        for (VariationParameter param : variationParameters.values()) {
            Double initialValue = param.getActiveMeasurementInitialValue();
            if ((initialValue != null) && (Math.abs(initialValue) > 1e-8)) {
                errors.add(param.getError() / initialValue);
            } else {
                errors.add(0.0);
            }
        }
        return errors;

    }

}
