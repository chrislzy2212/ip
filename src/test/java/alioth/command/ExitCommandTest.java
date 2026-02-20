package alioth.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.exception.AliothException;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Tests for ExitCommand to ensure the application signals for termination correctly.
 */
public class ExitCommandTest {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(Paths.get("data", "test-exit.txt"));
    }

    /**
     * Tests that isExit returns true for this command.
     */
    @Test
    public void isExit_returnsTrue() {
        ExitCommand command = new ExitCommand();
        assertTrue(command.isExit(), "ExitCommand must signal the program to terminate.");
    }

    /**
     * Tests that the goodbye message is buffered in the UI.
     */
    @Test
    public void execute_buffersByeMessage() throws AliothException {
        ExitCommand command = new ExitCommand();
        command.execute(tasks, ui, storage);

        String output = ui.consumeOutput();
        assertTrue(output.contains("Goodbye for now!"), "The royal farewell should be shown.");
    }
}
