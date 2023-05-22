package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;

public class SymbolCommand implements ShellCommand{

    private static final List<String> commandDescription = List.of("Accepts two arguments:",
            "symbol name,",
            "(optional) character to change the given shell symbol to.",
            "Valid symbol names are: PROMPT, MULTILINE, MORELINE.",
            "Returns the current symbol or changes the symbol to the given symbol.",
            "If no arguments are given, returns this command description.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        if(arguments.trim().length() == 0){
            env.writeln("No arguments given.");
            return ShellStatus.CONTINUE;
        }

        String symbolName;
        char newSymbol = 'x';
        boolean changeSymbol = false;
        try {
            ShellArgumentParser parser = new ShellArgumentParser(arguments);

            symbolName = parser.parseNextArgument();

            if(parser.hasNext()){
                changeSymbol = true;
                String symbolString = parser.parseNextArgument();

                if(symbolString.length() > 1){
                    throw new IllegalArgumentException();
                }

                newSymbol = symbolString.charAt(0);
            }

            if(parser.hasNext()){
                env.writeln("Too many arguments given.");
                return ShellStatus.CONTINUE;
            }

        } catch (IllegalArgumentException e){
            env.writeln("Illegal arguments");
            return ShellStatus.CONTINUE;
        }

        if(changeSymbol){

            Character oldSymbol;
            switch (symbolName) {
                case "PROMPT" -> {
                    oldSymbol = env.getPromptSymbol();
                    env.setPromptSymbol(newSymbol);
                }
                case "MULTILINE" -> {
                    oldSymbol = env.getMultilineSymbol();
                    env.setMultilineSymbol(newSymbol);
                }
                case "MORELINE" -> {
                    oldSymbol = env.getMorelinesSymbol();
                    env.setMorelinesSymbol(newSymbol);
                }
                default -> {
                    env.writeln("Unknown symbol type.");
                    return ShellStatus.CONTINUE;
                }
            }

            env.writeln("Symbol for " + symbolName + " changed from '" + oldSymbol +
                    "' to '" + newSymbol + "'");

        }else{

            Character symbol;
            switch (symbolName) {
                case "PROMPT" -> symbol = env.getPromptSymbol();
                case "MULTILINE" -> symbol = env.getMultilineSymbol();
                case "MORELINE" -> symbol = env.getMorelinesSymbol();
                default -> {
                    env.writeln("Unknown symbol type.");
                    return ShellStatus.CONTINUE;
                }
            }

            env.writeln("Symbol for " + symbolName + " is " + symbol);

        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}
