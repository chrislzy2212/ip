package alioth.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import alioth.AliothException;
import alioth.Message;
import alioth.task.Deadline;
import alioth.task.Event;
import alioth.task.Task;
import alioth.task.Todo;

/**
 * Handles saving and loading tasks to/from the hard disk.
 */
public class Storage {
    private static final DateTimeFormatter EVENT_FILE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final Path filePath;

    /**
     * Creates a storage that saves to the given relative path.
     *
     * @param filePath Path to the save file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from disk.
     * If the file does not exist, returns an empty list.
     *
     * @return List of tasks loaded from file.
     * @throws AliothException If there is an IO problem or corrupted data (optional handling).
     */
    public List<Task> load() throws AliothException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new AliothException(Message.SAVE_ERROR.getText());
        }

        List<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            Task task = parseLineToTask(line);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Saves tasks to disk. Creates the folder/file if needed.
     *
     * @param tasks Tasks to save.
     * @throws AliothException If there is an IO problem.
     */
    public void save(List<Task> tasks) throws AliothException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent); // creates ./data if missing
            }

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(convertTaskToLine(task));
            }

            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new AliothException(Message.SAVE_ERROR.getText());
        }
    }

    /**
     * Converts one saved line into a Task object.
     * If line is corrupted, we skip it by returning null.
     *
     * @param line Line from save file.
     * @return Task or null if line is invalid.
     */
    private Task parseLineToTask(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            return null; // corrupted line
        }

        String type = parts[0].trim();
        String doneFlag = parts[1].trim();
        String description = parts[2].trim();

        if (description.isEmpty()) {
            return null;
        }

        boolean isDone;
        if (doneFlag.equals("1")) {
            isDone = true;
        } else if (doneFlag.equals("0")) {
            isDone = false;
        } else {
            return null;
        }

        Task task;
        if (type.equals("T")) {
            if (parts.length != 3) {
                return null;
            }
            task = new Todo(description);
        } else if (type.equals("D")) {
            if (parts.length != 4) {
                return null;
            }
            String byString = parts[3].trim();
            if (byString.isEmpty()) {
                return null;
            }

            LocalDate by;
            try {
                by = LocalDate.parse(byString); // yyyy-MM-dd
            } catch (DateTimeParseException e) {
                return null;
            }

            task = new Deadline(description, by);
        } else if (type.equals("E")) {
            if (parts.length != 5) {
                return null;
            }
            String fromString = parts[3].trim();
            String toString = parts[4].trim();
            if (fromString.isEmpty() || toString.isEmpty()) {
                return null;
            }

            LocalDateTime from;
            LocalDateTime to;
            try {
                from = LocalDateTime.parse(fromString, EVENT_FILE_FORMAT);
                to = LocalDateTime.parse(toString, EVENT_FILE_FORMAT);
            } catch (DateTimeParseException e) {
                return null;
            }

            task = new Event(description, from, to);
        } else {
            return null;
        }

        task.setDone(isDone);
        return task;
    }

    /**
     * Converts a Task into one line for saving.
     *
     * @param task Task to convert.
     * @return String line to write into save file.
     */
    private String convertTaskToLine(Task task) {
        String doneFlag = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + doneFlag + " | " + task.getDescription();
        }

        if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D | " + doneFlag + " | " + task.getDescription() + " | " + d.getBy();
        }

        if (task instanceof Event) {
            Event e = (Event) task;
            return "E | " + doneFlag + " | " + task.getDescription()
                    + " | " + e.getFrom().format(EVENT_FILE_FORMAT)
                    + " | " + e.getTo().format(EVENT_FILE_FORMAT);
        }

        return "T | " + doneFlag + " | " + task.getDescription();
    }

    /**
     * Creates the default storage file path.
     *
     * @return Default save file path.
     */
    public static Path getDefaultPath() {
        return Paths.get("data", "alioth.txt");
    }
}
