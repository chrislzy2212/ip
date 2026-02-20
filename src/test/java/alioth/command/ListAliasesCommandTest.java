package alioth.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alioth.exception.AliothException;
import alioth.parser.Parser;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * Tests for ListAliasesCommand to verify alias retrieval and display logic.
 */
public class ListAliasesCommandTest {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() throws AliothException {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(Paths.get("data", "test-aliases-list.txt"));
        Parser.setStorage(storage);

        // Clear any existing aliases in the static Parser before each test
        Parser.removeAlias("ls");
    }

    /**
     * Tests that the command correctly displays defined aliases.
     */
    @Test
    public void execute_withAliases_showsInUi() throws AliothException {
        Parser.addAlias("ls", "list");
        ListAliasesCommand command = new ListAliasesCommand();
        command.execute(tasks, ui, storage);

        String output = ui.consumeOutput();
        assertTrue(output.contains("Royal Secret Names:"));
        assertTrue(output.contains("ls -> list"));
    }

    /**
     * Tests that the command handles an empty alias map gracefully.
     */
    @Test
    public void execute_noAliases_showsEmptyMessage() {
        ListAliasesCommand command = new ListAliasesCommand();
        command.execute(tasks, ui, storage);

        String output = ui.consumeOutput();
        assertTrue(output.contains("The secret scroll is empty"));
    }
}
