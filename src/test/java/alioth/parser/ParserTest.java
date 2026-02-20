package alioth.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import alioth.command.AddTodoCommand;
import alioth.command.Command;
import alioth.command.ExitCommand;
import alioth.command.ListCommand;
import alioth.exception.AliothException;
import alioth.message.Message;
import alioth.task.TaskList;
import alioth.task.Todo;

/**
 * Tests for the Parser class to ensure command interpretation is accurate.
 */
public class ParserTest {

    /**
     * Tests that a valid task number is correctly parsed from a string.
     */
    @Test
    public void parseTaskNumber_validNumber_success() throws Exception {
        assertEquals(3, Parser.parseTaskNumber("3", "mark"));
        assertEquals(5, Parser.parseTaskNumber("   5   ", "delete"));
    }

    /**
     * Tests that empty arguments for a numbered command throw the correct error.
     */
    @Test
    public void parseTaskNumber_emptyArgs_exceptionThrown() {
        AliothException e = assertThrows(AliothException.class, () ->
                Parser.parseTaskNumber("   ", "mark"));
        assertEquals(Message.INVALID_MARK.getText(), e.getMessage());
    }

    /**
     * Tests that noninteger arguments throw a dramatic princess error.
     */
    @Test
    public void parseTaskNumber_nonInteger_exceptionThrown() {
        AliothException e = assertThrows(AliothException.class, () ->
                Parser.parseTaskNumber("abc", "unmark"));
        assertEquals(Message.INVALID_UNMARK.getText(), e.getMessage());
    }

    /**
     * Tests that task indices are validated against the current TaskList size.
     */
    @Test
    public void parseTaskIndex_outOfBounds_exceptionThrown() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("Test task"));

        assertThrows(AliothException.class, () ->
                Parser.parseTaskIndex(tasks, "2", "delete"));
        assertThrows(AliothException.class, () ->
                Parser.parseTaskIndex(tasks, "0", "mark"));
    }

    /**
     * Tests that basic commands are parsed into their correct Command types.
     */
    @Test
    public void parse_basicCommands_success() throws Exception {
        Command list = Parser.parse("list");
        assertTrue(list instanceof ListCommand);

        Command bye = Parser.parse("bye");
        assertTrue(bye instanceof ExitCommand);

        Command todo = Parser.parse("todo read book");
        assertTrue(todo instanceof AddTodoCommand);
    }

    /**
     * Tests that leading spaces are strictly forbidden in the kingdom.
     */
    @Test
    public void parse_leadingSpaces_exceptionThrown() {
        AliothException e = assertThrows(AliothException.class, () ->
                Parser.parse("  list"));
        assertEquals(Message.LEADING_SPACES.getText(), e.getMessage());
    }

    /**
     * Tests the extraction of command words and arguments from raw input strings.
     */
    @Test
    public void stringExtraction_validInput_success() {
        assertEquals("todo", Parser.getCommandWord("todo read book"));
        assertEquals("read book", Parser.getCommandArgs("todo read book"));
        assertEquals("", Parser.getCommandArgs("list"));
    }

    /**
     * Tests that unknown commands are rejected with an appropriate message.
     */
    @Test
    public void parse_unknownCommand_exceptionThrown() {
        AliothException e = assertThrows(AliothException.class, () ->
                Parser.parse("dance"));
        assertEquals(Message.UNKNOWN_COMMAND.getText(), e.getMessage());
    }
}
