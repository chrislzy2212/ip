package alioth;

/**
 * Adds a todo task.
 */
public class AddTodoCommand extends Command {
    private final String args;

    public AddTodoCommand(String args) {
        this.args = args;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AliothException {
        String description = args.trim();
        if (description.isEmpty()) {
            throw new AliothException(Message.INVALID_TODO.getText());
        }

        Task task = new Todo(description);
        tasks.add(task);

        ui.showAddTask(task, tasks.size());
        storage.save(tasks.asList());
    }
}
