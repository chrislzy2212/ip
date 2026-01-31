package alioth;

import alioth.command.Command;
import alioth.storage.Storage;
import alioth.task.TaskList;
import alioth.ui.Ui;

import java.nio.file.Paths;

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
    public Alioth(String filePath) {
        ui = new Ui();
        storage = new Storage(Paths.get(filePath));

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
     * Starts the chatbot.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        new Alioth("data/duke.txt").run();
    }

    /**
     * Runs the chatbot until the user exits.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (AliothException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
