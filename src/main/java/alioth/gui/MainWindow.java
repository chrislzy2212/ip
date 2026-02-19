package alioth.gui;

import alioth.Alioth;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Alioth alioth;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/Vanellope.png"));
    private Image aliothImage = new Image(this.getClass().getResourceAsStream("/images/Sofia.png"));

    /**
     * Initializes the UI components after the FXML file has been loaded.
     * Binds the scroll pane to auto-scroll as new dialogs are added, and loads
     * the display images for the user and Alioth.
     */
    @FXML
    public void initialize() {
        assert scrollPane != null && dialogContainer != null
                && userInput != null && sendButton != null
                : "FXML injection failed in MainWindow";

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Alioth instance.
     *
     * @param a Alioth instance to use.
     */
    public void setAlioth(Alioth a) {
        alioth = a;

        String welcome = alioth.getWelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getAliothDialog(welcome, aliothImage)
        );
    }

    /**
     * Handles user input by creating dialog boxes and appending them.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            userInput.clear();
            return;
        }

        String response = alioth.getResponse(input);
        boolean isError = response.contains("OOPS!!!"); // Simple check for error highlighting

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getAliothDialog(response, aliothImage, isError)
        );

        userInput.clear();
    }
}
