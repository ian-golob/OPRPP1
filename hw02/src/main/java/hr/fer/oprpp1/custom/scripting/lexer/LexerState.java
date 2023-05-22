package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Lexer states.
 *
 * @author Ian Golob
 */
public enum LexerState {

    /**
     * In this state the lexer should generate
     * the TEXT token, START_OF_TAG token and EOF token.
     */
    TEXT,

    /**
     * In this state the lexer should generate a TAG_NAME token.
     */
    TAG_NAME,

    /**
     * In this state the lexer should generate the END_OF_TAG token and
     * other tokens that are found in the tag.
     */
    TAG,

}
