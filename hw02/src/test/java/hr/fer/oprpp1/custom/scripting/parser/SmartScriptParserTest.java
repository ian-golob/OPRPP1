package hr.fer.oprpp1.custom.scripting.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmartScriptParserTest {

    @Test
    public void testValidForExample1(){
        String input = "{$ FOR i -1 10 1 $}\n{$\nEND $}";

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
        assertEquals("{$FOR i -1 10 1 $}\n{$END$}", parser1.getDocumentNode().toString());
    }

    @Test
    public void testValidForExample2(){
        String input = "{$ FOR sco_re \"-1\"10 \"1\" $}\n next line \n{$\nEND $}";

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    @Test
    public void testValidForExample3(){
        String input = "{$ FOR year 1 last_year $}\n  {$ END $}";

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    @Test
    public void testInvalidForExample1(){
        String input = "{$ FOR 3 1 10 1 $} {$END$}";

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testInvalidForExample2(){
        String input = "{$ FOR * \"1\" -10 \"1\" $} {$END$}";

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testInvalidForExample3(){
        String input = "{$ FOR year @sin 10 $} {$END$} ";

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }
    @Test
    public void testInvalidForExample4(){
        String input = "{$ FOR year 1 10 \"1\" \"10\" $} {$END$}";

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testInvalidForExample5(){
        String input = "{$ FOR year $} {$END$}";

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testInvalidForExample6(){
        String input = "{$ FOR year 1 10 1 3 $}  {$END$}";

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }


    @Test
    public void testExample1(){
        String input = readExample(1);

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    @Test
    public void testExample2(){
        String input = readExample(2);

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    @Test
    public void testExample3(){
        String input = readExample(3);

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    @Test
    public void testExample4(){
        String input = readExample(4);

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testExample5(){
        String input = readExample(5);

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testExample6(){
        String input = readExample(6);

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    @Test
    public void testExample7(){
        String input = readExample(7);

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    @Test
    public void testExample8(){
        String input = readExample(8);

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testExample9(){
        String input = readExample(9);

        assertThrows(SmartScriptParserException.class,() -> new SmartScriptParser(input));
    }

    @Test
    public void testExample10(){
        String input = readExample(10);

        SmartScriptParser parser1 = new SmartScriptParser(input);
        SmartScriptParser parser2 = new SmartScriptParser(parser1.getDocumentNode().toString());

        assertEquals(parser1.getDocumentNode().toString(), parser2.getDocumentNode().toString());
        assertEquals("This is sample text.\\\\ \\{$\n" +
                "{$FOR i 1 10 1 $}\n" +
                " This is {$= i $}-th time this message is generated.\n" +
                "{$END$}\n" +
                "{$FOR i 0 10 $}\n" +
                " sin({$= i $}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n" +
                "{$END$}\n" +
                "{$= @mod_1 -1.47 \"\\\\\\\"\\n\\r\\t string\" $}\n",
                parser1.getDocumentNode().toString().replaceAll("\\r\\n", "\n"));
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
