package alioth;

public class Parser {

    public static String getCommandWord(String input) {
        String trimmed = input.trim();
        String[] parts = trimmed.split(" ", 2);
        return parts[0];
    }

    public static String getCommandArgs(String input) {
        String trimmed = input.trim();
        String[] parts = trimmed.split(" ", 2);
        if (parts.length < 2) {
            return "";
        }
        return parts[1];
    }

    public static int parseTaskNumber(String args, String commandWord) throws AliothException {
        if (args.trim().isEmpty()) {
            throw new AliothException("OOPS!!! Invalid " + commandWord + " format.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            throw new AliothException("OOPS!!! Invalid " + commandWord + " format.");
        }

        return taskNumber;
    }
}
