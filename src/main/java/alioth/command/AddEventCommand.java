package alioth.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import alioth.AliothException;
import alioth.Message;
import alioth.storage.Storage;
import alioth.task.Event;
import alioth.task.Task;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Adds an event task.
 */
public class AddEventCommand extends Command {
    private static final DateTimeFormatter EVENT_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final String args;

    /**
     * Creates an add event command with the given arguments.
     *
     * @param args User input arguments (expected: description /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm).
     */
    public AddEventCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        if (args.trim().isEmpty()) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        String[] firstSplit = args.split(" /from ", 2);
        if (firstSplit.length != 2) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        String description = firstSplit[0].trim();
        String[] secondSplit = firstSplit[1].split(" /to ", 2);
        if (secondSplit.length != 2) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        String fromString = secondSplit[0].trim();
        String toString = secondSplit[1].trim();
        if (description.isEmpty() || fromString.isEmpty() || toString.isEmpty()) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(fromString, EVENT_INPUT_FORMAT);
            to = LocalDateTime.parse(toString, EVENT_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new AliothException(Message.INVALID_EVENT.getText());
        }

        Task task = new Event(description, from, to);
        tasks.add(task);

        ui.showAddTask(task, tasks.size());
        storage.save(tasks.asList());
    }
}
