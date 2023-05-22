package hr.fer.oprpp1.custom.scripting.parser;

/**
 * If any exception occurs while parsing,
 * an instance of this exception is thrown.
 *
 * @author Ian Golob
 */
public class SmartScriptParserException extends RuntimeException {

    public SmartScriptParserException() {
    }

    public SmartScriptParserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new SmartScriptParserException with the specified detail message.
     * @param message The detail message.
     */
    public SmartScriptParserException(String message) {
        super(message);
    }
}
