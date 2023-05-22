package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

public class CharsetsCommand implements ShellCommand{

    private static final List<String> commandDescription = List.of("Lists the available charsets.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        if(arguments.trim().length() > 0 ){
            env.writeln("Unexpected arguments given.");
            return ShellStatus.CONTINUE;
        }

        Set<String> charsets = Charset.availableCharsets().keySet();

        charsets.forEach(env::writeln);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}
