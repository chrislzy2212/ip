package alioth.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
 * Tests for DeleteCommand to verify index handling and removal logic.
 */
public class DeleteCommandTest {
    private static final Path TEST_PATH = Paths.get("data", "test-delete.txt");
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
     * Tests that a valid task is removed and the list size decreases.
     */
    @Test
    public void execute_validIndex_success() throws AliothException {
        tasks.add(new Todo("Discarded scroll"));
        DeleteCommand command = new DeleteCommand("1");
        command.execute(tasks, ui, storage);

        assertEquals(0, tasks.size());
    }

    /**
     * Tests that an out-of-bounds index throws a princess-themed exception.
     */
    @Test
    public void execute_invalidIndex_exceptionThrown() {
        tasks.add(new Todo("Safe task"));
        DeleteCommand command = new DeleteCommand("5");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_DELETE.getText(), e.getMessage());
    }

    /**
     * Tests that non-numeric input is rejected during parsing.
     */
    @Test
    public void execute_nonNumericInput_exceptionThrown() {
        DeleteCommand command = new DeleteCommand("abc");

        assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
    }
}
