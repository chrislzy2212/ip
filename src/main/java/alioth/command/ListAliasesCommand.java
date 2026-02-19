package alioth.command;

import java.util.Map;

import alioth.Parser;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Lists all user-defined aliases.
 */
public class ListAliasesCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Map<String, String> aliases = Parser.getAliases();
        ui.showAliases(aliases);
    }
}
