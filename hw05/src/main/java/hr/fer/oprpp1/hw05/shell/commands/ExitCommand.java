package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;

public class ExitCommand implements ShellCommand{

    private static final List<String> commandDescription = List.of("Exits the program.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        if(arguments.trim().length() > 0 ){
            env.writeln("Unexpected arguments given.");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}
