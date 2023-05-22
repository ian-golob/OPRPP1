package hr.fer.oprpp1.custom.scripting.lexer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testExample1(){
        String input = readExample(1);
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals("Ovo je \nsve jedan text node\n", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertNull(lexer.getToken().getValue());
        assertEquals(TokenType.EOF, lexer.getToken().getType());

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample2(){
        String input = readExample(2);
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals("Ovo je \nsve jedan {$ text node\n", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertNull(lexer.getToken().getValue());
        assertEquals(TokenType.EOF, lexer.getToken().getType());

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample3(){
        String input = readExample(3);
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals("Ovo je \nsve jedan \\{$text node\n", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertNull(lexer.getToken().getValue());
        assertEquals(TokenType.EOF, lexer.getToken().getType());

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample4(){
        String input = readExample(4);
        Lexer lexer = new Lexer(input);

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample5(){
        String input = readExample(5);
        Lexer lexer = new Lexer(input);

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample6(){
        String input = readExample(6);
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals("Ovo je OK ", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(TokenType.START_OF_TAG, lexer.getToken().getType());

        lexer.setState(LexerState.TAG_NAME);

        lexer.nextToken();
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

        lexer.setState(LexerState.TAG);

        lexer.nextToken();
        assertEquals("String ide\nu više redaka\nčak tri", lexer.getToken().getValue());
        assertEquals(TokenType.STRING, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(TokenType.END_OF_TAG, lexer.getToken().getType());

        lexer.setState(LexerState.TEXT);

        lexer.nextToken();
        assertEquals("\n", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertNull(lexer.getToken().getValue());
        assertEquals(TokenType.EOF, lexer.getToken().getType());

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample7(){
        String input = readExample(7);
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals("Ovo je isto OK ", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(TokenType.START_OF_TAG, lexer.getToken().getType());

        lexer.setState(LexerState.TAG_NAME);

        lexer.nextToken();
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

        lexer.setState(LexerState.TAG);

        lexer.nextToken();
        assertEquals("String ide\nu \"više\" \nredaka\novdje a stvarno četiri", lexer.getToken().getValue());
        assertEquals(TokenType.STRING, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(TokenType.END_OF_TAG, lexer.getToken().getType());

        lexer.setState(LexerState.TEXT);

        lexer.nextToken();
        assertEquals("\n", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertNull(lexer.getToken().getValue());
        assertEquals(TokenType.EOF, lexer.getToken().getType());

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample8(){
        String input = readExample(8);
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals("Ovo se ruši ", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(TokenType.START_OF_TAG, lexer.getToken().getType());

        lexer.setState(LexerState.TAG_NAME);

        lexer.nextToken();
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

        lexer.setState(LexerState.TAG);

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testExample9(){
        String input = readExample(9);
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals("Ovo se ruši ", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(TokenType.START_OF_TAG, lexer.getToken().getType());

        lexer.setState(LexerState.TAG_NAME);

        lexer.nextToken();
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

        lexer.setState(LexerState.TAG);

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testTextState(){
        String input = " This is a test \n\t \\{$ $} ${}$"
                        + "{$"
                        + "{";
        Lexer lexer = new Lexer(input);

        lexer.nextToken();
        assertEquals(" This is a test \n\t {$ $} ${}$", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(TokenType.START_OF_TAG, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("{", lexer.getToken().getValue());
        assertEquals(TokenType.TEXT, lexer.getToken().getType());

        lexer.nextToken();
        assertNull(lexer.getToken().getValue());
        assertEquals(TokenType.EOF, lexer.getToken().getType());

        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testTagNameState(){
        String input = "="
                        + "\n\t ="
                        + "For "
                        + "   \t \n fOR";

        Lexer lexer = new Lexer(input);
        lexer.setState(LexerState.TAG_NAME);

        lexer.nextToken();
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("For", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("fOR", lexer.getToken().getValue());
        assertEquals(TokenType.TAG_NAME, lexer.getToken().getType());

    }

    @Test
    public void testTagState(){
        String input =
                "@sin_123"
                + "\n\t \"String with escapes \\\\ \\\" \\n \\r \\t !\""
                + "-1"
                + " - "
                + "^"
                + "123 "
                + " 123.12 "
                + " -123.34"
                + "$} ";

        Lexer lexer = new Lexer(input);
        lexer.setState(LexerState.TAG);

        lexer.nextToken();
        assertEquals("@sin_123", lexer.getToken().getValue());
        assertEquals(TokenType.FUNCTION, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("String with escapes \\ \" \n \r \t !", lexer.getToken().getValue());
        assertEquals(TokenType.STRING, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("-1", lexer.getToken().getValue());
        assertEquals(TokenType.INTEGER, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("-", lexer.getToken().getValue());
        assertEquals(TokenType.OPERATOR, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("^", lexer.getToken().getValue());
        assertEquals(TokenType.OPERATOR, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("123", lexer.getToken().getValue());
        assertEquals(TokenType.INTEGER, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("123.12", lexer.getToken().getValue());
        assertEquals(TokenType.DOUBLE, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("-123.34", lexer.getToken().getValue());
        assertEquals(TokenType.DOUBLE, lexer.getToken().getType());

        lexer.nextToken();
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(TokenType.END_OF_TAG, lexer.getToken().getType());

    }

    private String readExample(int n) {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
            if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
            byte[] data = is.readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch(IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }

}
