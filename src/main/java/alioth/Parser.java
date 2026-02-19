package alioth;

import alioth.command.AddDeadlineCommand;
import alioth.command.AddEventCommand;
import alioth.command.AddTodoCommand;
import alioth.command.Command;
import alioth.command.DeleteCommand;
import alioth.command.ExitCommand;
import alioth.command.FindCommand;
import alioth.command.ListCommand;
import alioth.command.MarkCommand;
import alioth.command.UnmarkCommand;

/**
 * Parses user input into command objects.
 */
public class Parser {

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {}

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
        default:
            throw new AliothException(Message.UNKNOWN_COMMAND.getText());
        }
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
}
