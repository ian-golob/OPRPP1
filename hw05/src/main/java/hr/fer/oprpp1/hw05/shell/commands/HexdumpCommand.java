package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellArgumentParser;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HexdumpCommand implements ShellCommand{

    private static final int LINE_BYTE_LENGTH = 16;

    private static final List<String> commandDescription =
            List.of("Takes one argument - file path - and outputs the hex representation of the given file.");

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String pathName;
        try {
            ShellArgumentParser parser = new ShellArgumentParser(arguments.trim());

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

        if(!Files.isRegularFile(dir)) {
            env.writeln("Illegal file path given.");
            return ShellStatus.CONTINUE;
        }

        try(BufferedInputStream input = new BufferedInputStream(Files.newInputStream(dir))){

            byte[] bytes = new byte[LINE_BYTE_LENGTH];
            int read = input.readNBytes(bytes, 0, LINE_BYTE_LENGTH);
            int currentByteOffset = 0;

            while(read > 0) {

                env.writeln(formatOutputLine(currentByteOffset, bytes, read));

                read = input.readNBytes(bytes, 0, LINE_BYTE_LENGTH);
                currentByteOffset += LINE_BYTE_LENGTH;
            }

        } catch (IOException e) {
            System.out.println("Error reading from given file.");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    private static String formatOutputLine(int currentByteOffset, byte[] bytes, int readBytes) {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%08x:", currentByteOffset));

        for(int i = 0; i < LINE_BYTE_LENGTH; i++){
            if(i == LINE_BYTE_LENGTH/2){
                sb.append('|');
            }else{
                sb.append(' ');
            }

            if(i < readBytes){
                sb.append(byteToHex(bytes[i]));
            }else{
                sb.append("  ");
            }
        }

        sb.append(" | ");

        for(int i = 0; i < LINE_BYTE_LENGTH; i++){
            if(i < readBytes){
                sb.append(byteToChar(bytes[i]));
            }else{
                sb.append("  ");
            }
        }


        return sb.toString();
    }

    private static char byteToChar(byte b) {
        if(b < 32){
            return '.';
        } else {
            return (char) b;
        }
    }

    private static String byteToHex(byte aByte) {
        return String.format("%02x", aByte);
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}
