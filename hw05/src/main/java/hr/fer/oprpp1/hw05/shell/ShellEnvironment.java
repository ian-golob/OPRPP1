package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.ShellCommand;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;

public class ShellEnvironment implements Environment{

    private final SortedMap<String, ShellCommand> commands;

    private Character promptSymbol;

    private Character moreLinesSymbol;

    private Character multilineSymbol;

    private Scanner input;

    private PrintStream output;

    public ShellEnvironment(SortedMap<String, ShellCommand> commands, InputStream input,
                            PrintStream output,Character promptSymbol, Character moreLinesSymbol,
                            Character multilineSymbol){
        this.commands = Collections.unmodifiableSortedMap(commands);
        this.input = new Scanner(input);
        this.output = output;
        this.promptSymbol = Objects.requireNonNull(promptSymbol);
        this.moreLinesSymbol = Objects.requireNonNull(moreLinesSymbol);
        this.multilineSymbol = Objects.requireNonNull(multilineSymbol);
    }

    @Override
    public String readLine() throws ShellIOException {
        try{

            System.out.print(promptSymbol + " ");

            StringBuilder inputBuilder = new StringBuilder();

            boolean hasNext = false;
            do{
                String inputLine = input.nextLine();

                if(inputLine.charAt(inputLine.length()-1) == moreLinesSymbol){
                    hasNext = true;
                    inputBuilder.append(inputLine.substring(0, inputLine.length()-1).trim());
                    System.out.print(multilineSymbol + " ");
                } else {
                    hasNext = false;
                    inputBuilder.append(inputLine.trim());
                }

            } while (hasNext);

            return inputBuilder.toString();
        } catch (RuntimeException ex){
            throw new ShellIOException();
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        try{
            output.print(text);
        } catch (RuntimeException ex){
            throw new ShellIOException();
        }
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        try{
            output.println(text);
        } catch (RuntimeException ex){
            throw new ShellIOException();
        }
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return commands;
    }

    @Override
    public Character getMultilineSymbol() {
        return multilineSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        multilineSymbol = Objects.requireNonNull(symbol);
    }

    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        promptSymbol = Objects.requireNonNull(symbol);
    }

    @Override
    public Character getMorelinesSymbol() {
        return moreLinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        moreLinesSymbol = Objects.requireNonNull(symbol);
    }
}
