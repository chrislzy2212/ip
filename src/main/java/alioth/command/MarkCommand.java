package alioth.command;

import alioth.*;
import alioth.storage.Storage;
import alioth.task.Task;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Marks a task as done.
 */
public class MarkCommand extends Command {
    private final String args;

    public MarkCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        int index = parseTaskIndex(tasks, args, "mark");

        Task task = tasks.get(index);
        task.setDone(true);

        ui.showMarkTask(task);
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
