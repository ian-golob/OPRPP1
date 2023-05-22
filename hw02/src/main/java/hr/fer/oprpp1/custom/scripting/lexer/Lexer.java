package hr.fer.oprpp1.custom.scripting.lexer;

import static java.lang.Character.*;

/**
 * An implementation of a lexer for the SmartScriptParser.
 *
 * @author Ian Golob
 */
public class Lexer {

    private final char[] data; // input data
    private LexerState state = LexerState.TEXT; // current lexer state
    private Token token; // last generated token
    private int currentIndex = 0; // last unprocessed character index
    private int nextCurrentIndex = 0;
    private TokenType nextTokenType;

    /**
     * Constructor that takes text to be tokenized as input.
     * @param text Text to be tokenized.
     */
    public Lexer(String text) {
        this.data = text.toCharArray();
    }

    /**
     * Generates and returns the next token.
     * @return The next token.
     * @throws LexerException If there is an error generating the next token.
     */
    public Token nextToken() {
        if(currentIndex > data.length){
            throw new LexerException();
        }

        if(currentIndex == data.length){
            currentIndex++;
            token = new Token(TokenType.EOF, null);
        }else{
            token = switch (state) {
                case TEXT -> getNextTextStateToken();
                case TAG_NAME -> getNextTagNameStateToken();
                case TAG -> getNextTagStateToken();
            };
        }

        return token;
    }

    /**
     * Parses and returns the next token for the lexer state TEXT.
     * Starts parsing from the currentIndex.
     * @return Next token.
     */
    private Token getNextTextStateToken() {
        nextCurrentIndex = currentIndex;
        String tokenString;
        nextTokenType = null;

        switch (data[nextCurrentIndex]) {
            case '{':

                if (nextCurrentIndex + 1 < data.length && data[nextCurrentIndex + 1] == '$') {
                    nextTokenType = TokenType.START_OF_TAG;
                    nextCurrentIndex+=2;
                    tokenString = "{$";
                } else {
                    nextTokenType = TokenType.TEXT;
                    tokenString = getNextTextString();
                }
                break;

            default:
                nextTokenType = TokenType.TEXT;
                tokenString = getNextTextString();
                break;
        }

        currentIndex = nextCurrentIndex;

        return new Token(nextTokenType, tokenString);
    }

    /**
     * Parses and returns the next token for the lexer state TAG_NAME.
     * Starts parsing from the currentIndex.
     * @return Next token.
     */
    private Token getNextTagNameStateToken() {
        nextCurrentIndex = currentIndex;

        skipToNextNonWhitespace();

        String tokenString;

        switch (data[nextCurrentIndex]) {
            case '=':
                tokenString = "=";
                nextCurrentIndex++;
                break;

            default:
                tokenString = getNextVariableString();
                break;
        }

        currentIndex = nextCurrentIndex;

        return new Token(TokenType.TAG_NAME, tokenString);
    }

    /**
     * Parses and returns the next token for the lexer state TAG.
     * Starts parsing from the currentIndex.
     * @return Next token.
     */
    private Token getNextTagStateToken() {

        nextCurrentIndex = currentIndex;

        skipToNextNonWhitespace();

        String tokenString;

        switch (data[nextCurrentIndex]) {

            case '@':
                nextTokenType = TokenType.FUNCTION;
                tokenString = getNextFunctionString();
                break;

            case '\"':
                nextTokenType = TokenType.STRING;
                tokenString = getNextStringString();
                break;

            case '-':
                if(nextCurrentIndex+1 < data.length && isDigit(data[nextCurrentIndex+1])){
                    tokenString = getNextNumberString();
                    break;
                }

            case '+':
            case '*':
            case '/':
            case '^':
                nextTokenType = TokenType.OPERATOR;
                tokenString = Character.toString(data[nextCurrentIndex++]);
                break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                tokenString = getNextNumberString();
                break;

            case '$':
                if(nextCurrentIndex+1 == data.length || data[nextCurrentIndex+1] != '}'){
                    throw new LexerException();
                }
                nextTokenType = TokenType.END_OF_TAG;
                tokenString = "$}";
                nextCurrentIndex+=2;
                break;

            default:
                nextTokenType = TokenType.VARIABLE;
                tokenString = getNextVariableString();
                break;
        }

        currentIndex = nextCurrentIndex;

        return new Token(nextTokenType, tokenString);
    }

    /**
     * Parses and returns the next longest string following the rules of TokenType.TEXT.
     * Starts parsing from index nextCurrentIndex and increments it.
     * @return Next longest string following the rules of TokenType.TEXT.
     */
    private String getNextTextString(){
        StringBuilder buffer = new StringBuilder();
        boolean endBuffering = false;

        while(!endBuffering && nextCurrentIndex < data.length){

            switch(data[nextCurrentIndex]){

                case '\\':
                    switch (data[nextCurrentIndex + 1]) {
                        case '\\':
                            buffer.append("\\");
                            break;
                        case '{':
                            buffer.append("{");
                            break;
                        default:
                            throw new LexerException();
                    }
                    nextCurrentIndex += 2;
                    break;

                case '{':
                    if(nextCurrentIndex+1 < data.length && data[nextCurrentIndex+1] == '$'){
                        endBuffering = true;
                        break;
                    }

                default:
                    buffer.append(data[nextCurrentIndex++]);
                    break;
            }

        }

        return buffer.toString();
    }

    /**
     * Parses and returns the next longest string following the rules of TokenType.VARIABLE.
     * Starts parsing from index nextCurrentIndex and increments it.
     * @return Next longest string following the rules of TokenType.VARIABLE.
     * @throws LexerException If the variable name is invalid.
     */
    private String getNextVariableString(){

        if(!Character.isLetter(data[nextCurrentIndex])){
            throw new LexerException();
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append(data[nextCurrentIndex++]);

        while(nextCurrentIndex < data.length){

            if( isLetter(data[nextCurrentIndex])
                || isDigit(data[nextCurrentIndex])
                || data[nextCurrentIndex] == '_'){

                buffer.append(data[nextCurrentIndex++]);

            }else{
                break;
            }

        }

        return buffer.toString();
    }

    /**
     * Parses and returns the next longest string following the rules of TokenType.FUNCTION.
     * Starts parsing from index nextCurrentIndex and increments it.
     * @return Next longest string following the rules of TokenType.FUNCTION.
     * @throws LexerException If the function name is invalid.
     */
    private String getNextFunctionString(){

        if(data[nextCurrentIndex] != '@'){
            throw new LexerException();
        }

        if( nextCurrentIndex + 1 == data.length
            || !Character.isLetter(data[nextCurrentIndex+1])){
            throw new LexerException();
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append(data[nextCurrentIndex++]);
        buffer.append(data[nextCurrentIndex++]);


        while(nextCurrentIndex < data.length){

            if( isLetter(data[nextCurrentIndex])
                    || isDigit(data[nextCurrentIndex])
                    || data[nextCurrentIndex] == '_'){

                buffer.append(data[nextCurrentIndex++]);

            }else{
                break;
            }

        }

        return buffer.toString();
    }

    /**
     * Parses and returns the next longest string following the rules of TokenType.STRING.
     * Starts parsing from index nextCurrentIndex and increments it.
     * @return Next longest string following the rules of TokenType.STRING.
     * @throws LexerException If the string does not start with an opening character (") or if it is invalid.
     * @throws IndexOutOfBoundsException If the string has no closing character (").
     */
    private String getNextStringString(){

        if(data[nextCurrentIndex] != '\"'){
            throw new LexerException();
        }

        StringBuilder buffer = new StringBuilder();
        nextCurrentIndex++; // "

        boolean endBuffering = false;

        while(!endBuffering){

            switch(data[nextCurrentIndex]){

                case '\\':
                    switch (data[nextCurrentIndex+1]){
                        case '\\':
                            buffer.append("\\");
                            break;
                        case '\"':
                            buffer.append("\"");
                            break;
                        case 'n':
                            buffer.append("\n");
                            break;
                        case 'r':
                            buffer.append("\r");
                            break;
                        case 't':
                            buffer.append("\t");
                            break;
                        default:
                            throw new LexerException();
                    }
                    nextCurrentIndex += 2;
                    break;

                case '\"':
                    endBuffering = true;
                    nextCurrentIndex++;
                    break;

                default:
                    buffer.append(data[nextCurrentIndex++]);
                    break;
            }
        }

        return buffer.toString();
    }

    /**
     * Parses and returns the next longest string following the rules of TokenType.DOUBLE or TokenType.INTEGER.
     * Starts parsing from index nextCurrentIndex and increments it.
     * Changes nextTokenType to DOUBLE or INTEGER.
     * @return Next longest string following the rules of TokenType.DOUBLE or TokenType.INTEGER.
     * @throws LexerException If the number is invalid.
     */
    private String getNextNumberString(){

        if(data[nextCurrentIndex] != '-' && !isDigit(data[nextCurrentIndex])) {
            throw new LexerException();
        }

        StringBuilder buffer = new StringBuilder();

        if(data[nextCurrentIndex] == '-'){
            buffer.append(data[nextCurrentIndex++]);
        }

        if(!isDigit(data[nextCurrentIndex])){
            throw new LexerException();
        }

        buffer.append(data[nextCurrentIndex++]);

        boolean isDecimal = false;
        while(nextCurrentIndex < data.length){

            if(isDigit(data[nextCurrentIndex])){

                buffer.append(data[nextCurrentIndex++]);

            } else if(data[nextCurrentIndex] == '.'){

                if(isDecimal){
                    throw new LexerException();
                }

                isDecimal = true;
                buffer.append(data[nextCurrentIndex++]);

            } else {
                break;
            }

        }

        if(isDecimal){
            nextTokenType = TokenType.DOUBLE;
        }else{
            nextTokenType = TokenType.INTEGER;
        }

        return buffer.toString();

    }

    /**
     * Changes the nextCurrentIndex to the next non-whitespace character.
     * @throws IndexOutOfBoundsException If the next non-whitespace character does not exist.
     */
    private void skipToNextNonWhitespace() {

        while(isWhitespace(data[nextCurrentIndex])){
            nextCurrentIndex++;
        }

    }

    /**
     * Returns the last generated token. Can be called multiple times.
     * Doesn't start generation of a new token.
     *
     * @return The last generated token.
     */
    public Token getToken() {
        return token;
    }

    public void setState(LexerState state){
        if(state == null){
            throw new NullPointerException();
        }

        this.state = state;
    }

}
