package alioth.command;

import alioth.*;
import alioth.storage.Storage;
import alioth.task.Task;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Unmarks a task in the list as not done.
 */
public class UnmarkCommand extends Command {
    private final String args;

    /**
     * Creates an unmark command with the given arguments.
     *
     * @param args User input arguments (expected: task number).
     */
    public UnmarkCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        int index = parseTaskIndex(tasks, args, "unmark");
        Task task = tasks.get(index);
        task.setDone(false);

        ui.showUnmarkTask(task);
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
