package alioth;

/**
 * Represents user-facing messages used by the application.
 */
public enum Message {
    LEADING_SPACES("OOPS!!! Command should not start with space(s)."),
    UNKNOWN_COMMAND("OOPS!!! I'm sorry, but I don't know what that means :-("),

    INVALID_TODO("OOPS!!! Invalid todo format. Use: todo <description>"),
    INVALID_DEADLINE("OOPS!!! Invalid deadline format. Use: deadline <desc> /by yyyy-MM-dd"),
    INVALID_EVENT("OOPS!!! Invalid event format. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"),

    INVALID_MARK("OOPS!!! Invalid mark format. Use: mark <task number>"),
    INVALID_UNMARK("OOPS!!! Invalid unmark format. Use: unmark <task number>"),
    INVALID_DELETE("OOPS!!! Invalid delete format. Use: delete <task number>"),

    SAVE_ERROR("OOPS!!! Could not save tasks to the save file.");

    private final String text;

    Message(String text) {
        this.text = text;
    }

    /**
     * Returns the message text.
     *
     * @return The message text.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the corresponding invalid-index message for the given command word.
     *
     * @param commandWord The command word (e.g., "mark", "unmark", "delete").
     * @return The message corresponding to the command word, or UNKNOWN_COMMAND if none matches.
     */
    public static Message invalidIndexCommand(String commandWord) {
        switch (commandWord) {
        case "mark":
            return INVALID_MARK;
        case "unmark":
            return INVALID_UNMARK;
        case "delete":
            return INVALID_DELETE;
        default:
            return UNKNOWN_COMMAND; // fallback; shouldn't happen if you use it correctly
        }
    }
}
