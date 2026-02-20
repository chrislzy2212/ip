package alioth.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.exception.AliothException;
import alioth.message.Message;
import alioth.storage.Storage;
import alioth.task.Deadline;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Tests for the AddDeadlineCommand to ensure robust parsing and duplicate checking.
 */
public class AddDeadlineCommandTest {
    private static final Path TEST_PATH = Paths.get("data", "test-deadline.txt");
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
     * Tests that a valid deadline is correctly parsed and added to the list.
     */
    @Test
    public void execute_validDeadline_success() throws AliothException {
        AddDeadlineCommand command = new AddDeadlineCommand("Fix crown /by 2026-02-20");
        command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("[D][ ] Fix crown (by: Feb 20 2026)", tasks.get(0).toString());
    }

    /**
     * Tests that adding a duplicate deadline throws a princess-themed exception.
     */
    @Test
    public void execute_duplicateTask_exceptionThrown() throws AliothException {
        tasks.add(new Deadline("Tea party", LocalDate.of(2026, 2, 20)));
        AddDeadlineCommand command = new AddDeadlineCommand("Tea party /by 2026-02-20");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.DUPLICATE.getText(), e.getMessage());
    }

    /**
     * Tests that an invalid date format triggers a descriptive error message.
     */
    @Test
    public void execute_invalidDate_exceptionThrown() {
        AddDeadlineCommand command = new AddDeadlineCommand("Ball /by 2026-13-40");

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.INVALID_DEADLINE.getText(), e.getMessage());
    }

    /**
     * Tests that missing arguments around the /by tag are rejected.
     */
    @Test
    public void execute_missingArgs_exceptionThrown() {
        AddDeadlineCommand command = new AddDeadlineCommand("Dance /by ");
        assertThrows(AliothException.class, () -> command.execute(tasks, ui, storage));

        AddDeadlineCommand command2 = new AddDeadlineCommand(" /by 2026-02-20");
        assertThrows(AliothException.class, () -> command2.execute(tasks, ui, storage));
    }
}
