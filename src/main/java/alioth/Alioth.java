package alioth;

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

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(Storage.getDefaultPath());
    private static final TaskList tasks = new TaskList();

    /**
     * Starts the chatbot.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        ui.showWelcome();

        try {
            tasks.asList().addAll(storage.load());
        } catch (AliothException e) {
            ui.showError(e.getMessage());
        }

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

    private static boolean handleInput(String input) throws AliothException {
        if (!input.equals(input.stripLeading())) {
            throw new AliothException("OOPS!!! Command should not start with space(s).");
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
            if (args.isEmpty()) {
                throw new AliothException("OOPS!!! The description of a todo cannot be empty.");
            }
            addTodo(args);
            return false;

        case "deadline":
            if (args.isEmpty()) {
                throw new AliothException("OOPS!!! Invalid deadline format.");
            }
            addDeadline(args);
            return false;

        case "event":
            if (args.isEmpty()) {
                throw new AliothException("OOPS!!! Invalid event format.");
            }
            addEvent(args);
            return false;

        default:
            throw new AliothException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private static void addTask(Task task) {
        tasks.add(task);
        ui.showAddTask(task, tasks.size());
        saveTasks();
    }

    private static void addTodo(String args) throws AliothException {
        String description = args.trim();

        if (description.isEmpty()) {
            throw new AliothException("OOPS!!! The description of a todo cannot be empty.");
        }

        addTask(new Todo(description));
    }

    private static void addDeadline(String args) throws AliothException {
        String[] parts = args.split(" /by ", 2);

        if (parts.length != 2) {
            throw new AliothException("OOPS!!! Invalid deadline format. Use: deadline <desc> /by yyyy-MM-dd");
        }

        String description = parts[0].trim();
        String byString = parts[1].trim();

        if (description.isEmpty() || byString.isEmpty()) {
            throw new AliothException("OOPS!!! Invalid deadline format. Use: deadline <desc> /by yyyy-MM-dd");
        }

        LocalDate byDate;
        try {
            byDate = LocalDate.parse(byString);
        } catch (DateTimeParseException e) {
            throw new AliothException("OOPS!!! Invalid deadline format. Use: deadline <desc> /by yyyy-MM-dd");
        }

        addTask(new Deadline(description, byDate));
    }

    private static void addEvent(String args) throws AliothException {
        String[] firstSplit = args.split(" /from ", 2);

        if (firstSplit.length != 2) {
            throw new AliothException("OOPS!!! Invalid event format. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        String description = firstSplit[0].trim();
        String[] secondSplit = firstSplit[1].split(" /to ", 2);

        if (secondSplit.length != 2) {
            throw new AliothException("OOPS!!! Invalid event format. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        String fromString = secondSplit[0].trim();
        String toString = secondSplit[1].trim();

        if (description.isEmpty() || fromString.isEmpty() || toString.isEmpty()) {
            throw new AliothException("OOPS!!! Invalid event format. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(fromString, EVENT_INPUT_FORMAT);
            to = LocalDateTime.parse(toString, EVENT_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new AliothException("OOPS!!! Invalid event format. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        addTask(new Event(description, from, to));
    }

    private static void markTask(String args) throws AliothException {
        int index = parseTaskIndex(args, "mark");
        Task task = tasks.get(index);
        task.setDone(true);

        ui.showMarkTask(task);
        saveTasks();
    }

    private static void unmarkTask(String args) throws AliothException {
        int index = parseTaskIndex(args, "unmark");
        Task task = tasks.get(index);
        task.setDone(false);

        ui.showUnmarkTask(task);
        saveTasks();
    }

    private static void deleteTask(String args) throws AliothException {
        int index = parseTaskIndex(args, "delete");
        Task removed = tasks.remove(index);

        ui.showDeleteTask(removed, tasks.size());
        saveTasks();
    }

    private static int parseTaskIndex(String args, String commandWord) throws AliothException {
        int taskNumber = Parser.parseTaskNumber(args, commandWord);

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AliothException("OOPS!!! Invalid " + commandWord + " format.");
        }

        return taskNumber - 1;
    }

    private static void saveTasks() {
        try {
            storage.save(tasks.asList());
        } catch (AliothException e) {
            ui.showError(e.getMessage());
        }
    }
}
