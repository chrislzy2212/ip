package alioth.command;

import alioth.AliothException;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Finds tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    private final String args;

    /**
     * Creates a find command with the given arguments.
     *
     * @param args User input arguments (expected: keyword).
     */
    public FindCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        String keyword = args.trim();
        if (keyword.isEmpty()) {
            throw new AliothException("OOPS!!! The keyword cannot be empty.");
        }

        ui.showMatchingTasks(tasks.find(keyword));
    }
}
