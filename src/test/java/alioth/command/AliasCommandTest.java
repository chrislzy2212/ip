package alioth.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.exception.AliothException;
import alioth.message.Message;
import alioth.parser.Parser;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Tests for AliasCommand to verify shortcut creation and validation logic.
 */
public class AliasCommandTest {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(Paths.get("data", "test-aliases.txt"));
        Parser.setStorage(storage);
    }

    /**
     * Tests that a valid alias is correctly added to the Parser.
     */
    @Test
    public void execute_validAlias_success() throws AliothException {
        AliasCommand command = new AliasCommand("ls list");
        command.execute(tasks, ui, storage);

        assertTrue(Parser.getAliases().containsKey("ls"));
        assertEquals("list", Parser.getAliases().get("ls"));
    }

    /**
     * Tests that an alias targeting a non-existent command is rejected.
     */
    @Test
    public void execute_unknownTarget_exceptionThrown() {
        AliasCommand command = new AliasCommand("dance jump");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertTrue(e.getMessage().contains("I don't know the command for"));
    }

    /**
     * Tests that shadowing a reserved royal word is forbidden.
     */
    @Test
    public void execute_reservedWordAlias_exceptionThrown() {
        AliasCommand command = new AliasCommand("todo list");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.ALIAS_IS_COMMAND_WORD.format("todo"), e.getMessage());
    }

    /**
     * Tests that incomplete arguments trigger the princess-themed error.
     */
    @Test
    public void execute_incompleteArgs_exceptionThrown() {
        AliasCommand command = new AliasCommand("ls");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_ALIAS.getText(), e.getMessage());
    }
}
