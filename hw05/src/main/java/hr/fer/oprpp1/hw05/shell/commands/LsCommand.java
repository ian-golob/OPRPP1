package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LsCommand implements ShellCommand {

    private static final List<String> commandDescription =
            List.of("Takes a single argument – directory – and writes a directory listing.");

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
            Files.list(dir).forEach(path -> env.writeln(formatOutput(path)));
        } catch (UncheckedIOException | IOException e) {
            env.writeln("Error listing files in the specified directory.");
        }

        return ShellStatus.CONTINUE;
    }

    private static String formatOutput(Path path){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        BasicFileAttributeView faView = Files.getFileAttributeView(
                path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );

        BasicFileAttributes attributes = null;
        try {
            attributes = faView.readAttributes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        FileTime fileTime = attributes.creationTime();
        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

        return String.format("%c%c%c%c %10d %s %s",
                Files.isDirectory(path) ? 'd' : '-',
                Files.isReadable(path) ? 'r' : '-',
                Files.isWritable(path) ? 'w' : '-',
                Files.isExecutable(path) ? 'x' : '-',
                attributes.size(),
                formattedDateTime,
                path.getFileName().toString());
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}
