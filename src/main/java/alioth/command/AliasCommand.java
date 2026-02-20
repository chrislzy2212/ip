package alioth.command;

import alioth.exception.AliothException;
import alioth.message.Message;
import alioth.parser.Parser;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Adds a user-defined alias for an existing command word.
 */
public class AliasCommand extends Command {
    private final String args;

    /**
     * Creates an alias command with the given arguments.
     *
     * @param args User input arguments (expected: aliasWord commandWord).
     */
    public AliasCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new AliothException(Message.INVALID_ALIAS.getText());
        }

        String[] parts = trimmed.split(" ", 2);
        if (parts.length != 2) {
            throw new AliothException(Message.INVALID_ALIAS.getText());
        }

        String aliasWord = parts[0].trim();
        String targetWord = parts[1].trim();

        Parser.addAlias(aliasWord, targetWord);
        ui.showAliasAdded(aliasWord, targetWord);
    }
}
