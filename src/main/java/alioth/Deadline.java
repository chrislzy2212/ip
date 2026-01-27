package alioth;

/**
 * Represents a task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task with the given description and deadline.
     *
     * @param description Description of the task.
     * @param by Deadline of the task.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
