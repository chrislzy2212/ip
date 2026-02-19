package alioth;

/**
 * Represents error messages used by the application.
 */
public enum Message {
    LEADING_SPACES("OOPS!!! Command should not start with space(s)."),
    UNKNOWN_COMMAND("OOPS!!! I'm sorry, but I don't know what that means :-("),

    INVALID_TODO("OOPS!!! Invalid todo format. Use: todo <description>"),
    INVALID_DEADLINE("OOPS!!! Invalid deadline format. Use: deadline <desc> /by yyyy-MM-dd"),
    INVALID_EVENT("OOPS!!! Invalid event format. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"),
    EVENT_CHRONOLOGY("OOPS!!! The start time (/from) must be before the end time (/to)."),

    INVALID_MARK("OOPS!!! Invalid mark format. Use: mark <task number>"),
    INVALID_UNMARK("OOPS!!! Invalid unmark format. Use: unmark <task number>"),
    INVALID_DELETE("OOPS!!! Invalid delete format. Use: delete <task number>"),
    INVALID_FIND("OOPS!!! Invalid find format. Use: find <keyword>"),

    // Aliases
    INVALID_ALIAS("OOPS!!! Invalid alias format. Use: alias <alias> <command>"),
    INVALID_UNALIAS("OOPS!!! Invalid unalias format. Use: unalias <alias>"),
    INVALID_ALIAS_WORD("OOPS!!! Alias and command must be letters only."),
    ALIAS_ALREADY_EXISTS("OOPS!!! Alias already exists: %s"),
    NO_SUCH_ALIAS("OOPS!!! No such alias: %s"),
    ALIAS_IS_COMMAND_WORD("OOPS!!! '%s' is an existing command word."),
    UNKNOWN_ALIAS_TARGET("OOPS!!! Unknown command: %s"),

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
     * Returns formatted message text.
     *
     * @param args Formatting arguments.
     * @return Formatted message text.
     */
    public String format(Object... args) {
        return String.format(text, args);
    }

    /**
     * Returns the corresponding invalid-index message for the given command word.
     *
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
            return UNKNOWN_COMMAND;
        }
    }

    /**
     * Returns an unknown-command message for a specific command word.
     *
     * @param commandWord The unknown command word.
     * @return A formatted unknown-command message.
     */
    public static String unknownCommand(String commandWord) {
        return "OOPS!!! Unknown command: " + commandWord;
    }
}
