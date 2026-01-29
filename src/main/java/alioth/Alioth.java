package alioth;

import java.util.Scanner;

/**
 * A chatbot that can store tasks of different types and display them on request.
 */
public class Alioth {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    private static final Task[] TASKS = new Task[MAX_TASKS];
    private static int taskCount = 0;

    /**
     * Starts the chatbot.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        printWelcome();

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
        if (input.equals("bye")) {
            printBye();
            return true;
        } else if (input.equals("list")) {
            printList();
            return false;
        } else if (input.startsWith("mark")) {
            markTask(input);
            return false;
        } else if (input.startsWith("unmark")) {
            unmarkTask(input);
            return false;
        } else if (input.startsWith("todo")) {
            addTodo(input);
            return false;
        } else if (input.startsWith("deadline")) {
            addDeadline(input);
            return false;
        } else if (input.startsWith("event")) {
            addEvent(input);
            return false;
        } else {
            throw new AliothException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private static void addTask(Task task) throws AliothException {
        if (taskCount >= MAX_TASKS) {
            throw new AliothException("OOPS!!! The task list is full.");
        }

        TASKS[taskCount] = task;
        taskCount++;

        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void addTodo(String input) throws AliothException {
        if (input.equals("todo")) {
            throw new AliothException("OOPS!!! The description of a todo cannot be empty.");
        }

        String description = input.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new AliothException("OOPS!!! The description of a todo cannot be empty.");
        }

        addTask(new Todo(description));
    }

    private static void addDeadline(String input) throws AliothException {
        if (input.equals("deadline")) {
            throw new AliothException("OOPS!!! Invalid deadline format.");
        }

        String details = input.substring("deadline".length()).trim();
        String[] parts = details.split(" /by ", 2);

        if (parts.length < 2) {
            throw new AliothException("OOPS!!! Invalid deadline format.");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new AliothException("OOPS!!! Invalid deadline format.");
        }

        addTask(new Deadline(description, by));
    }

    private static void addEvent(String input) throws AliothException {
        if (input.equals("event")) {
            throw new AliothException("OOPS!!! Invalid event format.");
        }

        String details = input.substring("event".length()).trim();
        String[] firstSplit = details.split(" /from ", 2);

        if (firstSplit.length < 2) {
            throw new AliothException("OOPS!!! Invalid event format.");
        }

        String description = firstSplit[0].trim();
        String[] secondSplit = firstSplit[1].split(" /to ", 2);

        if (secondSplit.length < 2) {
            throw new AliothException("OOPS!!! Invalid event format.");
        }

        String from = secondSplit[0].trim();
        String to = secondSplit[1].trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new AliothException("OOPS!!! Invalid event format.");
        }

        addTask(new Event(description, from, to));
    }

    private static void markTask(String input) throws AliothException {
        int index = parseTaskIndex(input, "mark");
        Task task = TASKS[index];
        task.setDone(true);

        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    private static void unmarkTask(String input) throws AliothException {
        int index = parseTaskIndex(input, "unmark");
        Task task = TASKS[index];
        task.setDone(false);

        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(LINE);
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

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new AliothException("OOPS!!! Invalid " + commandWord + " format.");
        }

        return taskNumber - 1;
    }

    private static void printList() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + TASKS[i]);
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
