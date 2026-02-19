package alioth.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that starts and ends at a specific date/time.
 */
public class Event extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event task with the given description, start, and end.
     *
     * @param description Description of the task.
     * @param from Start date/time of the event.
     * @param to End date/time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date/time of the event.
     *
     * @return Start date/time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end date/time of the event.
     *
     * @return End date/time.
     */
    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from.format(OUTPUT_FORMAT)
                + " to: " + to.format(OUTPUT_FORMAT) + ")";
    }
}
