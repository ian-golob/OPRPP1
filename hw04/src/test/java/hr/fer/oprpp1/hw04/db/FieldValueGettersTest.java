package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static hr.fer.oprpp1.hw04.db.FieldValueGetters.*;

/**
 * Test class for FieldValueGetters.
 * @author Ian Golob
 */
public class FieldValueGettersTest {

    @Test
    public void testFIRST_NAME(){
        StudentRecord record = new StudentRecord("0036533922", "Ian", "Golob", 5);

        assertEquals("Ian", FIRST_NAME.get(record));
    }

    @Test
    public void testLAST_NAME(){
        StudentRecord record = new StudentRecord("0036533922", "Ian", "Golob", 5);

        assertEquals("Golob", LAST_NAME.get(record));
    }

    @Test
    public void testJMBAG(){
        StudentRecord record = new StudentRecord("0036533922", "Ian", "Golob", 5);

        assertEquals("0036533922", JMBAG.get(record));
    }

}
