package hr.fer.oprpp1.hw02.prob1;

/**
 * Represents an exception that occurred while analyzing the Lexer input
 * or wrong usage of the Lexer.
 *
 * @author Ian Golob
 */
public class LexerException extends RuntimeException {

    public LexerException() {
    }

    public LexerException(String message) {
        super(message);
    }
}
