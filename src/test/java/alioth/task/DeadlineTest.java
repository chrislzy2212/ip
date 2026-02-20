package alioth.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Deadline task to verify formatting and date handling.
 */
public class DeadlineTest {

    /**
     * Tests that the toString method correctly formats the task with its deadline.
     */
    @Test
    public void toString_validDeadline_formattedCorrectly() {
        LocalDate date = LocalDate.of(2026, 2, 20);
        Deadline deadline = new Deadline("submit royal report", date);

        String expected = "[D][ ] submit royal report (by: Feb 20 2026)";
        assertEquals(expected, deadline.toString());
    }

    /**
     * Tests that the done status is correctly reflected in the string representation.
     */
    @Test
    public void toString_completedDeadline_showsDoneIcon() {
        LocalDate date = LocalDate.of(2026, 12, 25);
        Deadline deadline = new Deadline("christmas party", date);
        deadline.setDone(true);

        assertTrue(deadline.toString().contains("[X]"));
        assertTrue(deadline.toString().contains("Dec 25 2026"));
    }

    /**
     * Tests that the getBy method returns the correct date object.
     */
    @Test
    public void getBy_returnsCorrectDate() {
        LocalDate date = LocalDate.of(2026, 5, 4);
        Deadline deadline = new Deadline("star wars marathon", date);

        assertEquals(date, deadline.getBy());
    }

    /**
     * Tests that the date format remains in English even if the system locale changes.
     * This ensures cross-platform consistency for the royal records.
     */
    @Test
    public void toString_differentLocale_remainsEnglish() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.FRENCH);

            Deadline d = new Deadline("Royal Ball", LocalDate.of(2026, 2, 20));
            assertTrue(d.toString().contains("Feb"),
                    "The month should remain in English regardless of system locale.");

        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}
