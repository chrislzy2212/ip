package alioth.task;

/**
 * Represents a task with a description and a completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a new task with the given description.
     * The task is not done by default.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Sets the completion status of this task.
     *
     * @param status True to mark as done, false to mark as not done.
     */
    public void setDone(boolean status) {
        isDone = status;
    }

    /**
     * Returns the description of this task.
     *
     * @return Description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is done.
     *
     * @return True if done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a string representation of this task.
     *
     * @return Task in the form "[X] description" or "[ ] description".
     */
    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        return "[" + status + "] " + description;
    }
}
