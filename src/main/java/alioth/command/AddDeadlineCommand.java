package alioth.command;

import alioth.*;
import alioth.storage.Storage;
import alioth.task.Deadline;
import alioth.task.Task;
import alioth.task.TaskList;
import alioth.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Adds a deadline task.
 */
public class AddDeadlineCommand extends Command {
    private final String args;

    /**
     * Creates an add deadline command with the given arguments.
     *
     * @param args User input arguments (expected: <desc> /by yyyy-MM-dd).
     */
    public AddDeadlineCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
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

        Task task = new Deadline(description, byDate);
        tasks.add(task);

        ui.showAddTask(task, tasks.size());
        storage.save(tasks.asList());
    }
}
