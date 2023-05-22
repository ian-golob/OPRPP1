package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits Element and has single read-only String property: value.
 * Represents a string expression.
 *
 * @author Ian Golob
 */
public class ElementString extends Element {

    private final String value;

    public ElementString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String asText() {
        return '\"' + escapeCharacters(value) + '\"';
    }

    /**
     * Escapes the following characters:
     * '\', '"', '\n', '\r', '\t'.
     * @param text Text to escape
     * @return Text with escaped characters.
     */
    private static String escapeCharacters(String text){

        char[] textChars = text.toCharArray();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < textChars.length; i++){

            switch (textChars[i]){
                case '\\':
                    builder.append("\\\\");
                    break;
                case '\"':
                    builder.append("\\\"");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                default:
                    builder.append(textChars[i]);
                    break;
            }

        }

        return builder.toString();
    }

}
