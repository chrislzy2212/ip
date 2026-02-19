package alioth.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of tasks and provides operations to manage them.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list by copying the given list of tasks.
     *
     * @param tasks The list of tasks to copy from.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Task list to copy should not be null";
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the task list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "TaskList should not add null task";
        tasks.add(task);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index Index of the task to retrieve.
     * @return The task at the given index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index Index of the task to remove.
     * @return The removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns a list of tasks whose descriptions contain the given keyword.
     *
     * @param keyword Keyword to search for.
     * @return List of matching tasks.
     */
    public List<Task> find(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .toList();
    }
    /**
     * Returns the number of tasks in the task list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return The list of tasks.
     */
    public List<Task> asList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Checks if a task already exists in the royal records to prevent duplicates.
     * It compares the string representation of the new task with existing ones.
     *
     * @param newTask The task to check for in the kingdom.
     * @return True if an identical task is already in the list, false otherwise.
     */
    public boolean containsDuplicate(Task newTask) {
        return tasks.stream().anyMatch(task ->
                task.toString().equals(newTask.toString())); //
    }
}
