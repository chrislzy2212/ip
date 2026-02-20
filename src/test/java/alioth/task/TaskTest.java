package alioth.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the base Task class to verify core status and description logic.
 */
public class TaskTest {

    /**
     * Tests that a new task is created with the correct description and "not done" status.
     */
    @Test
    public void constructor_validDescription_initializesCorrectly() {
        Task task = new Task("Read royal scrolls");

        assertEquals("Read royal scrolls", task.getDescription());
        assertFalse(task.isDone(), "A new task should be incomplete by default.");
    }

    /**
     * Tests that the completion status can be updated and retrieved.
     */
    @Test
    public void setDone_changeStatus_updatesCorrectly() {
        Task task = new Task("Visit the garden");

        task.setDone(true);
        assertTrue(task.isDone());

        task.setDone(false);
        assertFalse(task.isDone());
    }

    /**
     * Tests that the toString method provides the correct status icons.
     */
    @Test
    public void toString_statusToggle_returnsCorrectIcons() {
        Task task = new Task("Prepare for the ball");

        assertEquals("[ ] Prepare for the ball", task.toString());

        task.setDone(true);
        assertEquals("[X] Prepare for the ball", task.toString());
    }
}
