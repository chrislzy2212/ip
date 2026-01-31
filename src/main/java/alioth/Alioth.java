package alioth;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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

    private static final String LINE = "____________________________________________________________";

    private static final Storage storage = new Storage(Storage.getDefaultPath());
    private static final List<Task> tasks = new ArrayList<>();

    /**
     * Starts the chatbot.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        printWelcome();

        try {
            tasks.addAll(storage.load());
        } catch (AliothException e) {
            printError(e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            try {
                boolean shouldExit = handleInput(input);
                if (shouldExit) {
                    break;
                }
            } catch (AliothException e) {
                printError(e.getMessage());
            }
        }
    }

    private static boolean handleInput(String input) throws AliothException {
        if (!input.equals(input.stripLeading())) {
            throw new AliothException("OOPS!!! Command should not start with space(s).");
        }

        if (input.equals("bye")) {
            printBye();
            return true;
        }

        if (input.equals("list")) {
            printList();
            return false;
        }

        if (input.startsWith("mark ")) {
            markTask(input);
            return false;
        }

        if (input.startsWith("unmark ")) {
            unmarkTask(input);
            return false;
        }

        if (input.startsWith("delete ")) {
            deleteTask(input);
            return false;
        }

        if (input.equals("todo")) {
            throw new AliothException("OOPS!!! The description of a todo cannot be empty.");
        }

        if (input.startsWith("todo ")) {
            addTodo(input);
            return false;
        }

        if (input.equals("deadline")) {
            throw new AliothException("OOPS!!! Invalid deadline format.");
        }

        if (input.startsWith("deadline ")) {
            addDeadline(input);
            return false;
        }

        if (input.equals("event")) {
            throw new AliothException("OOPS!!! Invalid event format.");
        }

        if (input.startsWith("event ")) {
            addEvent(input);
            return false;
        }

        throw new AliothException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    private static void addTask(Task task) {
        tasks.add(task);

        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(LINE);

        try {
            storage.save(tasks);
        } catch (AliothException e) {
            printError(e.getMessage());
        }
    }

    private static void addTodo(String input) throws AliothException {
        String description = input.substring("todo ".length());

        if (description.trim().isEmpty()) {
            throw new AliothException("OOPS!!! The description of a todo cannot be empty.");
        }

        addTask(new Todo(description.trim()));
    }

    private static void addDeadline(String input) throws AliothException {
        String details = input.substring("deadline ".length());
        String[] parts = details.split(" /by ", 2);

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
            byDate = LocalDate.parse(byString); // expects yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new AliothException("OOPS!!! Invalid deadline format. Use: deadline <desc> /by yyyy-MM-dd");
        }

        addTask(new Deadline(description, byDate));
    }


    private static void addEvent(String input) throws AliothException {
        String details = input.substring("event ".length());
        String[] firstSplit = details.split(" /from ", 2);

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

    private static void markTask(String input) throws AliothException {
        int index = parseTaskIndex(input, "mark");
        Task task = tasks.get(index);
        task.setDone(true);

        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);

        try {
            storage.save(tasks);
        } catch (AliothException e) {
            printError(e.getMessage());
        }
    }

    private static void unmarkTask(String input) throws AliothException {
        int index = parseTaskIndex(input, "unmark");
        Task task = tasks.get(index);
        task.setDone(false);

        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(LINE);

        try {
            storage.save(tasks);
        } catch (AliothException e) {
            printError(e.getMessage());
        }
    }

    private static void deleteTask(String input) throws AliothException {
        int index = parseTaskIndex(input, "delete");
        Task removed = tasks.remove(index);

        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(LINE);

        try {
            storage.save(tasks);
        } catch (AliothException e) {
            printError(e.getMessage());
        }
    }

    private static int parseTaskIndex(String input, String commandWord) throws AliothException {
        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new AliothException("OOPS!!! Invalid " + commandWord + " format.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new AliothException("OOPS!!! Invalid " + commandWord + " format.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AliothException("OOPS!!! Invalid " + commandWord + " format.");
        }

        return taskNumber - 1;
    }

    private static void printList() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }

        System.out.println(LINE);
    }

    private static void printError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    private static void printWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Alioth");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void printBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
