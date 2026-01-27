package alioth;

import java.util.Scanner;

/**
 * A chatbot that can store tasks and display them on request.
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
            } else {
                addTask(input);
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

    private static void addTask(String task) {
        TASKS[taskCount] = new Task(task);
        taskCount++;

        System.out.println(LINE);
        System.out.println("added: " + task);
        System.out.println(LINE);
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
