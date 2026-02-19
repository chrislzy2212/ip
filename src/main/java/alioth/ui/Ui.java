package alioth.ui;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import alioth.task.Task;

/**
 * Handles user interaction (input and output) for the chatbot.
 * with a sweet and dramatic princess personality.
 */
public class Ui {
    /** Simplified separator to prevent encoding issues in the GUI. */
    private static final String LINE = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

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
     * Prints the royal welcome message.
     */
    public void showWelcome() {
        addBlock("Salutations! I am Alioth, your royal helper!",
                "What is your heart's wish for our list today?");
    }

    /**
     * Prints the goodbye message.
     */
    public void showBye() {
        addBlock("Goodbye for now! I hope all your dreams come true!",
                "Come back to the castle soon!");
    }

    /**
     * Prints an error message with princess flair.
     */
    public void showError(String message) {
        addBlock("Oh heavens! A little bit of trouble:",
                message,
                "Don't worry, we can fix it together!");
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
        showTaskList("Checking royal records... Here are the tasks:", tasks);
    }

    /**
     * Shows tasks that match the given keyword.
     */
    public void showMatchingTasks(List<Task> tasks) {
        showTaskList("I found these matching tasks in the garden:", tasks);
    }

    /**
     * Prints confirmation after adding a task.
     *
     * @param task Task that was added.
     * @param size Current number of tasks in the list.
     */
    public void showAddTask(Task task, int size) {
        showLine();
        addLine("Whistle while you work! I have added this task:");
        addLine("  " + task);
        addLine("Now your kingdom has " + size + " tasks to complete!");
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
        addLine("Let it go! I have removed this task for you:");
        addLine("  " + task);
        addLine("Your path is clearer now with " + size + " tasks left.");
        showLine();
    }

    /**
     * Prints confirmation after marking a task as done.
     *
     * @param task Task that was marked.
     */
    public void showMarkTask(Task task) {
        showLine();
        addLine("Hooray! A dream come true! I marked this as done:");
        addLine("  " + task);
        addLine("You are doing such a wonderful job!");
        showLine();
    }

    /**
     * Prints confirmation after unmarking a task.
     *
     * @param task Task that was unmarked.
     */
    public void showUnmarkTask(Task task) {
        showLine();
        addLine("Oh! I have marked this task as not done yet:");
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
        showMessage("A new secret name! " + aliasWord + " is now "
                + targetCommandWord + ". How exciting!");
    }

    /**
     * Shows a confirmation message after removing an alias.
     *
     * @param aliasWord Alias word removed by the user.
     */
    public void showAliasRemoved(String aliasWord) {
        showMessage("The magic is gone! " + aliasWord + " is no longer a secret name.");
    }

    /**
     * Shows all current aliases.
     *
     * @param aliases Map of aliases to target command words.
     */
    public void showAliases(Map<String, String> aliases) {
        showLine();
        if (aliases.isEmpty()) {
            addLine("The secret scroll is empty. No aliases defined!");
        } else {
            addLine("Royal Secret Names:");
            for (Map.Entry<String, String> entry : aliases.entrySet()) {
                addLine("  * " + entry.getKey() + " -> " + entry.getValue());
            }
        }
        showLine();
    }
}
