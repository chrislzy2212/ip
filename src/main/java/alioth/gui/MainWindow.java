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

    private Image userImage;
    private Image aliothImage;

    /**
     * Initializes the UI components after the FXML file has been loaded.
     * Binds the scroll pane to auto-scroll as new dialogs are added, and loads
     * the display images for the user and Alioth.
     */
    @FXML
    public void initialize() {
        userImage = new Image(getClass().getResourceAsStream("/images/Vanellope.png"));
        aliothImage = new Image(getClass().getResourceAsStream("/images/Sofia.png"));
    }

    /**
     * Injects the Alioth instance.
     *
     * @param a Alioth instance to use.
     */
    public void setAlioth(Alioth a) {
        alioth = a;
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

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getAliothDialog(response, aliothImage)
        );

        scrollPane.setVvalue(1.0);

        userInput.clear();
    }
}
