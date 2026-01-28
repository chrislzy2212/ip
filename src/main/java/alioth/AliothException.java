package alioth;

/**
 * Represents an error caused by invalid user input.
 */
public class AliothException extends Exception {

    /**
     * Creates an exception with the given message.
     *
     * @param message Error message to show to the user.
     */
    public AliothException(String message) {
        super(message);
    }
}
