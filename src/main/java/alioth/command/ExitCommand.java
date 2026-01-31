package alioth.command;

import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Exits the application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
