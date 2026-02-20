package alioth.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import alioth.message.Message;

/**
 * Tests for the AliothException class to ensure error messages are preserved.
 */
public class AliothExceptionTest {

    /**
     * Tests that the exception correctly stores and returns the provided message.
     */
    @Test
    public void constructor_validMessage_messageStoredCorrectly() {
        String errorMessage = "Oh heavens! A little bit of trouble.";
        AliothException exception = new AliothException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests that the exception works correctly when integrated with the Message enum.
     */
    @Test
    public void constructor_enumMessage_messageStoredCorrectly() {
        String expectedMessage = Message.UNKNOWN_COMMAND.getText();
        AliothException exception = new AliothException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
