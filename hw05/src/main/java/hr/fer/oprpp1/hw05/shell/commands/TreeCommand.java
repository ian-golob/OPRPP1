package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TreeCommand implements ShellCommand {
    private static final List<String> commandDescription =
            List.of("Takes a single argument - directory name - and prints a tree.");

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

        if(!Files.isDirectory(dir)) {
            env.writeln("Illegal directory path given.");
            return ShellStatus.CONTINUE;
        }

        try {

            Files.walkFileTree(dir, new TreeFileVisitor(env));

        } catch (UncheckedIOException | IOException e) {
            env.writeln("Error printing files in the specified directory.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}
