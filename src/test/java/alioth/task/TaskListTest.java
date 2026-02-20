package alioth.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the TaskList class to verify list management and search logic.
 */
public class TaskListTest {
    private TaskList taskList;

    /**
     * Initializes an empty task list before each test.
     */
    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    /**
     * Tests that adding a task increases the list size.
     */
    @Test
    public void add_validTask_sizeIncreases() {
        taskList.add(new Todo("read book"));
        assertEquals(1, taskList.size());
    }

    /**
     * Tests that a task can be retrieved correctly by its index.
     */
    @Test
    public void get_validIndex_returnsTask() {
        Todo todo = new Todo("borrow book");
        taskList.add(todo);
        assertEquals(todo, taskList.get(0));
    }

    /**
     * Tests that removing a task decreases size and returns the correct task.
     */
    @Test
    public void remove_validIndex_removesTask() {
        Todo todo = new Todo("return book");
        taskList.add(todo);
        Task removed = taskList.remove(0);

        assertEquals(todo, removed);
        assertEquals(0, taskList.size());
    }

    /**
     * Tests that the find method returns only tasks containing the keyword.
     */
    @Test
    public void find_matchingKeyword_returnsFilteredList() {
        taskList.add(new Todo("royal ball"));
        taskList.add(new Todo("garden walk"));

        List<Task> results = taskList.find("royal");
        assertEquals(1, results.size());
        assertTrue(results.get(0).getDescription().contains("royal"));
    }

    /**
     * Tests that duplicates are correctly identified.
     */
    @Test
    public void containsDuplicate_sameTask_returnsTrue() {
        Todo t1 = new Todo("polish crown");
        taskList.add(t1);

        Todo t2 = new Todo("polish crown");
        assertTrue(taskList.containsDuplicate(t2), "Identical tasks should be flagged as duplicates.");
    }

    /**
     * Tests that distinct tasks are not flagged as duplicates.
     */
    @Test
    public void containsDuplicate_differentTask_returnsFalse() {
        taskList.add(new Todo("sleep"));
        assertFalse(taskList.containsDuplicate(new Todo("wake up")));
    }
}
