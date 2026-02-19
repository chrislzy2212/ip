package alioth;

import java.nio.file.Path;

import alioth.command.Command;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

/**
 * A chatbot that can store tasks of different types and display them on request.
 */
public class Alioth {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates an instance of the chatbot using the given file path to load and save tasks.
     *
     * @param filePath File path for the storage file.
     */
    public Alioth(Path filePath) {
        assert filePath != null : "Storage path should not be null";

        ui = new Ui();
        storage = new Storage(filePath);

        // Alias persistence setup
        Parser.setStorage(storage);
        try {
            Parser.loadAliases();
        } catch (AliothException e) {
            ui.showError(e.getMessage());
        }

        TaskList tempTasks;
        try {
            tempTasks = new TaskList(storage.load());
        } catch (AliothException e) {
            ui.showError(e.getMessage());
            tempTasks = new TaskList();
        }
        tasks = tempTasks;
    }

    /**
     * Returns the welcome message for display in the GUI.
     *
     * @return Welcome message.
     */
    public String getWelcomeMessage() {
        ui.showWelcome();
        return ui.consumeOutput();
    }

    /**
     * Returns Alioth's response to the user's input.
     * This is used by the GUI to get a reply string to display.
     *
     * @param input Full user input.
     * @return The response message to display.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parse(input);
            command.execute(tasks, ui, storage);
            return ui.consumeOutput();
        } catch (AliothException e) {
            ui.clearOutput();
            return ui.formatError(e.getMessage());
        }
    }
}
