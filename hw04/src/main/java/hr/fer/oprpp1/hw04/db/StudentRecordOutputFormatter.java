package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that formats student records.
 */
public class StudentRecordOutputFormatter {

    /**
     * Returns a formatted student record output.
     * List elements represent output rows.
     * @param records Records to format.
     * @return A formatted student record output.
     */
    public static List<String> format(List<StudentRecord> records) {
        List<String> output = new ArrayList<>();

        if(records.size() > 0){
            boolean containsNonNull = false;
            int maxJmbagLength = 0;
            int maxLastNameLength = 0;
            int maxFirstNameLength = 0;

            for(StudentRecord record: records){
                if(record == null){
                    continue;
                }
                containsNonNull = true;

                maxJmbagLength = Math.max(
                        maxJmbagLength,
                        record.getJmbag().length());

                maxLastNameLength = Math.max(
                        maxLastNameLength,
                        record.getLastName().length());

                maxFirstNameLength = Math.max(
                        maxFirstNameLength,
                        record.getFirstName().length());

            }

            if(containsNonNull){
                output.add(generateEdgeRow(
                        maxJmbagLength + 2,
                        maxFirstNameLength + 2,
                        maxLastNameLength + 2));

                for(StudentRecord record: records){
                    if(record == null){
                        continue;
                    }

                    StringBuilder rowBuilder = new StringBuilder();

                    rowBuilder.append("| ");

                    rowBuilder.append(padWithSpace(
                            record.getJmbag(),
                            maxJmbagLength
                    ));

                    rowBuilder.append(" | ");

                    rowBuilder.append(padWithSpace(
                            record.getLastName(),
                            maxLastNameLength
                    ));

                    rowBuilder.append(" | ");

                    rowBuilder.append(padWithSpace(
                            record.getFirstName(),
                            maxFirstNameLength
                    ));

                    rowBuilder.append(" | ");

                    rowBuilder.append(record.getFinalGrade());

                    rowBuilder.append(" |");

                    output.add(rowBuilder.toString());
                }

                output.add(generateEdgeRow(
                        maxJmbagLength + 2,
                        maxFirstNameLength + 2,
                        maxLastNameLength + 2));

            }
        }


        output.add("Records selected: " + records.stream().filter(Objects::nonNull).count());

        return output;
    }

    /**
     * Generates the formatted row edge of the output (the first or last row).
     * @param jmbagColumnLength The jmbag column length.
     * @param firstNameColumnLength The first name column length.
     * @param lastNameColumnLength The last name column length.
     * @return the formatted row edge of the output (the first or last row).
     */
    public static String generateEdgeRow(int jmbagColumnLength,
                                         int firstNameColumnLength,
                                         int lastNameColumnLength){

        StringBuilder rowBuilder = new StringBuilder();

        rowBuilder.append('+');
        for(int i = 0; i < jmbagColumnLength; i++) rowBuilder.append('=');
        rowBuilder.append('+');
        for(int i = 0; i < lastNameColumnLength; i++) rowBuilder.append('=');
        rowBuilder.append('+');
        for(int i = 0; i < firstNameColumnLength; i++) rowBuilder.append('=');
        rowBuilder.append('+');
        for(int i = 0; i < 3; i++) rowBuilder.append('=');
        rowBuilder.append('+');

        return rowBuilder.toString();
    }

    /**
     * Appends spaces (' ') to the given string until it has the given length.
     * @param string String to pad.
     * @param length Length to pad to.
     * @return A new padded string of the given length.
     */
    public static String padWithSpace(String string, int length){
        return String.format("%-" + length + "s", string);
    }
}
