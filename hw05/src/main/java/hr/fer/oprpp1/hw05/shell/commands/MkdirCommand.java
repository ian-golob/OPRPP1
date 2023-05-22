package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MkdirCommand implements ShellCommand{

    private static final List<String> commandDescription =
            List.of("Takes a single argument - directory name, and creates the appropriate directory structure.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        arguments = arguments.trim();

        String pathName;
        try {
            ShellArgumentParser parser = new ShellArgumentParser(arguments);

            pathName = parser.parseNextPathName();

            if(parser.hasNext()){
                env.writeln("Too many arguments given.");
                return ShellStatus.CONTINUE;
            }

        } catch (IllegalArgumentException e){
            env.writeln("Illegal arguments");
            return ShellStatus.CONTINUE;
        }

        Path dir = Path.of(pathName);

        if(Files.isRegularFile(dir)){
            env.writeln("Expected directory name.");
            return ShellStatus.CONTINUE;
        }

        if(Files.exists(dir)){
            env.writeln("The given directory already exists.");
            return ShellStatus.CONTINUE;
        }


        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            env.writeln("Error creating directory.");
            return ShellStatus.CONTINUE;
        }

        env.writeln("Created directory.");

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}
