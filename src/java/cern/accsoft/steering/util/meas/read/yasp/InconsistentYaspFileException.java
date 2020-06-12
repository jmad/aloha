package cern.accsoft.steering.util.meas.read.yasp;

public class InconsistentYaspFileException extends YaspReaderException {
    private static final long serialVersionUID = 1L;

    public InconsistentYaspFileException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public InconsistentYaspFileException(String arg0) {
        super(arg0);
    }

    public InconsistentYaspFileException(Throwable arg0) {
        super(arg0);
    }

}
