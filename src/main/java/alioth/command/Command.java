package alioth.command;

import alioth.AliothException;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Represents an executable user command.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @param tasks Task list to operate on.
     * @param ui UI to show messages.
     * @param storage Storage to load/save tasks.
     * @throws AliothException If execution fails.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException;

    /**
     * Indicates whether this command causes the program to exit.
     *
     * @return True if the program should exit.
     */
    public boolean isExit() {
        return false;
    }
}
