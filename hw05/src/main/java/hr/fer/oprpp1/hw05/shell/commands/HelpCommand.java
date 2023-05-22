package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;

public class HelpCommand implements ShellCommand {

    private static final List<String> commandDescription =
            List.of("Lists the available shell commands.",
                    "Takes one optional argument - command name.",
                    "If the optional argument is specified, outputs the given command details.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String commandName = null;
        boolean commandSpecified = false;
        try {
            ShellArgumentParser parser = new ShellArgumentParser(arguments.trim());

            if(parser.hasNext()){
                commandSpecified = true;
                commandName = parser.parseNextArgument();
            }

            if(parser.hasNext()){
                env.writeln("Too many arguments given.");
                return ShellStatus.CONTINUE;
            }

        } catch (IllegalArgumentException e){
            env.writeln("Illegal arguments");
            return ShellStatus.CONTINUE;
        }

        if(commandSpecified){
            ShellCommand command = env.commands().get(commandName);

            if(command == null){
                env.writeln("Unknown command.");
            } else {
                command.getCommandDescription().forEach(env::writeln);
            }

        } else {

            env.writeln("Available commands:");
            env.commands().keySet().forEach(env::writeln);

        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }

}
