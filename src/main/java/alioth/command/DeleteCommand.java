package alioth.command;

import alioth.AliothException;
import alioth.Message;
import alioth.Parser;
import alioth.storage.Storage;
import alioth.task.Task;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Deletes a task from the list.
 */
public class DeleteCommand extends Command {
    private final String args;

    /**
     * Creates a delete command with the given arguments.
     *
     * @param args User input arguments (expected: task number).
     */
    public DeleteCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        int index = parseTaskIndex(tasks, args, "delete");
        Task removed = tasks.remove(index);

        ui.showDeleteTask(removed, tasks.size());
        storage.save(tasks.asList());
    }

    private int parseTaskIndex(TaskList tasks, String args, String commandWord) throws AliothException {
        int taskNumber = Parser.parseTaskNumber(args, commandWord);

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AliothException(Message.invalidIndexCommand(commandWord).getText());
        }

        return taskNumber - 1;
    }
}
