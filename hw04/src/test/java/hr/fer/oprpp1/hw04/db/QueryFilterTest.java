package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for QueryFilter.
 * @author Ian Golob
 */
public class QueryFilterTest {

    @Test
    public void testAccepts(){
        StudentRecord record1 = new StudentRecord("0036533922", "Ian", "Golob", 5);
        StudentRecord record2 = new StudentRecord("0036533923", "iaN", "goloB", 1);


        ConditionalExpression conditionalExpression1 =
                new ConditionalExpression(FieldValueGetters.FIRST_NAME, "*an", ComparisonOperators.LIKE);
        ConditionalExpression conditionalExpression2 =
                new ConditionalExpression(FieldValueGetters.LAST_NAME, "Golob", ComparisonOperators.EQUALS);

        QueryFilter filter = new QueryFilter(List.of(conditionalExpression1, conditionalExpression2));

        assertTrue(filter.accepts(record1));
        assertFalse(filter.accepts(record2));
    }


}
