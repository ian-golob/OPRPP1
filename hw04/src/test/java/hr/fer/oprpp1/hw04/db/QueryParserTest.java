package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {

    @Test
    public void testIsDirectQuery(){
        QueryParser qp1 = new QueryParser(" jmbag=\"0123456789\" \t");
        QueryParser qp2 = new QueryParser(" jmbag>\"0123456789\" \t");
        QueryParser qp3 = new QueryParser(" firstName=\"name\" \t");

        assertTrue(qp1.isDirectQuery());
        assertFalse(qp2.isDirectQuery());
        assertFalse(qp3.isDirectQuery());
    }

    @Test
    public void testGetQueriedJMBAG(){
        QueryParser qp1 = new QueryParser(" jmbag=\"0123456789\" \t");
        QueryParser qp2 = new QueryParser(" jmbag>\"0123456789\" \t");
        QueryParser qp3 = new QueryParser(" firstName=\"name\" \t");

        assertEquals("0123456789", qp1.getQueriedJMBAG());
        assertThrows(IllegalStateException.class, qp2::getQueriedJMBAG);
        assertThrows(IllegalStateException.class, qp3::getQueriedJMBAG);
    }

    @Test
    public void testGetQuery(){
        QueryParser qp = new QueryParser(
                "\t\nfirstName  <=  \"Value\"  " +
                        "and firstName  <  \"Value\"  " +
                        "and firstName>=  \"Value\"  " +
                        "and firstName>\"Value\"  " +
                        "and firstName =\"Value\"  " +
                        "and firstName !=\"Value\"  " +
                        "and firstName LIKE  \"Value\"  \n");

        List<ConditionalExpression> query = qp.getQuery();

        for(ConditionalExpression expression: query){
            assertEquals(FieldValueGetters.FIRST_NAME, expression.getFieldGetter());
            assertEquals("Value", expression.getStringLiteral());
        }

        assertEquals(ComparisonOperators.LESS_OR_EQUALS, query.get(0).getComparisonOperator());
        assertEquals(ComparisonOperators.LESS, query.get(1).getComparisonOperator());
        assertEquals(ComparisonOperators.GREATER_OR_EQUALS, query.get(2).getComparisonOperator());
        assertEquals(ComparisonOperators.GREATER, query.get(3).getComparisonOperator());
        assertEquals(ComparisonOperators.EQUALS, query.get(4).getComparisonOperator());
        assertEquals(ComparisonOperators.NOT_EQUALS, query.get(5).getComparisonOperator());
        assertEquals(ComparisonOperators.LIKE, query.get(6).getComparisonOperator());
    }

    @Test
    public void testExample1(){
        QueryParser qp = new QueryParser(" jmbag =\"0123456789\" ");

        assertTrue(qp.isDirectQuery());
        assertEquals("0123456789", qp.getQueriedJMBAG());
        assertEquals(1, qp.getQuery().size());
    }

    @Test
    public void testExample2(){
        QueryParser qp = new QueryParser("jmbag=\"0123456789\"\t and lastName>\"J\"");

        assertFalse(qp.isDirectQuery());
        assertThrows(IllegalStateException.class, qp::getQueriedJMBAG);
        assertEquals(2, qp.getQuery().size());
    }

}
