package cern.accsoft.steering.util.meas.read.yasp;

import cern.accsoft.steering.util.meas.read.ReaderException;

public class YaspReaderException extends ReaderException {
    private static final long serialVersionUID = 1L;

    public YaspReaderException(String arg0) {
        super(arg0);
    }

    public YaspReaderException(Throwable arg0) {
        super(arg0);
    }

    public YaspReaderException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}