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

            if (input.equals("bye")) {
                printBye();
                break;
            } else if (input.equals("list")) {
                printList();
            } else if (input.startsWith("mark ")) {
                markTask(input);
            } else if (input.startsWith("unmark ")) {
                unmarkTask(input);
            } else if (input.startsWith("todo ")) {
                addTodo(input);
            } else if (input.startsWith("deadline ")) {
                addDeadline(input);
            } else if (input.startsWith("event ")) {
                addEvent(input);
            } else {
                addTask(new Todo(input));
            }
        }
    }

    private static int parseTaskNumber(String input) {
        String[] parts = input.split(" ", 2);
        return Integer.parseInt(parts[1]);
    }

    private static void markTask(String input) {
        int taskNumber = parseTaskNumber(input);
        int index = taskNumber - 1;

        Task task = TASKS[index];
        task.setDone(true);

        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    private static void unmarkTask(String input) {
        int taskNumber = parseTaskNumber(input);
        int index = taskNumber - 1;

        Task task = TASKS[index];
        task.setDone(false);

        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    private static void addTask(Task task) {
        TASKS[taskCount] = task;
        taskCount++;

        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void addTodo(String input) {
        String description = input.substring("todo ".length());
        addTask(new Todo(description));
    }

    private static void addDeadline(String input) {
        String details = input.substring("deadline ".length());
        String[] parts = details.split(" /by ", 2);

        String description = parts[0];
        String by = parts[1];

        addTask(new Deadline(description, by));
    }

    private static void addEvent(String input) {
        String details = input.substring("event ".length());
        String[] firstSplit = details.split(" /from ", 2);

        String description = firstSplit[0];
        String[] secondSplit = firstSplit[1].split(" /to ", 2);

        String from = secondSplit[0];
        String to = secondSplit[1];

        addTask(new Event(description, from, to));
    }

    private static void printList() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + TASKS[i]);
        }

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
