package alioth.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
 * Tests for UnmarkCommand to verify status reversion and persistence.
 */
public class UnmarkCommandTest {
    private static final Path TEST_PATH = Paths.get("data", "test-unmark.txt");
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
     * Tests that a completed task's status is correctly reverted to not done.
     */
    @Test
    public void execute_validIndex_success() throws AliothException {
        Todo todo = new Todo("Attend the tea party");
        todo.setDone(true);
        tasks.add(todo);

        UnmarkCommand command = new UnmarkCommand("1");
        command.execute(tasks, ui, storage);

        assertFalse(tasks.get(0).isDone(), "The task should be unmarked (isDone = false).");
        assertTrue(ui.consumeOutput().contains("[ ]"), "The UI should reflect the incomplete icon.");
    }

    /**
     * Tests that trying to unmark an out-of-bounds index throws a princess exception.
     */
    @Test
    public void execute_invalidIndex_exceptionThrown() {
        tasks.add(new Todo("Find the glass slipper"));
        UnmarkCommand command = new UnmarkCommand("2");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_UNMARK.getText(), e.getMessage());
    }

    /**
     * Tests that the command triggers a save operation to persist the change.
     */
    @Test
    public void execute_persistsToStorage() throws AliothException {
        Todo todo = new Todo("Be kind");
        todo.setDone(true);
        tasks.add(todo);

        UnmarkCommand command = new UnmarkCommand("1");
        command.execute(tasks, ui, storage);

        assertTrue(Files.exists(TEST_PATH), "The task list should be saved after unmarking.");
    }
}
