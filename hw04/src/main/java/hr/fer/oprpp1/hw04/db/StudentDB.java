package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * A demo class for the StudentDatabase class and other related classes.
 *
 * @author Ian Golob
 */
public class StudentDB {

    public static void main(String[] args) {

        List<String> rows = null;
        try {
            rows = Files.readAllLines(
                    Paths.get("database.txt"),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.out.println("Error reading file \"database.txt\": the file does not exist or it is malformed.");
            exit(1);
        }

        StudentDatabase db = null;
        try {
             db = new StudentDatabase(rows);
        } catch (IllegalArgumentException ex){
            System.out.println("An exception occurred while parsing the input: " + ex.getMessage());
            exit(1);
        }

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        while(true){

            out.print("> ");
            String command = sc.next();

            switch(command){

                case "query":
                    String queryString = sc.nextLine().trim();

                    try {
                        QueryParser parser = new QueryParser(queryString);

                        List<StudentRecord> records = new ArrayList<>();

                        if(parser.isDirectQuery()){
                            records.add(db.forJMBAG(parser.getQueriedJMBAG()));

                        }else{
                            records.addAll(db.filter(new QueryFilter(parser.getQuery())));
                        }

                        List<String> output = StudentRecordOutputFormatter.format(records);

                        output.forEach(out::println);

                    } catch (WildcardException exception){
                        out.println("Invalid query, a string literal used with the LIKE operator " +
                                "can not contain more than one wildcard character (*).");
                    } catch (IllegalArgumentException exception){
                        out.println("Invalid query.");
                    }

                    break;

                case "exit":
                    out.println("Goodbye!");
                    exit(1);

                default:
                    out.println("Unknown command.");

            }
        }
    }

}
