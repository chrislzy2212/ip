package alioth;

/**
 * Error messages using a sweet princess personality with direct command examples.
 */
public enum Message {
    LEADING_SPACES("Oh dear! A princess loves a tidy room. "
            + "Please do not start your command with any spaces!"),
    UNKNOWN_COMMAND("Oh my! I have never heard of that command in all the kingdom. "
            + "Could you please try a different word?"),
    INVALID_TODO("A princess needs a plan! Please tell me what you want to do. "
            + "Use: todo <description>"),
    INVALID_DEADLINE("Oh no! We must not be late for the royal ball! "
            + "Please use: deadline <desc> /by yyyy-MM-dd"),
    INVALID_EVENT("We are having a party! Please tell me when it starts and ends using: "
            + "event <desc> /from <time> /to <time>"),
    EVENT_CHRONOLOGY("How sad! Time is a fickle thing. "
            + "You cannot start a party after it has already ended, darling."),
    INVALID_MARK("Wait! I can only mark a task as done if you give me its royal number. "
            + "Try: mark <number>"),
    INVALID_UNMARK("Oh! Did you change your mind? "
            + "Please use the task number so I can unmark it: unmark <number>"),
    INVALID_DELETE("Goodbye, task! But I need to know which number to remove first. "
            + "Try: delete <number>"),
    INVALID_FIND("I can find anything! But you must give me a word to look for, like: "
            + "find <keyword>"),

    // Aliases
    INVALID_ALIAS("A new secret name! How exciting! Please use: alias <alias> <command>"),
    INVALID_UNALIAS("The magic is gone! To stop the shortcut, use: unalias <alias>"),
    INVALID_ALIAS_WORD("Oh! Just around the riverbend, we only use letters for our aliases."),
    ALIAS_ALREADY_EXISTS("Oh! This secret name is already used in the royal records: %s"),
    NO_SUCH_ALIAS("I looked everywhere in the palace, but I cannot find that alias: %s"),
    ALIAS_IS_COMMAND_WORD("%s is a royal word already. You cannot change a king's command!"),
    UNKNOWN_ALIAS_TARGET("Heigh-ho! I don't know the command for: %s"),

    DUPLICATE("Oh heavens! This task already exists in your royal records."),
    SAVE_ERROR("Oh no! The magic mirror has clouded over. I could not save your tasks.");

    private final String text;

    Message(String text) {
        this.text = text;
    }

    /**
     * Returns the raw message text.
     *
     * @return The message string.
     */
    public String getText() {
        return text;
    }

    /**
     * Formats the message with the provided arguments.
     *
     * @param args Formatting arguments.
     * @return The formatted string.
     */
    public String format(Object... args) {
        return String.format(text, args);
    }

    /**
     * Returns the corresponding invalid-index message for the given command word.
     *
     * @param commandWord The command word being used (e.g., mark, delete).
     * @return The corresponding Message or UNKNOWN_COMMAND if no match is found.
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
}
