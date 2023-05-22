package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class CatCommand implements ShellCommand{

    private static final List<String> commandDescription = List.of("Takes one or two arguments.", 
            "The first argument is path to some file and is mandatory.",
            "The second argument is charset name that should be used to interpret chars from bytes.",
            "If not provided, or invalid, a default platform charset will be used.",
            "This command opens the given file and writes its content to console.");

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

        Path path = Path.of(pathName);
        String charsetName = arguments.substring(pathName.length()).trim();

        Charset charset = Charset.defaultCharset();

        if(charsetName.length() > 0){
            try {
                charset = Charset.forName(charsetName);
            } catch (IllegalArgumentException ex){
                env.writeln("Illegal charset name.");
                return ShellStatus.CONTINUE;
            }
        }

        try(BufferedReader input = Files.newBufferedReader(path, charset)){
            Stream<String> lines = input.lines();

            lines.forEach(env::writeln);

        } catch (UncheckedIOException | IOException e) {
            env.writeln("Error reading file with charset " + charset.name());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }

}
