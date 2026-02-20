package alioth.command;

import alioth.exception.AliothException;
import alioth.parser.Parser;
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
        int index = Parser.parseTaskIndex(tasks, args, "delete");
        Task removed = tasks.remove(index);

        ui.showDeleteTask(removed, tasks.size());
        storage.save(tasks.asList());
    }
}
