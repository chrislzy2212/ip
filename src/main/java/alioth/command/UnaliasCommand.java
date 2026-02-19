package alioth.command;

import alioth.AliothException;
import alioth.Message;
import alioth.Parser;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Removes a user-defined alias.
 */
public class UnaliasCommand extends Command {
    private final String args;

    /**
     * Creates an unalias command with the given arguments.
     *
     * @param args User input arguments.
     */
    public UnaliasCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        String aliasWord = args.trim();
        if (aliasWord.isEmpty()) {
            throw new AliothException(Message.INVALID_UNALIAS.getText());
        }

        Parser.removeAlias(aliasWord);
        ui.showAliasRemoved(aliasWord);
    }
}
