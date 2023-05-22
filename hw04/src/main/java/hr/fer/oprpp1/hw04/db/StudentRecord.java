package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * A representation of a student record.
 *
 * @author Ian Golob
 */
public class StudentRecord {

    private final String jmbag;

    private String firstName;

    private String lastName;

    private Integer finalGrade;

    /**
     * Creates a student record with the given attributes.
     * @param jmbag The student's JMBAG.
     * @param firstName The student's first name.
     * @param lastName The student's last name.
     * @param finalGrade The student's final grade.
     */
    public StudentRecord(String jmbag, String firstName, String lastName, Integer finalGrade) {
        this.jmbag = jmbag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.finalGrade = finalGrade;
    }

    public String getJmbag() {
        return jmbag;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getFinalGrade() {
        return finalGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(jmbag, that.jmbag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }

}
