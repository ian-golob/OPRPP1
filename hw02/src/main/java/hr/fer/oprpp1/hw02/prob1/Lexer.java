package hr.fer.oprpp1.hw02.prob1;

import java.util.Arrays;

import static java.lang.Character.*;

/**
 * A simple implementation of a lexical analyzer.
 *
 * @author Ian Golob
 */
public class Lexer {

    private char[] data; // input text
    private Token token; // current token
    private int currentIndex; // last unprocessed character index
    private LexerState currentState;

    /**
     * Constructor that takes text to be tokenized as input.
     * @param text Text to be tokenized.
     */
    public Lexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
        token = null;
        currentState = LexerState.BASIC;
    }

    /**
     * Generates and returns the next token.
     * @return The next token.
     * @throws LexerException If there is an error generating the next token.
     */
    public Token nextToken() {
        if(currentIndex > data.length){
            throw new LexerException("There are no more tokens in the given text");
        }

        while(currentIndex < data.length && isWhitespace(data[currentIndex])) currentIndex++;

        Token token;

        if(currentIndex == data.length){
            currentIndex++;
            token = new Token(TokenType.EOF, null);
        }else{
            if( (currentState == LexerState.EXTENDED && isDigit(data[currentIndex]))
                || isLetter(data[currentIndex])
                || data[currentIndex] == '\\'){
                //WORD
                StringBuilder builder = new StringBuilder();

                int position = currentIndex;
                while(  position < data.length
                        && ((currentState == LexerState.EXTENDED && isDigit(data[position]))
                        || isLetter(data[position])
                            || data[position] == '\\')){

                    if(data[position] == '\\' && currentState == LexerState.BASIC){

                        if( position + 1 == data.length
                            || !(data[position+1] == '\\' || isDigit(data[position+1]))){
                            throw new LexerException("Invalid input");
                        }

                        builder.append(data[position + 1]);
                        position+=2;

                    }else {
                        builder.append(data[position++]);
                    }
                }

                currentIndex = position;

                token = new Token(TokenType.WORD, builder.toString());

            }else if(isDigit(data[currentIndex])){
                //NUMBER

                int position;
                for(position = currentIndex + 1;
                    position < data.length && isDigit(data[position]);
                    position++);

                String numberString = String.valueOf(Arrays.copyOfRange(data, currentIndex, position));

                try{
                    Long number = Long.parseLong(numberString);

                    currentIndex = position;
                    token = new Token(TokenType.NUMBER, number);

                } catch (NumberFormatException ex){
                    throw new LexerException("Invalid input");
                }

            }else{
                //SYMBOL
                token = new Token(TokenType.SYMBOL, data[currentIndex++]);
            }
        }

        this.token = token;

        return token;
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

    /**
     * Sets the state of the lexer, used to determine the next token.
     * @param state State of the lexer.
     */
    public void setState(LexerState state){
        if(state == null){
            throw new NullPointerException();
        }

        currentState = state;
    }

}
