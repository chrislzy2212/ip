package alioth.command;

import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Lists all tasks.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks.asList());
    }
}
