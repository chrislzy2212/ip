package alioth.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.task.Task;
import alioth.task.Todo;

/**
 * Tests for the Ui class to ensure royal messaging and buffering work correctly.
 */
public class UiTest {
    private Ui ui;

    /**
     * Sets up a fresh UI instance before each test.
     */
    @BeforeEach
    public void setUp() {
        ui = new Ui();
    }

    /**
     * Tests that the welcome message is correctly buffered.
     */
    @Test
    public void showWelcome_populatedBuffer_success() {
        ui.showWelcome();
        String output = ui.consumeOutput();

        assertTrue(output.contains("Salutations! I am Alioth"));
        assertTrue(output.contains("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
    }

    /**
     * Tests that consumeOutput clears the internal buffer after being called.
     */
    @Test
    public void consumeOutput_clearsBuffer_success() {
        ui.showMessage("Test Message");
        ui.consumeOutput();

        assertEquals("", ui.consumeOutput(), "Buffer should be empty after consumption.");
    }

    /**
     * Tests the formatting of the royal task list.
     */
    @Test
    public void showTasks_formattedList_success() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("Read royal scrolls"));

        ui.showTasks(tasks);
        String output = ui.consumeOutput();

        assertTrue(output.contains("1. [T][ ] Read royal scrolls"));
        assertTrue(output.contains("Checking royal records"));
    }

    /**
     * Tests that formatError produces a standalone string without affecting the buffer.
     */
    @Test
    public void formatError_returnsFormattedString_success() {
        String error = ui.formatError("Oh no!");

        assertTrue(error.contains("Oh no!"));
        assertTrue(error.startsWith("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
        assertEquals("", ui.consumeOutput(), "formatError should not populate the main buffer.");
    }
}
