package hr.fer.oprpp1.hw05.shell;

import java.util.NoSuchElementException;

public class ShellArgumentParser {

    private final String input;

    private int index = 0;


    public ShellArgumentParser(String input) {
        this.input = input;
    }

    public boolean hasNext(){
        skipWhitespace();

        return index < input.length();
    }

    public String parseNextPathName(){
        if(!hasNext()){
            throw new NoSuchElementException();
        }

        StringBuilder sb = new StringBuilder();

        boolean quoted = false;
        if(input.charAt(index) == '\"'){
            quoted = true;
            index++;
        }

        while(index < input.length()) {

            if(quoted){

                if(input.charAt(index) == '\"'){
                    index++;
                    break;
                }

                if(input.charAt(index) == '\\' &&
                        index + 1 < input.length() &&
                        (input.charAt(index + 1) == '\"' ||
                                input.charAt(index + 1) == '\\')){
                    sb.append(input.charAt(index+1));
                    index+=2;
                    continue;
                }

            } else {
                if(Character.isWhitespace(input.charAt(index))){
                    break;
                }
            }

            sb.append(input.charAt(index));
            index++;
        }

        if(quoted && input.charAt(index-1) != '\"'){
            throw new IllegalArgumentException();
        }

        return sb.toString();
    }

    public String parseNextArgument(){
        if(!hasNext()){
            throw new NoSuchElementException();
        }

        skipWhitespace();

        StringBuilder sb = new StringBuilder();

        while(index < input.length()) {
            if(Character.isWhitespace(input.charAt(index))){
                break;
            }

            sb.append(input.charAt(index));
            index++;
        }

        return sb.toString();
    }

    private void skipWhitespace() {

        while(index < input.length() && Character.isWhitespace(input.charAt(index))){
            index++;
        }

    }


}
