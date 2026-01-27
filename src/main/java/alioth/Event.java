package alioth;

/**
 * Represents a task that starts and ends at a specific date/time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task with the given description, start, and end.
     *
     * @param description Description of the task.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
