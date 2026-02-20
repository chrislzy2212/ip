package alioth.command;

import alioth.exception.AliothException;
import alioth.parser.Parser;
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
        int index = Parser.parseTaskIndex(tasks, args, "mark");

        Task task = tasks.get(index);
        task.setDone(true);

        ui.showMarkTask(task);
        storage.save(tasks.asList());
    }
}
