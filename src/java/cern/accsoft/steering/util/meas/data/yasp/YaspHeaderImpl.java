package cern.accsoft.steering.util.meas.data.yasp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class YaspHeaderImpl implements YaspHeader {
    private static final Logger LOGGER = Logger.getLogger(YaspHeaderImpl.class);

    /** the string data of the header */
    private Map<YaspHeaderData, String> stringData = new HashMap<YaspHeaderData, String>();
    /** the long data of the header */
    private Map<YaspHeaderData, Long> longData = new HashMap<YaspHeaderData, Long>();
    /** the double data of the header */
    private Map<YaspHeaderData, Double> doubleData = new HashMap<YaspHeaderData, Double>();

    @Override
    public String getStringData(YaspHeaderData data) {
        if (this.stringData.containsKey(data)) {
            return this.stringData.get(data);
        }

        LOGGER.error("Header does not contain string data for key: " + data.getYaspTag());
        return "";
    }

    @Override
    public Long getLongData(YaspHeaderData data) {
        if (this.longData.containsKey(data)) {
            return longData.get(data);
        }

        LOGGER.error("Header does not contain long data for key: " + data.getYaspTag());
        return 0L;
    }
    

    @Override
    public Double getDoubleData(YaspHeaderData data) {
        if(this.doubleData.containsKey(data)) {
            return this.doubleData.get(data);
        }
        
        LOGGER.error("Header does not contain double data for key: " + data.getYaspTag());
        return Double.NaN;
    }

    public void addEntry(YaspHeaderData key, String value) {
        this.stringData.put(key, value);

        if (Long.class == key.getDataTypeClass()) {
            try {
                Long longValue = new Long(value);
                this.longData.put(key, longValue);
            } catch (NumberFormatException e) {
                LOGGER.error("Could not convert yasp header entry [" + key.getYaspTag() //
                        + "] = " + value + " to long.");
            }
        } else if(Double.class == key.getDataTypeClass()) {
            try {
                Double doubleValue = new Double(value);
                this.doubleData.put(key, doubleValue);
            } catch (NumberFormatException e) {
                LOGGER.error("Could not convert yasp header entry [" + key.getYaspTag() //
                        + "] = " + value + " to double.");
            }
        }
    }
}
