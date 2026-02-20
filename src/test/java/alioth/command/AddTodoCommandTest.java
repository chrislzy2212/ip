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
 * Tests for AddTodoCommand to verify description validation and duplicate checking.
 */
public class AddTodoCommandTest {
    private static final Path TEST_PATH = Paths.get("data", "test-todo.txt");
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
     * Tests that a valid todo is correctly added to the list and storage.
     */
    @Test
    public void execute_validTodo_success() throws AliothException {
        AddTodoCommand command = new AddTodoCommand("Read royal scrolls");
        command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] Read royal scrolls", tasks.get(0).toString());
    }

    /**
     * Tests that an empty description throws a princess-themed exception.
     */
    @Test
    public void execute_emptyDescription_exceptionThrown() {
        AddTodoCommand command = new AddTodoCommand("   ");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_TODO.getText(), e.getMessage());
    }

    /**
     * Tests that duplicate todos are identified and rejected.
     */
    @Test
    public void execute_duplicateTodo_exceptionThrown() throws AliothException {
        tasks.add(new Todo("Visit the garden"));
        AddTodoCommand command = new AddTodoCommand("Visit the garden");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.DUPLICATE.getText(), e.getMessage());
    }
}
