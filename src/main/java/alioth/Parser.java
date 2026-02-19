package alioth;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import alioth.command.AddDeadlineCommand;
import alioth.command.AddEventCommand;
import alioth.command.AddTodoCommand;
import alioth.command.AliasCommand;
import alioth.command.Command;
import alioth.command.DeleteCommand;
import alioth.command.ExitCommand;
import alioth.command.FindCommand;
import alioth.command.ListAliasesCommand;
import alioth.command.ListCommand;
import alioth.command.MarkCommand;
import alioth.command.UnaliasCommand;
import alioth.command.UnmarkCommand;
import alioth.storage.Storage;
import alioth.task.TaskList;

/**
 * Parses user input into command objects.
 */
public class Parser {

    private static final Set<String> COMMAND_WORDS = Set.of(
            "bye", "list", "find", "mark", "unmark", "delete",
            "todo", "deadline", "event",
            "alias", "unalias", "aliases"
    );

    private static final Map<String, String> ALIASES = new HashMap<>();
    private static Storage storage; // for alias persistence

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {}

    /**
     * Sets the storage used to load/save aliases.
     *
     * @param storage Storage instance.
     */
    public static void setStorage(Storage storage) {
        assert storage != null : "Parser storage should not be null";
        Parser.storage = storage;
    }

    /**
     * Loads aliases from storage into memory.
     *
     * @throws AliothException If loading aliases fails.
     */
    public static void loadAliases() throws AliothException {
        if (storage == null) {
            return;
        }

        Map<String, String> loaded = storage.loadAliases();
        ALIASES.clear();
        ALIASES.putAll(loaded);
    }

    /**
     * Saves current aliases to storage.
     *
     * @throws AliothException If saving aliases fails.
     */
    private static void saveAliases() throws AliothException {
        if (storage == null) {
            return;
        }

        storage.saveAliases(getAliases());
    }

    /**
     * Parses the user input into a Command object.
     *
     * @param input Full user input.
     * @return The corresponding Command.
     * @throws AliothException If the input is invalid or the command is unknown.
     */
    public static Command parse(String input) throws AliothException {
        assert input != null : "Parser.parse input should not be null";

        if (!input.equals(input.stripLeading())) {
            throw new AliothException(Message.LEADING_SPACES.getText());
        }

        String commandWord = getCommandWord(input);
        String args = getCommandArgs(input);

        commandWord = resolveAlias(commandWord);

        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "find":
            return new FindCommand(args);
        case "mark":
            return new MarkCommand(args);
        case "unmark":
            return new UnmarkCommand(args);
        case "delete":
            return new DeleteCommand(args);
        case "todo":
            return new AddTodoCommand(args);
        case "deadline":
            return new AddDeadlineCommand(args);
        case "event":
            return new AddEventCommand(args);

        case "alias":
            return new AliasCommand(args);
        case "unalias":
            return new UnaliasCommand(args);
        case "aliases":
            return new ListAliasesCommand();

        default:
            throw new AliothException(Message.UNKNOWN_COMMAND.getText());
        }
    }

    private static String resolveAlias(String commandWord) {
        return ALIASES.getOrDefault(commandWord, commandWord);
    }

    private static boolean isReservedCommandWord(String word) {
        return COMMAND_WORDS.contains(word);
    }

    private static boolean isValidAliasWord(String word) {
        return word.matches("[a-zA-Z]+");
    }

    /**
     * Adds an alias that maps {@code aliasWord} to {@code targetCommandWord}.
     *
     * @param aliasWord The alias to add.
     * @param targetCommandWord The existing command word the alias maps to.
     * @throws AliothException If the alias is invalid, clashes with a command word,
     *                         already exists, or the target command is unknown.
     */
    public static void addAlias(String aliasWord, String targetCommandWord) throws AliothException {
        String alias = aliasWord.trim();
        String target = targetCommandWord.trim();

        if (alias.isEmpty() || target.isEmpty()) {
            throw new AliothException(Message.INVALID_ALIAS.getText());
        }

        if (!isValidAliasWord(alias) || !isValidAliasWord(target)) {
            throw new AliothException(Message.INVALID_ALIAS_WORD.getText());
        }

        if (isReservedCommandWord(alias)) {
            throw new AliothException(Message.ALIAS_IS_COMMAND_WORD.format(alias));
        }

        if (ALIASES.containsKey(alias)) {
            throw new AliothException(Message.ALIAS_ALREADY_EXISTS.format(alias));
        }

        if (!COMMAND_WORDS.contains(target)) {
            throw new AliothException(Message.UNKNOWN_ALIAS_TARGET.format(target));
        }

        ALIASES.put(alias, target);
        saveAliases();
    }

    /**
     * Removes an existing alias.
     *
     * @param aliasWord The alias to remove.
     * @throws AliothException If the alias does not exist.
     */
    public static void removeAlias(String aliasWord) throws AliothException {
        String alias = aliasWord.trim();

        if (alias.isEmpty()) {
            throw new AliothException(Message.INVALID_UNALIAS.getText());
        }

        if (!ALIASES.containsKey(alias)) {
            throw new AliothException(Message.NO_SUCH_ALIAS.format(alias));
        }

        ALIASES.remove(alias);
        saveAliases();
    }

    /**
     * Returns an unmodifiable snapshot of current aliases.
     *
     * @return A copy of the alias map.
     */
    public static Map<String, String> getAliases() {
        return Map.copyOf(ALIASES);
    }

    /**
     * Returns the first word of the input as the command word.
     *
     * @param input User input string.
     * @return The command word, or an empty string if the input is blank.
     */
    public static String getCommandWord(String input) {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String[] parts = trimmed.split(" ", 2);
        return parts[0];
    }

    /**
     * Returns the remaining part of the input after the command word as command arguments.
     *
     * @param input User input string.
     * @return The command arguments, or an empty string if there are no arguments / input is blank.
     */
    public static String getCommandArgs(String input) {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String[] parts = trimmed.split(" ", 2);
        if (parts.length < 2) {
            return "";
        }
        return parts[1];
    }

    /**
     * Parses an integer task number from the given arguments string.
     *
     * @param args The argument string expected to contain the task number.
     * @param commandWord The command word used (for forming the error message).
     * @return The parsed task number.
     * @throws AliothException If the task number is missing or not a valid integer.
     */
    public static int parseTaskNumber(String args, String commandWord) throws AliothException {
        Message invalidMessage = Message.invalidIndexCommand(commandWord);

        if (args.trim().isEmpty()) {
            throw new AliothException(invalidMessage.getText());
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            throw new AliothException(invalidMessage.getText());
        }

        return taskNumber;
    }

    /**
     * Parses and validates a 1-based task index from the given arguments.
     *
     * Ensures the parsed task number refers to an existing task in the
     * provided {@code TaskList}, then converts it to a 0-based index for internal use.
     *
     * @param tasks The task list used to validate the index range.
     * @param args The argument string expected to contain the task number.
     * @param commandWord The command word (used to construct error messages).
     * @return The validated 0-based task index.
     * @throws AliothException If the task number is missing, not an integer,
     *                         or outside the valid range.
     */
    public static int parseTaskIndex(TaskList tasks, String args, String commandWord) throws AliothException {
        int taskNumber = parseTaskNumber(args, commandWord);

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AliothException(Message.invalidIndexCommand(commandWord).getText());
        }

        return taskNumber - 1;
    }
}
