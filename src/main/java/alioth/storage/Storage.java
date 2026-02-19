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

            List<String> lines = tasks.stream()
                    .map(this::convertTaskToLine)
                    .toList();

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
        String[] parts = splitLine(line);
        if (parts == null) {
            return null;
        }

        String type = parts[0].trim();
        Boolean isDone = parseDoneFlag(parts[1].trim());
        String description = parts[2].trim();

        if (isDone == null || description.isEmpty()) {
            return null;
        }

        Task task = createTaskFromParts(type, description, parts);
        if (task == null) {
            return null;
        }

        task.setDone(isDone);
        return task;
    }

    private String[] splitLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }
        return parts;
    }

    private Boolean parseDoneFlag(String doneFlag) {
        if (doneFlag.equals("1")) {
            return true;
        }
        if (doneFlag.equals("0")) {
            return false;
        }
        return null;
    }

    private Task createTaskFromParts(String type, String description, String[] parts) {
        if (type.equals("T")) {
            return createTodo(description, parts);
        }
        if (type.equals("D")) {
            return createDeadline(description, parts);
        }
        if (type.equals("E")) {
            return createEvent(description, parts);
        }
        return null;
    }

    private Task createTodo(String description, String[] parts) {
        if (parts.length != 3) {
            return null;
        }
        return new Todo(description);
    }

    private Task createDeadline(String description, String[] parts) {
        if (parts.length != 4) {
            return null;
        }
        String byString = parts[3].trim();
        if (byString.isEmpty()) {
            return null;
        }

        try {
            LocalDate by = LocalDate.parse(byString); // yyyy-MM-dd
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Task createEvent(String description, String[] parts) {
        if (parts.length != 5) {
            return null;
        }

        String fromString = parts[3].trim();
        String toString = parts[4].trim();
        if (fromString.isEmpty() || toString.isEmpty()) {
            return null;
        }

        try {
            LocalDateTime from = LocalDateTime.parse(fromString, EVENT_FILE_FORMAT);
            LocalDateTime to = LocalDateTime.parse(toString, EVENT_FILE_FORMAT);
            return new Event(description, from, to);
        } catch (DateTimeParseException e) {
            return null;
        }
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
