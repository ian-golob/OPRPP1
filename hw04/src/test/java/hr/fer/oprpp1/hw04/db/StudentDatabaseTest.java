package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for StudentDatabase.
 *
 * @author Ian Golob
 */
public class StudentDatabaseTest {

    private static List<String> readDatabaseRows() throws IOException {
        return Files.readAllLines(
                Paths.get("src/test/resources/database.txt"),
                StandardCharsets.UTF_8
        );
    }

    @Test
    public void testStudentDatabaseInvalidInput(){
        assertThrows(IllegalArgumentException.class,
                () -> new StudentDatabase(List.of("")));
        assertThrows(IllegalArgumentException.class,
                () -> new StudentDatabase(List.of("1 Ian Golob -1")));
    }

    @Test
    public void testForJMBAG() throws IOException {
        StudentDatabase db = new StudentDatabase(readDatabaseRows());
        String jmbag = "0000000001";
        String firstName = "Marin";
        String lastName = "Akšamović";
        Integer finalGrade = 2;

        StudentRecord record = db.forJMBAG("0000000001");

        assertEquals(jmbag, record.getJmbag());
        assertEquals(firstName, record.getFirstName());
        assertEquals(lastName, record.getLastName());
        assertEquals(finalGrade, record.getFinalGrade());
    }

    @Test
    public void testForJMBAGInvalidJMBAG() throws IOException {
        StudentDatabase db = new StudentDatabase(readDatabaseRows());

        assertNull(db.forJMBAG("-1"));
    }

    @Test
    public void testFilterReturnAll() throws IOException {
        List<String> databaseRows = readDatabaseRows();
        StudentDatabase db = new StudentDatabase(databaseRows);
        IFilter filter = record -> true;

        List<StudentRecord> filteredRecords = db.filter(filter);

        assertEquals(databaseRows.size(), filteredRecords.size());
    }

    @Test
    public void testFilterReturnNone() throws IOException {
        List<String> databaseRows = readDatabaseRows();
        StudentDatabase db = new StudentDatabase(databaseRows);
        IFilter filter = record -> false;

        List<StudentRecord> filteredRecords = db.filter(filter);

        assertEquals(0, filteredRecords.size());
    }


}
