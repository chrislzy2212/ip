package alioth.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import alioth.task.Deadline;
import alioth.task.Event;
import alioth.task.Task;
import alioth.task.Todo;

/**
 * Tests for the Storage class to ensure persistence logic is robust,
 * internationalization-ready, and corruption-resistant.
 */
public class StorageTest {
    @TempDir
    Path tempDir;

    private Path testFilePath;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test-alioth.txt");
        storage = new Storage(testFilePath);
    }

    /**
     * Tests that tasks can be saved to and loaded from disk successfully.
     */
    @Test
    public void saveLoad_validTasks_success() throws Exception {
        List<Task> tasks = new ArrayList<>();

        Todo t = new Todo("borrow book");
        t.setDone(true);
        tasks.add(t);

        Deadline d = new Deadline("return book", LocalDate.of(2026, 2, 20));
        tasks.add(d);

        Event e = new Event("royal ball",
                LocalDateTime.of(2026, 2, 20, 18, 0),
                LocalDateTime.of(2026, 2, 20, 22, 0));
        tasks.add(e);

        storage.save(tasks);
        assertTrue(Files.exists(testFilePath));

        List<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertEquals(t.toString(), loaded.get(0).toString());
        assertEquals(d.toString(), loaded.get(1).toString());
        assertEquals(e.toString(), loaded.get(2).toString());
    }

    /**
     * Tests that the storage correctly handles non-English characters and Emojis.
     * This verifies UTF-8 encoding is strictly enforced.
     */
    @Test
    public void saveLoad_internationalCharacters_success() throws Exception {
        String description = "提交报告 👑"; // Chinese and Emoji
        List<Task> tasks = List.of(new Todo(description));

        storage.save(tasks);
        List<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals(description, loaded.get(0).getDescription());
    }

    /**
     * Tests that a missing file returns an empty list instead of throwing an error.
     */
    @Test
    public void load_missingFile_returnsEmptyList() throws Exception {
        Path missingPath = tempDir.resolve("non-existent.txt");
        Storage missingStorage = new Storage(missingPath);
        List<Task> loaded = missingStorage.load();
        assertEquals(0, loaded.size());
    }

    /**
     * Tests that corrupted lines or mismatched formats are skipped gracefully.
     */
    @Test
    public void load_corruptedFile_skipsInvalidLines() throws Exception {
        List<String> corruptedLines = List.of(
                "T | 1 | valid todo",
                "X | 0 | unknown type",
                "D | 0 | missing date",
                "E | 0 | event | 2026-02-20",
                "T | ? | bad status | desc"
        );
        Files.write(testFilePath, corruptedLines, StandardCharsets.UTF_8);

        List<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertEquals("valid todo", loaded.get(0).getDescription());
    }

    /**
     * Tests that dates are saved/loaded consistently regardless of System Locale.
     */
    @Test
    public void load_differentLocale_parsesCorrectly() throws Exception {
        Locale originalLocale = Locale.getDefault();
        try {
            List<Task> tasks = List.of(new Deadline("Meeting", LocalDate.of(2026, 2, 20)));
            storage.save(tasks);
            Locale.setDefault(Locale.FRENCH);

            List<Task> loaded = storage.load();
            assertEquals(1, loaded.size());
            assertTrue(loaded.get(0).toString().contains("Feb"));

        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}
