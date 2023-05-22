package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;

public interface ShellCommand {

    ShellStatus executeCommand(Environment env, String arguments);
    String getCommandName();
    List<String> getCommandDescription();


}
