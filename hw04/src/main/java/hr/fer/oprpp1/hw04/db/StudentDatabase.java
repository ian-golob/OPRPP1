package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Represents a database of students (StudentRecords).
 *
 * @author Ian Golob
 */
public class StudentDatabase {

    private final List<StudentRecord> records = new ArrayList<>();

    private final Map<String, StudentRecord> jmbagIndex = new HashMap<>();

    /**
     * Creates a database with the given data.
     * The given data has to be divided into rows
     * and the columns have be divided with the \t character.
     * Rows have to contain 4 columns:
     * JMBAG - string,
     * firstName - string,
     * lastName - string
     * and finalGrade - integer from 1 to 5.
     * @param rows String list of rows with student data.
     * @throws IllegalArgumentException If the rows do not conform with the rules.
     */
    public StudentDatabase(List<String> rows){

        for(String row: rows){
            String[] columns = row.split("\t");

            if(columns.length != 4) {
                throw new IllegalArgumentException("A row has more than 4 columns.");
            }

            String jmbag = columns[0];
            String firstName = columns[2];
            String lastName = columns[1];
            Integer finalGrade = Integer.parseInt(columns[3]);

            if(finalGrade < 1 || finalGrade > 5){
                throw new IllegalArgumentException("The final grade has to be a number between 1 and 5 (including).");
            }

            StudentRecord record = new StudentRecord(jmbag, firstName, lastName, finalGrade);

            if(jmbagIndex.containsKey(jmbag)){
                throw new IllegalArgumentException("A student with the given jmbag already exists.");
            }

            jmbagIndex.put(jmbag, record);
            records.add(record);
        }

    }

    /**
     * Returns the student record associated with the given JMBAG or null if there is no such record.
     * Returns the record in O(1) complexity.
     * @param jmbag The jmbag to search for.
     * @return The student record associated with the given JMBAG or null if there is no such record.
     */
    public StudentRecord forJMBAG(String jmbag){
        return jmbagIndex.get(jmbag);
    }

    /**
     * Returns a list of all the student records that return true when given to the filter.
     * @param filter Student record filter.
     * @return A list of all the student records that return true when given to the filter.
     */
    public List<StudentRecord> filter(IFilter filter){
        List<StudentRecord> returnList = new ArrayList<>();

        for(StudentRecord record: records){
            if(filter.accepts(record)){
                returnList.add(record);
            }
        }

        return returnList;
    }

}
