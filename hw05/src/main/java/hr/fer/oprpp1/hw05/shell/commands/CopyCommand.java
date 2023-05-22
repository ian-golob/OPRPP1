package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

public class CopyCommand implements ShellCommand{

    private static final List<String> commandDescription = List.of(
            "Accepts two arguments: source file name and destination file name (i.e. paths and names).",
            "If destination file exists, the user is asked if the destination file can be overwritten.",
            "The command works only with files (not directories).",
            "If the second argument is directory, the file is copied into the given directory with the original file name.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        arguments = arguments.trim();

        String srcPathString;
        String destPathString;
        try {
            ShellArgumentParser parser = new ShellArgumentParser(arguments);

            srcPathString = parser.parseNextPathName();
            destPathString = parser.parseNextPathName();

            if(parser.hasNext()){
                env.writeln("Too many arguments given.");
                return ShellStatus.CONTINUE;
            }

        } catch (IllegalArgumentException e){
            env.writeln("Illegal arguments");
            return ShellStatus.CONTINUE;
        } catch (NoSuchElementException ex){
            env.writeln("Not enough arguments given.");
            return ShellStatus.CONTINUE;
        }

        Path srcPath = Path.of(srcPathString);
        Path destPath = Path.of(destPathString);

        if(Files.isDirectory(destPath)){
            destPath = destPath.resolve(srcPath.getFileName().toString());
        }

        if(Files.exists(destPath)){
            env.writeln("File already exists, do you want to overwrite the current file? (Y/N)");

            String line = env.readLine();

            if(line.trim().equals("Y")){

            } else if(line.trim().equals("N")){
                env.writeln("File not copied.");
                return ShellStatus.CONTINUE;
            } else {
                env.writeln("Unknown response, file not copied.");
                return ShellStatus.CONTINUE;
            }
        }

        try(BufferedInputStream input = new BufferedInputStream(Files.newInputStream(srcPath));
            BufferedOutputStream output =new BufferedOutputStream(Files.newOutputStream(destPath))){

            int in = input.read();
            while (in != -1){
                output.write(in);
                in = input.read();
            }

            env.writeln("File copied.");
        } catch (UncheckedIOException | IOException e) {
            env.writeln("Error copying file.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }

}
