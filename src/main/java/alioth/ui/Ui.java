package alioth.ui;

import alioth.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user interaction (input and output) for the chatbot.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a UI that reads input from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints the welcome message.
     */
    public void showWelcome() {
        showLine();
        System.out.println("Hello! I'm Alioth");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Prints the goodbye message.
     */
    public void showBye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Prints an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Prints all tasks in the provided list.
     *
     * @param tasks Tasks to display.
     */
    public void showTasks(List<Task> tasks) {
        showLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    /**
     * Prints confirmation after adding a task.
     *
     * @param task Task that was added.
     * @param size Current number of tasks in the list.
     */
    public void showAddTask(Task task, int size) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
        showLine();
    }

    /**
     * Prints confirmation after deleting a task.
     *
     * @param task Task that was removed.
     * @param size Current number of tasks in the list.
     */
    public void showDeleteTask(Task task, int size) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
        showLine();
    }

    /**
     * Prints confirmation after marking a task as done.
     *
     * @param task Task that was marked.
     */
    public void showMarkTask(Task task) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        showLine();
    }

    /**
     * Prints confirmation after unmarking a task.
     *
     * @param task Task that was unmarked.
     */
    public void showUnmarkTask(Task task) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        showLine();
    }

    /**
     * Reads the next line of user input.
     *
     * @return The user's input line.
     */
    public String readCommand() {
        return scanner.nextLine();
    }
}
