package alioth.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.exception.AliothException;
import alioth.message.Message;
import alioth.storage.Storage;
import alioth.task.Event;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Tests for AddEventCommand to verify chronology, parsing, and uniqueness logic.
 */
public class AddEventCommandTest {
    private static final Path TEST_PATH = Paths.get("data", "test-event.txt");
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
     * Tests that a valid event is parsed and added correctly.
     */
    @Test
    public void execute_validEvent_success() throws AliothException {
        String input = "Royal Ball /from 2026-02-20 1800 /to 2026-02-20 2200";
        AddEventCommand command = new AddEventCommand(input);
        command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("[E][ ] Royal Ball (from: Feb 20 2026 18:00 to: Feb 20 2026 22:00)", tasks.get(0).toString());
    }

    /**
     * Tests that an event ending before it starts is rejected.
     */
    @Test
    public void execute_invalidChronology_exceptionThrown() {
        String input = "Time Paradox /from 2026-02-20 2200 /to 2026-02-20 1800";
        AddEventCommand command = new AddEventCommand(input);

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.EVENT_CHRONOLOGY.getText(), e.getMessage());
    }

    /**
     * Tests that duplicate events are flagged as errors.
     */
    @Test
    public void execute_duplicateEvent_exceptionThrown() throws AliothException {
        LocalDateTime from = LocalDateTime.of(2026, 2, 20, 18, 0);
        LocalDateTime to = LocalDateTime.of(2026, 2, 20, 22, 0);
        tasks.add(new Event("Ball", from, to));

        String input = "Ball /from 2026-02-20 1800 /to 2026-02-20 2200";
        AddEventCommand command = new AddEventCommand(input);

        AliothException e = assertThrows(AliothException.class, () ->
                command.execute(tasks, ui, storage));
        assertEquals(Message.DUPLICATE.getText(), e.getMessage());
    }

    /**
     * Tests that malformed delimiters or missing times trigger an invalid event message.
     */
    @Test
    public void execute_malformedInput_exceptionThrown() {
        AddEventCommand missingTo = new AddEventCommand("Party /from 2026-02-20 1800");
        assertThrows(AliothException.class, () -> missingTo.execute(tasks, ui, storage));

        AddEventCommand wrongDate = new AddEventCommand("Party /from today /to tomorrow");
        assertThrows(AliothException.class, () -> wrongDate.execute(tasks, ui, storage));
    }
}
