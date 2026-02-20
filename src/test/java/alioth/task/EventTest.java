package alioth.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Event task to verify dual-date formatting and status updates.
 */
public class EventTest {

    /**
     * Tests that the toString method correctly formats the event with start and end times.
     */
    @Test
    public void toString_validEvent_formattedCorrectly() {
        LocalDateTime from = LocalDateTime.of(2026, 2, 20, 18, 0);
        LocalDateTime to = LocalDateTime.of(2026, 2, 20, 22, 0);
        Event event = new Event("Royal Ball", from, to);

        // Verifies the princess formatting: [E][ ] desc (from: MMM dd yyyy HH:mm to: MMM dd yyyy HH:mm)
        String expected = "[E][ ] Royal Ball (from: Feb 20 2026 18:00 to: Feb 20 2026 22:00)";
        assertEquals(expected, event.toString());
    }

    /**
     * Tests that the done status is correctly reflected without altering the time information.
     */
    @Test
    public void toString_completedEvent_showsDoneIcon() {
        LocalDateTime from = LocalDateTime.of(2026, 12, 25, 10, 0);
        LocalDateTime to = LocalDateTime.of(2026, 12, 25, 14, 0);
        Event event = new Event("Christmas Brunch", from, to);
        event.setDone(true);

        assertTrue(event.toString().contains("[X]"));
        assertTrue(event.toString().contains("Dec 25 2026 10:00"));
        assertTrue(event.toString().contains("Dec 25 2026 14:00"));
    }

    /**
     * Tests that start and end times can be retrieved correctly.
     */
    @Test
    public void getTimes_returnCorrectObjects() {
        LocalDateTime from = LocalDateTime.of(2026, 5, 4, 9, 0);
        LocalDateTime to = LocalDateTime.of(2026, 5, 4, 17, 0);
        Event event = new Event("Jedi Training", from, to);

        assertEquals(from, event.getFrom());
        assertEquals(to, event.getTo());
    }

    /**
     * Tests that the date and time format remains in English even if the system locale changes.
     * Events use MMM dd yyyy HH:mm, which is highly sensitive to Locale settings.
     */
    @Test
    public void toString_differentLocale_remainsEnglish() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.GERMANY);

            LocalDateTime from = LocalDateTime.of(2026, 3, 15, 14, 30);
            LocalDateTime to = LocalDateTime.of(2026, 3, 15, 17, 0);
            Event e = new Event("Royal Tea", from, to);
            String output = e.toString();
            assertTrue(output.contains("Mar 15 2026 14:30"),
                    "The 'from' date/time should remain in English.");
            assertTrue(output.contains("Mar 15 2026 17:00"),
                    "The 'to' date/time should remain in English.");

        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}
