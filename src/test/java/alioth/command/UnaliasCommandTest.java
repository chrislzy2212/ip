package alioth.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
 * Tests for UnaliasCommand to verify shortcut removal and error handling.
 */
public class UnaliasCommandTest {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() throws AliothException {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(Paths.get("data", "test-unalias.txt"));
        Parser.setStorage(storage);

        // Ensure the parser has a known alias to remove
        if (!Parser.getAliases().containsKey("ls")) {
            Parser.addAlias("ls", "list");
        }
    }

    /**
     * Tests that an existing alias is successfully removed.
     */
    @Test
    public void execute_validAlias_success() throws AliothException {
        UnaliasCommand command = new UnaliasCommand("ls");
        command.execute(tasks, ui, storage);

        assertFalse(Parser.getAliases().containsKey("ls"), "Alias should be removed from the Parser.");
        assertTrue(ui.consumeOutput().contains("no longer a secret name"), "UI should confirm removal.");
    }

    /**
     * Tests that trying to remove a non-existent alias throws the correct royal error.
     */
    @Test
    public void execute_nonExistentAlias_exceptionThrown() {
        UnaliasCommand command = new UnaliasCommand("magicWord");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.NO_SUCH_ALIAS.format("magicWord"), e.getMessage());
    }

    /**
     * Tests that empty arguments are rejected.
     */
    @Test
    public void execute_emptyArgs_exceptionThrown() {
        UnaliasCommand command = new UnaliasCommand("   ");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_UNALIAS.getText(), e.getMessage());
    }
}
