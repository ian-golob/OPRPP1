package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.util.SortedMap;
import java.util.TreeMap;

public class MyShell {

    public static void main(String[] args) {


        SortedMap<String, ShellCommand> commands = new TreeMap<>();

        commands.put("symbol", new SymbolCommand());
        commands.put("exit", new ExitCommand());
        commands.put("charsets", new CharsetsCommand());
        commands.put("cat", new CatCommand());
        commands.put("ls", new LsCommand());
        commands.put("tree", new TreeCommand());
        commands.put("copy", new CopyCommand());
        commands.put("mkdir", new MkdirCommand());
        commands.put("hexdump", new HexdumpCommand());
        commands.put("help", new HelpCommand());

        Environment env = new ShellEnvironment(commands, System.in, System.out,
                '>', '\\', '|');

        try {

            System.out.println("Welcome to MyShell v 1.0");

            ShellStatus status;
            do {
                String line = env.readLine();

                int commandNameEnd = 0;
                for (; commandNameEnd < line.length(); commandNameEnd++) {
                    if (Character.isWhitespace(line.charAt(commandNameEnd))) {
                        break;
                    }
                }

                String commandName = line.substring(0, commandNameEnd);
                String arguments = line.substring(commandNameEnd).trim();

                ShellCommand command = commands.get(commandName);

                if (command == null) {
                    System.out.println("Unknown command");
                    status = ShellStatus.CONTINUE;
                } else {
                    status = command.executeCommand(env, arguments);
                }

            } while (status != ShellStatus.TERMINATE);

        } catch (ShellIOException ex) {
            env.writeln("An error occurred. Exiting...");
        }
    }

}
