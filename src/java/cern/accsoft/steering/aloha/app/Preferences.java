/*
 * $Id: Preferences.java,v 1.5 2008-09-09 21:13:26 kfuchsbe Exp $
 * 
 * $Date: 2008-09-09 21:13:26 $ 
 * $Revision: 1.5 $ 
 * $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.app;

import java.text.NumberFormat;

/**
 * interface for aloha - preferences
 * 
 * @author kfuchsbe
 * 
 */
public interface Preferences {

    /**
     * @return the number - format for aloha.
     */
    public NumberFormat getNumberFormat();

    /**
     * set a new numberFormat
     * 
     * @param numberFormat
     *            the {@link NumberFormat} to set
     */
    public void setNumberFormat(NumberFormat numberFormat);

    /**
     * @return the default path where looking for data
     */
    public String getDataPath();

    /**
     * set the dataPath for Aloha
     * 
     * @param dataPath
     */
    public void setInputPath(String dataPath);

    /**
     * @return the measurement-number to use when loading data
     */
    public Integer getMeasurementNumber();

    /**
     * @param measurementNumber
     *            the measurementNumber to set
     */
    public void setMeasurementNumber(Integer measurementNumber);

    /**
     * @param enabled
     *            true to enable selftest on startup, false to disable
     */
    public void setSelfTestEnabled(boolean enabled);

    /**
     * @return true if selftest on startup is enabled, false otherwise
     */
    public boolean isSelfTestEnabled();
    
}
