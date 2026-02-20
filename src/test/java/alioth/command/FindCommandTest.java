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
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.task.Todo;
import alioth.ui.Ui;

/**
 * Tests for FindCommand to verify keyword filtering and input validation.
 */
public class FindCommandTest {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(Paths.get("data", "test-find.txt"));

        tasks.add(new Todo("royal ball"));
        tasks.add(new Todo("garden walk"));
        tasks.add(new Todo("polish crown"));
    }

    /**
     * Tests that the command correctly filters tasks by keyword.
     */
    @Test
    public void execute_validKeyword_success() throws AliothException {
        FindCommand command = new FindCommand("royal");
        command.execute(tasks, ui, storage);

        String output = ui.consumeOutput();
        assertTrue(output.contains("royal ball"));
        assertFalse(output.contains("garden walk"));
    }

    /**
     * Tests that finding with an empty keyword throws a princess exception.
     */
    @Test
    public void execute_emptyKeyword_exceptionThrown() {
        FindCommand command = new FindCommand("   ");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_FIND.getText(), e.getMessage());
    }

    /**
     * Tests that multiple matches are all included in the output.
     */
    @Test
    public void execute_multipleMatches_success() throws AliothException {
        tasks.add(new Todo("royal dinner"));
        FindCommand command = new FindCommand("royal");
        command.execute(tasks, ui, storage);

        String output = ui.consumeOutput();
        assertTrue(output.contains("royal ball"));
        assertTrue(output.contains("royal dinner"));
    }
}
