package alioth.ui;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import alioth.task.Task;

/**
 * Handles user interaction (input and output) for the chatbot.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    private final Scanner scanner;
    private final StringBuilder output;

    /**
     * Creates a UI that reads input from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
        output = new StringBuilder();
    }

    /**
     * Reads the next line of user input.
     *
     * @return The user's input line.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Appends a line to the output buffer.
     *
     * @param text Line to add.
     */
    private void addLine(String text) {
        output.append(text).append(System.lineSeparator());
    }

    /**
     * Appends multiple lines to the output buffer.
     *
     * @param lines Lines to add.
     */
    private void addLines(String... lines) {
        for (String line : lines) {
            addLine(line);
        }
    }

    /**
     * Appends a standard output block wrapped by separator lines.
     *
     * @param lines Content lines inside the block.
     */
    private void addBlock(String... lines) {
        showLine();
        addLines(lines);
        showLine();
    }

    /**
     * Returns the buffered output and clears it.
     *
     * @return Output content since the last consume.
     */
    public String consumeOutput() {
        String text = output.toString().stripTrailing();
        output.setLength(0);
        return text;
    }

    /**
     * Clears the current output buffer without returning the text.
     */
    public void clearOutput() {
        output.setLength(0);
    }

    /**
     * Formats an error message in a standard UI block.
     *
     * @param message Error message.
     * @return A formatted error block as a string.
     */
    public String formatError(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append(System.lineSeparator());
        sb.append(message).append(System.lineSeparator());
        sb.append(LINE);
        return sb.toString();
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        addLine(LINE);
    }

    /**
     * Prints the welcome message.
     */
    public void showWelcome() {
        addBlock("Hello! I'm Alioth", "What can I do for you?");
    }

    /**
     * Prints the goodbye message.
     */
    public void showBye() {
        addBlock("Bye. Hope to see you again soon!");
    }

    /**
     * Prints an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        addBlock(message);
    }

    /**
     * Prints a normal informational message.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        addBlock(message);
    }

    /**
     * Prints tasks in a standard numbered list block.
     *
     * @param header Header text to show above the list.
     * @param tasks Tasks to display.
     */
    private void showTaskList(String header, List<Task> tasks) {
        showLine();
        addLine(header);
        for (int i = 0; i < tasks.size(); i++) {
            addLine((i + 1) + ". " + tasks.get(i));
        }
        showLine();
    }

    /**
     * Prints all tasks in the provided list.
     *
     * @param tasks Tasks to display.
     */
    public void showTasks(List<Task> tasks) {
        showTaskList("Here are the tasks in your list:", tasks);
    }

    /**
     * Shows tasks that match the given keyword.
     *
     * @param tasks Matching tasks to display.
     */
    public void showMatchingTasks(List<Task> tasks) {
        showTaskList("Here are the matching tasks in your list:", tasks);
    }

    /**
     * Prints confirmation after adding a task.
     *
     * @param task Task that was added.
     * @param size Current number of tasks in the list.
     */
    public void showAddTask(Task task, int size) {
        showLine();
        addLine("Got it. I've added this task:");
        addLine("  " + task);
        addLine("Now you have " + size + " tasks in the list.");
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
        addLine("Noted. I've removed this task:");
        addLine("  " + task);
        addLine("Now you have " + size + " tasks in the list.");
        showLine();
    }

    /**
     * Prints confirmation after marking a task as done.
     *
     * @param task Task that was marked.
     */
    public void showMarkTask(Task task) {
        showLine();
        addLine("Nice! I've marked this task as done:");
        addLine("  " + task);
        showLine();
    }

    /**
     * Prints confirmation after unmarking a task as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showUnmarkTask(Task task) {
        showLine();
        addLine("OK, I've marked this task as not done yet:");
        addLine("  " + task);
        showLine();
    }

    /**
     * Shows a confirmation message after adding an alias.
     *
     * @param aliasWord Alias word added by the user.
     * @param targetCommandWord The command word the alias maps to.
     */
    public void showAliasAdded(String aliasWord, String targetCommandWord) {
        showMessage("Alias added: " + aliasWord + " -> " + targetCommandWord);
    }

    /**
     * Shows a confirmation message after removing an alias.
     *
     * @param aliasWord Alias word removed by the user.
     */
    public void showAliasRemoved(String aliasWord) {
        showMessage("Alias removed: " + aliasWord);
    }

    /**
     * Shows all current aliases.
     *
     * @param aliases Map of aliases to target command words.
     */
    public void showAliases(Map<String, String> aliases) {
        showLine();
        if (aliases.isEmpty()) {
            addLine("No aliases defined.");
        } else {
            addLine("Aliases:");
            for (Map.Entry<String, String> entry : aliases.entrySet()) {
                addLine("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }
        showLine();
    }
}
