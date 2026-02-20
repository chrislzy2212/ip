package alioth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Integration tests for the Alioth chatbot.
 */
public class AliothTest {

    @TempDir
    Path tempDir;

    private Path testPath;
    private Alioth alioth;

    @BeforeEach
    public void setUp() {
        testPath = tempDir.resolve("alioth-test.txt");
        alioth = new Alioth(testPath);
    }

    /**
     * Tests that the welcome message is correctly retrieved for the GUI.
     */
    @Test
    public void getWelcomeMessage_returnsCorrectText() {
        String welcome = alioth.getWelcomeMessage();
        assertTrue(welcome.contains("Salutations! I am Alioth"));
    }

    /**
     * Tests a full interaction cycle: input -> command -> response.
     */
    @Test
    public void getResponse_validInput_returnsSuccessMessage() {
        String response = alioth.getResponse("todo Bake royal cake");

        assertTrue(response.contains("Whistle while you work!"));
        assertTrue(response.contains("[T][ ] Bake royal cake"));
    }

    /**
     * Tests that errors are caught and formatted correctly for the GUI.
     */
    @Test
    public void getResponse_invalidInput_returnsFormattedError() {
        // Leading spaces are forbidden in Alioth's kingdom
        String response = alioth.getResponse("  todo invalid");

        assertTrue(response.contains("Oh dear! A princess loves a tidy room."));
        assertTrue(response.contains("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
    }
}
