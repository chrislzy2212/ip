package alioth.message;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Message enum to verify error text and mapping logic.
 */
public class MessageTest {

    /**
     * Tests that command words are correctly mapped to their specific invalid index messages.
     */
    @Test
    public void invalidIndexCommand_validCommands_returnsCorrectMessage() {
        assertEquals(Message.INVALID_MARK, Message.invalidIndexCommand("mark"));
        assertEquals(Message.INVALID_UNMARK, Message.invalidIndexCommand("unmark"));
        assertEquals(Message.INVALID_DELETE, Message.invalidIndexCommand("delete"));
    }

    /**
     * Tests that unknown command words fall back to UNKNOWN_COMMAND.
     */
    @Test
    public void invalidIndexCommand_unknownCommand_returnsUnknownMessage() {
        assertEquals(Message.UNKNOWN_COMMAND, Message.invalidIndexCommand("dance"));
    }

    /**
     * Tests that the format method correctly replaces placeholders in the error strings.
     */
    @Test
    public void format_withArguments_returnsFormattedString() {
        String alias = "ls";
        String expected = "Oh! This secret name is already used in the royal records: ls";
        assertEquals(expected, Message.ALIAS_ALREADY_EXISTS.format(alias));
    }

    /**
     * Tests that getText returns the raw, unformatted princess text.
     */
    @Test
    public void getText_returnsRawString() {
        String expected = "Oh dear! A princess loves a tidy room. "
                + "Please do not start your command with any spaces!";
        assertEquals(expected, Message.LEADING_SPACES.getText());
    }
}
