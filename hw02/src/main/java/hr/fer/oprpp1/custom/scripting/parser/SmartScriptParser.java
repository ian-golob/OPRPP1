package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.*;
import hr.fer.oprpp1.custom.scripting.nodes.*;

import java.util.Arrays;

/**
 * This class represents a parser.
 * Used to parse the specified "SmartScript language" text to a node/element tree.
 *
 * @author Ian Golob
 */
public class SmartScriptParser {

    private final String text;
    private final Lexer lexer;
    private DocumentNode documentNode;
    private ObjectStack nodeStack;

    /**
     * Creates an instance of the SmartScriptParser and parses the given text into a node/element tree.
     * @param text Text to parse.
     */
    public SmartScriptParser(String text) {
        this.text = text;
        lexer = new Lexer(text);
        parseText();
    }

    /**
     * Begins parsing the given text.
     */
    public void parseText(){
        documentNode = new DocumentNode();
        nodeStack = new ObjectStack();
        nodeStack.push(documentNode);

        try{

            generateAndConsumeAllTokens();

        }catch (Exception ex){
            throw new SmartScriptParserException(ex);
        }

        if(nodeStack.size() != 1){
            throw new SmartScriptParserException();
        }

    }

    /**
     * Generates tokens using lexer.nextToken() until it receives the EOF token.
     * When generated, consumes a token to build the document node/element tree.
     */
    private void generateAndConsumeAllTokens(){

        Token token;
        while((token = lexer.nextToken()).getType() != TokenType.EOF){

            if(token.getType() == TokenType.TEXT){
                Node textNode = new TextNode((String) token.getValue());
                ((Node) nodeStack.peek()).addChildNode(textNode);

            } else if (token.getType() == TokenType.START_OF_TAG) {

                lexer.setState(LexerState.TAG_NAME);
                Token tagNameToken = lexer.nextToken();
                lexer.setState(LexerState.TAG);

                if(tagNameToken.getType() != TokenType.TAG_NAME){
                    throw new SmartScriptParserException();
                }

                switch (tagNameToken.getValue().toString().toUpperCase()){
                    case "=":
                        generateAndConsumeEchoTagTokens();
                        break;
                    case "FOR":
                        generateAndConsumeForTagTokens();
                        break;
                    case "END":
                        generateAndConsumeEndTagTokens();
                        break;
                    default:
                        throw new SmartScriptParserException("Invalid tag name.");
                }

                lexer.setState(LexerState.TEXT);

            } else {
                throw new SmartScriptParserException();
            }
        }

    }

    /**
     * Generates the remaining ECHO tag tokens and consumes them.
     * (does not generate the START_OF_TAG or TAG_NAME tokens).
     * The last token generated and consumed is the END_OF_TAG token.
     */
    private void generateAndConsumeEchoTagTokens(){
        List elementList = new ArrayIndexedCollection();

        Token echoToken;
        while((echoToken = lexer.nextToken()).getType() != TokenType.END_OF_TAG){
            elementList.add(getElementFromToken(echoToken));
        }

        EchoNode echoNode = new EchoNode(Arrays.copyOf(elementList.toArray(), elementList.size(), Element[].class));
        ((Node) nodeStack.peek()).addChildNode(echoNode);
    }

    /**
     * Generates the remaining FOR tag tokens and consumes them.
     * (does not generate the START_OF_TAG or TAG_NAME tokens).
     * The last token generated and consumed is the END_OF_TAG token.
     */
    private void generateAndConsumeForTagTokens(){
        ElementVariable variable = null;
        Element startExpression = null;
        Element endExpression = null;
        Element stepExpression = null;

        Token variableToken = lexer.nextToken();
        if(variableToken.getType() != TokenType.VARIABLE){
            throw new SmartScriptParserException("Invalid FOR variable.");
        }
        variable = (ElementVariable) getElementFromToken(variableToken);

        Token startExpressionToken = lexer.nextToken();
        if(!isValidForTagTokenType(startExpressionToken)){
            throw new SmartScriptParserException("Invalid FOR start expression.");
        }
        startExpression = getElementFromToken(startExpressionToken);

        Token endExpressionToken = lexer.nextToken();
        if(!isValidForTagTokenType(endExpressionToken)){
            throw new SmartScriptParserException("Invalid FOR end expression.");
        }
        endExpression = getElementFromToken(endExpressionToken);

        Token stepExpressionToken = lexer.nextToken();
        if(stepExpressionToken.getType() != TokenType.END_OF_TAG){

            if(!isValidForTagTokenType(stepExpressionToken)){
                throw new SmartScriptParserException("Invalid FOR step expression.");
            }

            stepExpression = getElementFromToken(stepExpressionToken);

            Token endToken = lexer.nextToken();

            if(endToken.getType() != TokenType.END_OF_TAG){
                throw new SmartScriptParserException();
            }

        }

        ForLoopNode forLoopNode = new ForLoopNode(variable, startExpression, endExpression, stepExpression);

        ((Node) nodeStack.peek()).addChildNode(forLoopNode);

        nodeStack.push(forLoopNode);
    }

    /**
     * Generates the remaining END tag tokens and consumes them
     * (does not generate the START_OF_TAG or TAG_NAME tokens).
     * The last token generated and consumed is the END_OF_TAG token.
     */
    private void generateAndConsumeEndTagTokens(){
        if(lexer.nextToken().getType() != TokenType.END_OF_TAG){
            throw new SmartScriptParserException();
        }

        nodeStack.pop();
    }

    /**
     * Returns the Element representation of a Token.
     * Valid TokenTypes are DOUBLE, INTEGER, FUNCTION, OPERATOR, STRING and VARIABLE.
     * @param token Given token.
     * @return The Element representation of the given token.
     * @throws SmartScriptParserException If TokenType is invalid.
     * @throws NullPointerException If given token is null.
     */
    private Element getElementFromToken(Token token) {

        if(token == null){
            throw new NullPointerException();
        }

        switch (token.getType()){
            case DOUBLE:
                return new ElementConstantDouble(Double.parseDouble((String) token.getValue()));

            case INTEGER:
                return new ElementConstantInteger(Integer.parseInt((String) token.getValue()));

            case FUNCTION:
                return new ElementFunction((String) token.getValue());

            case OPERATOR:
                return new ElementOperator((String) token.getValue());

            case STRING:
                return new ElementString((String) token.getValue());

            case VARIABLE:
                return new ElementVariable((String) token.getValue());

            default:
                throw new SmartScriptParserException();
        }

    }

    /**
     * Returns the generated document node.
     * @return Document node or null if the document was not parsed.
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }

    /**
     * Checks if the given token is valid to be a start, end or step expression in a FOR tag.
     * A valid token is not null and of type STRING, VARIABLE, INTEGER or DOUBLE.
     *
     * @param token Token to be checked.
     * @return True if the token is valid to be in a FOR tag, false otherwise.
     */
    private static boolean isValidForTagTokenType(Token token){
        if(token == null){
            return false;
        }

        return  token.getType() == TokenType.STRING
                || token.getType() == TokenType.VARIABLE
                || token.getType() == TokenType.INTEGER
                || token.getType() == TokenType.DOUBLE;
    }

}
