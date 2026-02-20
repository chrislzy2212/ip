package alioth.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.task.Todo;
import alioth.ui.Ui;

/**
 * Tests for ListCommand to verify task retrieval and display logic.
 */
public class ListCommandTest {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(Paths.get("data", "test-list.txt"));
    }

    /**
     * Tests that the command correctly displays tasks in the UI buffer.
     */
    @Test
    public void execute_withTasks_showsInUi() {
        tasks.add(new Todo("First task"));
        tasks.add(new Todo("Second task"));

        ListCommand command = new ListCommand();
        command.execute(tasks, ui, storage);

        String output = ui.consumeOutput();
        assertTrue(output.contains("Checking royal records"));
        assertTrue(output.contains("1. [T][ ] First task"));
        assertTrue(output.contains("2. [T][ ] Second task"));
    }

    /**
     * Tests that the command handles an empty list without crashing.
     */
    @Test
    public void execute_emptyList_showsEmptyHeader() {
        ListCommand command = new ListCommand();
        command.execute(tasks, ui, storage);

        String output = ui.consumeOutput();
        assertTrue(output.contains("Checking royal records"));
        // The UI should show the header even if no tasks follow
    }
}
