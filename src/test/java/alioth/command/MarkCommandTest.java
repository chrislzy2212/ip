package alioth.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.exception.AliothException;
import alioth.message.Message;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.task.Todo;
import alioth.ui.Ui;

/**
 * Tests for MarkCommand to verify status toggling and persistence.
 */
public class MarkCommandTest {
    private static final Path TEST_PATH = Paths.get("data", "test-mark.txt");
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(TEST_PATH);
    }

    @AfterEach
    public void tearDown() throws Exception {
        Files.deleteIfExists(TEST_PATH);
    }

    /**
     * Tests that a task's status is correctly changed to done.
     */
    @Test
    public void execute_validIndex_success() throws AliothException {
        tasks.add(new Todo("Polish the glass slipper"));
        MarkCommand command = new MarkCommand("1");
        command.execute(tasks, ui, storage);

        assertTrue(tasks.get(0).isDone(), "The task should be marked as done.");
        assertTrue(ui.consumeOutput().contains("[X]"), "The UI should reflect the completion icon.");
    }

    /**
     * Tests that marking an invalid index throws the specific royal error.
     */
    @Test
    public void execute_invalidIndex_exceptionThrown() {
        tasks.add(new Todo("Attend the ball"));
        MarkCommand command = new MarkCommand("2"); // Only 1 task exists

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_MARK.getText(), e.getMessage());
    }

    /**
     * Tests that the command saves the updated status to storage.
     */
    @Test
    public void execute_savesToStorage() throws AliothException {
        tasks.add(new Todo("Be kind"));
        MarkCommand command = new MarkCommand("1");
        command.execute(tasks, ui, storage);

        // This verifies the save method was called without crashing
        assertTrue(Files.exists(TEST_PATH), "Storage should save the updated list.");
    }
}
