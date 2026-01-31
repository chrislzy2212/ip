package alioth;

/**
 * Parses user input into command word, arguments, and task numbers.
 */
public class Parser {

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
     * Parses the task number from the given arguments string.
     *
     * @param args The arguments string that should contain the task number.
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
