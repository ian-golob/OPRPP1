package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a parser of a string query.
 *
 * @author Ian Golob
 */
public class QueryParser {

    private boolean isDirectQuery;
    private String queriedJMBAG;
    private final List<ConditionalExpression> query = new ArrayList<>();

    private final String queryString;
    private int position = 0;

    /**
     * Creates a new QueryParser and parses the given queryString.
     * @param queryString The query string to parse.
     * @throws IllegalArgumentException If the query is invalid.
     */
    public QueryParser(String queryString){
        this.queryString = queryString;

        skipWhitespace();
        boolean firstExpression = true;

        do{
            if(firstExpression){
                firstExpression = false;
            }else{
                parseAnd();
            }

            IFieldValueGetter fieldValueGetter = parseFieldName();
            IComparisonOperator operator = parseOperator();
            String literal = parseLiteral();

            query.add(new ConditionalExpression(fieldValueGetter, literal, operator));

            skipWhitespace();
        }while(position < queryString.length());

        if(query.size() < 1){
            throw new IllegalArgumentException("The given query has no expression.");
        }

        if(query.size() == 1
                && query.get(0).getFieldGetter().equals(FieldValueGetters.JMBAG)
                && query.get(0).getComparisonOperator().equals(ComparisonOperators.EQUALS)){

            isDirectQuery = true;
            queriedJMBAG = query.get(0).getStringLiteral();
        }
    }

    private void skipWhitespace(){
        while(position < queryString.length()
                && Character.isWhitespace(queryString.charAt(position))){
            position++;
        }
    }

    /**
     * Parses the next AND from the query string, moves the parser position to the next non-parsed character.
     * Ignores whitespaces in the beginning, ignores case.
     * @throws IllegalArgumentException If there is no AND following the whitespace.
     */
    private void parseAnd(){
        skipWhitespace();

        if(position + 2 >= queryString.length()){
            throw new IllegalArgumentException();
        }

        String and = queryString.substring(position, position + 3);

        if(!and.equalsIgnoreCase("AND")){
            throw new IllegalArgumentException();
        }

        position+=3;
    }

    /**
     * Parses and returns the value getter for the parsed field name from the query string, moves the parser position to the next non-parsed character.
     * Valid field names are: firstName, lastName and jmbag.
     * Ignores whitespaces in the beginning.
     * @return The field value getter associated with the field name.
     * @throws IllegalArgumentException If the field name is invalid.
     */
    private IFieldValueGetter parseFieldName(){
        skipWhitespace();

        if(queryString.startsWith("firstName", position)){
            position += "firstName".length();

            return FieldValueGetters.FIRST_NAME;
        }

        if(queryString.startsWith("lastName", position)){
            position += "lastName".length();

            return FieldValueGetters.LAST_NAME;
        }

        if(queryString.startsWith("jmbag", position)){
            position += "jmbag".length();

            return FieldValueGetters.JMBAG;
        }

        throw new IllegalArgumentException();
    }

    /**
     * Parses and returns the next operator from the query string, moves the parser position to the next non-parsed character.
     * Valid operators are: <, <=, >, >=, =, != and LIKE.
     * Ignores whitespaces in the beginning.
     * @return The next comparison operator.
     * @throws IllegalArgumentException If the operator is invalid.
     */
    private IComparisonOperator parseOperator(){
        skipWhitespace();

        if(queryString.startsWith("<=", position)){
            position += "<=".length();

            return ComparisonOperators.LESS_OR_EQUALS;
        }

        if(queryString.startsWith("<", position)){
            position += "<".length();

            return ComparisonOperators.LESS;
        }

        if(queryString.startsWith(">=", position)){
            position += ">=".length();

            return ComparisonOperators.GREATER_OR_EQUALS;
        }

        if(queryString.startsWith(">", position)){
            position += ">".length();

            return ComparisonOperators.GREATER;
        }

        if(queryString.startsWith("=", position)){
            position += "=".length();

            return ComparisonOperators.EQUALS;
        }

        if(queryString.startsWith("!=", position)){
            position += "!=".length();

            return ComparisonOperators.NOT_EQUALS;
        }

        if(queryString.startsWith("LIKE", position)){
            position += "LIKE".length();

            return ComparisonOperators.LIKE;
        }

        throw new IllegalArgumentException();
    }

    /**
     * Parses and returns the next string literal from the query string, moves the parser position to the next non-parsed character.
     * Ignores whitespaces in the beginning.
     * @return The next string literal, without quotations.
     * @throws IllegalArgumentException If there is no string literal.
     */
    private String parseLiteral(){
        skipWhitespace();

        if(queryString.charAt(position) != '\"'){
            throw new IllegalArgumentException("Character \" expected, got: " + queryString.charAt(position) + ".");
        }

        int stringLiteralStart = position + 1;
        int stringLiteralEnd = queryString.indexOf('\"', position+1);

        if(stringLiteralEnd == -1){
            throw new IllegalArgumentException("Second character \" expected, none found.");
        }

        position = stringLiteralEnd + 1;

        return queryString.substring(stringLiteralStart, stringLiteralEnd);
    }

    /**
     * Returns true if the query was of the form jmbag="some_jmbag".
     * @return True if the query was of the form jmbag="some_jmbag".
     */
    public boolean isDirectQuery(){
        return isDirectQuery;
    }

    /**
     * If the query is a direct query returns the jmbag given in the direct query comparison,
     * if the query is not a direct query throws an IllegalStateException.
     * @return The jmbag given in the direct query comparison.
     * @throws IllegalStateException If the query is not a direct query.
     */
    public String getQueriedJMBAG(){
        if(!isDirectQuery){
            throw new IllegalStateException("The query must be a direct query to call this function.");
        }

        return queriedJMBAG;
    }

    /**
     * Returns a list of conditional expressions parsed from the query.
     * @return A list of conditional expressions parsed from the query.
     */
    public List<ConditionalExpression> getQuery(){
        return query;
    }

}
