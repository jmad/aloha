package cern.accsoft.steering.util.io;

public class TextFileParserException extends Exception {

    private static final long serialVersionUID = 1L;

    public TextFileParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextFileParserException(String message) {
        super(message);
    }

    public TextFileParserException(Throwable cause) {
        super(cause);
    }

}
