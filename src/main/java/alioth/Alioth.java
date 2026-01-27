package alioth;

import java.util.Scanner;

/**
 * A chatbot that can store tasks and display them on request.
 */
public class Alioth {
    private static final String LINE = "____________________________________________________________";

    private static final int MAX_TASKS = 100;
    private static final String[] TASKS = new String[MAX_TASKS];
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
            } else {
                addTask(input);
            }
        }
    }

    private static void addTask(String task) {
        TASKS[taskCount] = task;
        taskCount++;

        System.out.println(LINE);
        System.out.println("added: " + task);
        System.out.println(LINE);
    }

    private static void printList() {
        System.out.println(LINE);

        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + TASKS[i]);
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
