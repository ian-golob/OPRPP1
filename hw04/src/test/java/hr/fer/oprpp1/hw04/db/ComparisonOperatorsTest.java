package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static hr.fer.oprpp1.hw04.db.ComparisonOperators.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ComparisonOperators.
 * @author Ian Golob
 */
public class ComparisonOperatorsTest {

    @Test
    public void testLESS(){
        assertTrue(LESS.satisfied("a", "b"));

        assertFalse(LESS.satisfied("a", "a"));

        assertFalse(LESS.satisfied("b", "a"));
    }

    @Test
    public void testLESS_OR_EQUALS(){
        assertTrue(LESS_OR_EQUALS.satisfied("a", "b"));

        assertTrue(LESS_OR_EQUALS.satisfied("a", "a"));

        assertFalse(LESS_OR_EQUALS.satisfied("b", "a"));
    }

    @Test
    public void testGREATER(){
        assertFalse(GREATER.satisfied("a", "b"));

        assertFalse(GREATER.satisfied("a", "a"));

        assertTrue(GREATER.satisfied("b", "a"));
    }

    @Test
    public void testGREATER_OR_EQUALS(){
        assertFalse(GREATER_OR_EQUALS.satisfied("a", "b"));

        assertTrue(GREATER_OR_EQUALS.satisfied("a", "a"));

        assertTrue(GREATER_OR_EQUALS.satisfied("b", "a"));
    }

    @Test
    public void testEQUALS(){
        assertFalse(EQUALS.satisfied("a", "b"));

        assertTrue(EQUALS.satisfied("a", "a"));

        assertFalse(EQUALS.satisfied("b", "a"));
    }

    @Test
    public void testNOT_EQUALS(){
        assertTrue(NOT_EQUALS.satisfied("a", "b"));

        assertFalse(NOT_EQUALS.satisfied("a", "a"));

        assertTrue(NOT_EQUALS.satisfied("b", "a"));
    }


    @Test
    public void testLIKE(){
        assertTrue(LIKE.satisfied("Zagreb", "Zagreb"));
        assertTrue(LIKE.satisfied("AAA", "AA*AA"));
        assertTrue(LIKE.satisfied("AAAA", "AA*AA"));
        assertTrue(LIKE.satisfied("", "*"));
        assertTrue(LIKE.satisfied("AAaAAA", "AAa*"));
        assertTrue(LIKE.satisfied("AAAAaA", "*AaA"));
        assertTrue(LIKE.satisfied("AAAAAA", "*"));

        assertFalse(LIKE.satisfied("Zagreb", "Aba*"));
        assertFalse(LIKE.satisfied("Zagreb", "zagreb"));

        assertThrows(IllegalArgumentException.class, () -> LIKE.satisfied("Zagreb", "**"));
    }
}
