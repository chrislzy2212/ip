package alioth.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import alioth.task.Task;
import alioth.task.Todo;
import alioth.task.Deadline;
import alioth.task.Event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {

    @Test
    public void saveLoad_threeTasks_success() throws Exception {
        Path filePath = Paths.get("data", "test-duke.txt");
        Storage storage = new Storage(filePath);

        List<Task> tasks = new ArrayList<>();

        Todo t = new Todo("borrow book");
        t.setDone(true);
        tasks.add(t);

        Deadline d = new Deadline("return book", LocalDate.of(2026, 2, 1));
        d.setDone(false);
        tasks.add(d);

        Event e = new Event("project meeting",
                LocalDateTime.of(2026, 2, 1, 14, 0),
                LocalDateTime.of(2026, 2, 1, 16, 0));
        e.setDone(true);
        tasks.add(e);

        storage.save(tasks);

        List<Task> loaded = storage.load();
        assertEquals(3, loaded.size());

        // Compare via toString to avoid needing equals() in Task classes
        assertEquals(t.toString(), loaded.get(0).toString());
        assertEquals(d.toString(), loaded.get(1).toString());
        assertEquals(e.toString(), loaded.get(2).toString());

        // cleanup
        Files.deleteIfExists(filePath);
    }

    @Test
    public void load_missingFile_returnsEmptyList() throws Exception {
        Path filePath = Paths.get("data", "this-file-should-not-exist-12345.txt");
        Storage storage = new Storage(filePath);

        // make sure it doesn't exist (if it does, delete it)
        Files.deleteIfExists(filePath);

        List<Task> loaded = storage.load();
        assertEquals(0, loaded.size());
    }
}
