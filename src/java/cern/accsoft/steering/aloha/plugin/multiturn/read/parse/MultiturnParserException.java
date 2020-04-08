/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.read.parse;

/**
 * @author kfuchsbe
 * 
 */
public class MultiturnParserException extends Exception {
	private static final long serialVersionUID = 1L;

	public MultiturnParserException() {
		super();
	}

	/**
	 * @param message
	 */
	public MultiturnParserException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MultiturnParserException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MultiturnParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
