package alioth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {

    @Test
    public void parseTaskNumber_validNumber_success() throws Exception {
        assertEquals(3, Parser.parseTaskNumber("3", "mark"));
        assertEquals(3, Parser.parseTaskNumber("   3   ", "delete"));
        assertEquals(0, Parser.parseTaskNumber("0", "unmark")); // allowed by your method (range checked elsewhere)
        assertEquals(-2, Parser.parseTaskNumber("-2", "mark")); // allowed by your method (range checked elsewhere)
    }

    @Test
    public void parseTaskNumber_emptyArgs_exceptionThrown() {
        try {
            Parser.parseTaskNumber("   ", "mark");
            fail(); // should not reach here
        } catch (Exception e) {
            assertEquals(Message.invalidIndexCommand("mark").getText(), e.getMessage());
        }
    }

    @Test
    public void parseTaskNumber_nonInteger_exceptionThrown() {
        try {
            Parser.parseTaskNumber("abc", "delete");
            fail(); // should not reach here
        } catch (Exception e) {
            assertEquals(Message.invalidIndexCommand("delete").getText(), e.getMessage());
        }
    }
}
