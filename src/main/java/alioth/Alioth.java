package alioth;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A chatbot that can store tasks of different types and display them on request.
 */
public class Alioth {
    private static final DateTimeFormatter EVENT_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates an instance of the chatbot using the given file path to load and save tasks.
     *
     * @param filePath File path for the storage file.
     */
    public Alioth(String filePath) {
        ui = new Ui();
        storage = new Storage(Paths.get(filePath));

        TaskList tempTasks;
        try {
            tempTasks = new TaskList(storage.load());
        } catch (AliothException e) {
            ui.showError(e.getMessage());
            tempTasks = new TaskList();
        }
        tasks = tempTasks;
    }

    /**
     * Starts the chatbot.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        new Alioth("data/duke.txt").run();
    }

    /**
     * Runs the chatbot until the user exits.
     */
    public void run() {
        ui.showWelcome();
        while (true) {
            String input = ui.readCommand();
            try {
                boolean shouldExit = handleInput(input);
                if (shouldExit) {
                    break;
                }
            } catch (AliothException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private boolean handleInput(String input) throws AliothException {
        if (!input.equals(input.stripLeading())) {
            throw new AliothException(Message.LEADING_SPACES.getText());
        }

        String command = Parser.getCommandWord(input);
        String args = Parser.getCommandArgs(input);

        switch (command) {
        case "bye":
            ui.showBye();
            return true;

        case "list":
            ui.showTasks(tasks.asList());
            return false;

        case "mark":
            markTask(args);
            return false;

        case "unmark":
            unmarkTask(args);
            return false;

        case "delete":
            deleteTask(args);
            return false;

        case "todo":
            addTodo(args);
            return false;

        case "deadline":
            addDeadline(args);
            return false;

        case "event":
            addEvent(args);
            return false;

        default:
            throw new AliothException(Message.UNKNOWN_COMMAND.getText());
        }
    }

    private void addTask(Task task) {
        tasks.add(task);
        ui.showAddTask(task, tasks.size());
        saveTasks();
    }

    private void addTodo(String args) throws AliothException {
        String description = args.trim();
        if (description.isEmpty()) {
            throw new AliothException(Message.INVALID_TODO.getText());
        }
        addTask(new Todo(description));
    }

    private void addDeadline(String args) throws AliothException {
        if (args.trim().isEmpty()) {
            throw new AliothException(Message.INVALID_DEADLINE.getText());
        }

        String[] parts = args.split(" /by ", 2);
        if (parts.length != 2) {
            throw new AliothException(Message.INVALID_DEADLINE.getText());
        }

        String description = parts[0].trim();
        String byString = parts[1].trim();
        if (description.isEmpty() || byString.isEmpty()) {
            throw new AliothException(Message.INVALID_DEADLINE.getText());
        }

        LocalDate byDate;
        try {
            byDate = LocalDate.parse(byString);
        } catch (DateTimeParseException e) {
            throw new AliothException(Message.INVALID_DEADLINE.getText());
        }

        addTask(new Deadline(description, byDate));
    }

    private void addEvent(String args) throws AliothException {
        if (args.trim().isEmpty()) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        String[] firstSplit = args.split(" /from ", 2);
        if (firstSplit.length != 2) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        String description = firstSplit[0].trim();
        String[] secondSplit = firstSplit[1].split(" /to ", 2);
        if (secondSplit.length != 2) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        String fromString = secondSplit[0].trim();
        String toString = secondSplit[1].trim();
        if (description.isEmpty() || fromString.isEmpty() || toString.isEmpty()) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(fromString, EVENT_INPUT_FORMAT);
            to = LocalDateTime.parse(toString, EVENT_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        addTask(new Event(description, from, to));
    }

    private void markTask(String args) throws AliothException {
        int index = parseTaskIndex(args, "mark");
        Task task = tasks.get(index);
        task.setDone(true);

        ui.showMarkTask(task);
        saveTasks();
    }

    private void unmarkTask(String args) throws AliothException {
        int index = parseTaskIndex(args, "unmark");
        Task task = tasks.get(index);
        task.setDone(false);

        ui.showUnmarkTask(task);
        saveTasks();
    }

    private void deleteTask(String args) throws AliothException {
        int index = parseTaskIndex(args, "delete");
        Task removed = tasks.remove(index);

        ui.showDeleteTask(removed, tasks.size());
        saveTasks();
    }

    private int parseTaskIndex(String args, String commandWord) throws AliothException {
        int taskNumber = Parser.parseTaskNumber(args, commandWord);

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AliothException(Message.invalidIndexCommand(commandWord).getText());
        }

        return taskNumber - 1;
    }

    private void saveTasks() {
        try {
            storage.save(tasks.asList());
        } catch (AliothException e) {
            ui.showError(e.getMessage());
        }
    }
}
