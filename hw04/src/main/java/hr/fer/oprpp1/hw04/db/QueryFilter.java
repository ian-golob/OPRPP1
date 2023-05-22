package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * A class that represents a student record filter that filters student records
 * based on the given list of conditional expressions.
 *
 * @author Ian Golob
 */
public class QueryFilter implements IFilter{

    List<ConditionalExpression> query;

    /**
     * Creates a filter that filters student records
     * based on all of the given conditional expressions.
     * @param query The given list of conditional expressions.
     */
    public QueryFilter(List<ConditionalExpression> query){
        this.query = query;
    }

    @Override
    public boolean accepts(StudentRecord record) {

        for(ConditionalExpression expression: query){

            IComparisonOperator operator = expression.getComparisonOperator();
            String fieldValue = expression.getFieldGetter().get(record);
            String stringLiteral = expression.getStringLiteral();

            if(!operator.satisfied(fieldValue, stringLiteral)){
                return false;
            }

        }

        return true;
    }

}
