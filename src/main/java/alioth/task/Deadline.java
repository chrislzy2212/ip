package alioth.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that needs to be done before a specific date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy")
                    .withLocale(Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates a deadline task with the given description and deadline date.
     *
     * @param description Description of the task.
     * @param by Deadline date of the task.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        assert by != null : "Deadline 'by' date should not be null";
        this.by = by;
    }

    /**
     * Returns the deadline date of this task.
     *
     * @return Deadline date.
     */
    public LocalDate getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}
