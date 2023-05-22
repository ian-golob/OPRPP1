package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * A node representing a piece of textual data. It inherits from Node class.
 *
 * @author Ian Golob
 */
public class TextNode extends Node {

    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString(){
        return escapeCharacters(text);
    }

    /**
     * Escapes the following characters;
     * @param text Text to escape
     * @return Text with escaped characters.
     */
    private static String escapeCharacters(String text){

        char[] textChars = text.toCharArray();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < textChars.length; i++){

            if(textChars[i] == '\\'){
                builder.append('\\');
            }

            if( textChars[i] == '{'
                    && i+1 < textChars.length
                    && textChars[i+1] == '$'){
                builder.append('\\');
            }

            builder.append(textChars[i]);

        }

        return builder.toString();
    }

}
