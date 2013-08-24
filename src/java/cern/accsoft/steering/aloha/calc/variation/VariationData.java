/*
 * $Id: VariationData.java,v 1.2 2009-03-16 16:38:11 kfuchsbe Exp $
 * 
 * $Date: 2009-03-16 16:38:11 $ $Revision: 1.2 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.calc.variation;

import java.util.List;

/**
 * this interface provides the data, which represents the input parameters / results of an aloha-fit. (fitting
 * kick-response, dispersion or both)
 * 
 * @author kfuchsbe
 */
public interface VariationData {

    public void reset();

    public int getVariationParametersCount();

    public abstract void addVariationParameter(VariationParameter parameter);

    public abstract void removeVariationParameter(String key);

    public abstract VariationParameter getVariationParameter(String key);

    public abstract List<VariationParameter> getVariationParameters();

    /**
     * @return the actual values of the variation parameters
     */
    public abstract List<Double> getVariationParameterValues();

    /**
     * @return the changes for each parameter
     */
    public abstract List<Double> getVariationParameterChanges();

    /**
     * @return the changes of each parameter wrt the initial value
     */
    public abstract List<Double> getVariationParameterRelativeChanges();

    /**
     * @return the errors for each parameter wrt the initial values
     */
    public abstract List<Double> getVariationParameterRelativeErrors();

    /**
     * @return the errors on the variation parameters
     */
    public abstract List<Double> getVariationParameterValueErrors();

    /**
     * @return the initial values of the variation parameters
     */
    public abstract List<Double> getVariationParameterInitialValues();

    /**
     * @return the names of the variation paramters
     */
    public abstract List<String> getVariationParameterNames();

    //
    // stuff for fixed variation-parameters
    //

    /**
     * @return all the variation parameters which are 'fixed' (i.e. not used for fitting at the moment)
     */
    public abstract List<VariationParameter> getFixedVariationParameters();

    /**
     * add the variation parameter of the given key to the fixed variation parameters and removes them from the varied
     * ones.
     * 
     * @param key the key of the variation parameter
     */
    public abstract void fixVariationParameter(String key);

    /**
     * add the variation parameter of the given key to the varied variation parameters and removes them from the fixed
     * ones ones.
     * 
     * @param key the key of the variation parameter
     */
    public abstract void unfixVariationParameter(String key);

    /**
     * @param listener the listener to add
     */
    public void addListener(VariationDataListener listener);

    /**
     * @param listener the listener to remove
     */
    public void removeListener(VariationDataListener listener);
}
