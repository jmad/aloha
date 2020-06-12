package cern.accsoft.steering.util.meas.data.yasp;

public interface YaspHeader {

    /**
     * Retrieve the string data for the given {@link YaspHeaderData}.
     * 
     * @param data the {@link YaspHeaderData} to retrieve
     * @return the value in the current yasp reading or an empty string in case the data was not found
     */
    public String getStringData(YaspHeaderData data);

    /**
     * Retrieve the long data for the given {@link YaspHeaderData}.
     * 
     * @param data the {@link YaspHeaderData} to retrieve
     * @return the value in the current yasp reading or 0L in case the data was not found
     */
    public Long getLongData(YaspHeaderData data);

    /**
     * Retrieve the double value for the given {@link YaspHeaderData}
     * 
     * @param data the {@link YaspHeaderData} to retrieve
     * @return the double value in the current yasp reading or {@link Double#NaN} in case the data was not found
     */
    public Double getDoubleData(YaspHeaderData data);
}
