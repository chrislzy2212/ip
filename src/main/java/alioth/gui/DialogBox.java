package alioth.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Private constructor to create a DialogBox.
     * Loads the FXML layout and sets the text and image.
     *
     * @param text The text to be displayed in the dialog.
     * @param img The image to be displayed as the profile picture.
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
            assert dialog != null && displayPicture != null
                    : "FXML injection failed in DialogBox";
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box so that the ImageView is on the left and text is on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a dialog box for the user.
     *
     * @param text The user's input text.
     * @param img The user's profile image.
     * @return A DialogBox containing the user's text and image.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.dialog.getStyleClass().add("user-label"); // Add this line
        return db;
    }

    /**
     * Creates a dialog box for Alioth with default styling.
     *
     * @param text Alioth's response text.
     * @param img Alioth's profile image.
     * @return A DialogBox containing Alioth's text and image, flipped.
     */
    public static DialogBox getAliothDialog(String text, Image img) {
        return getAliothDialog(text, img, false);
    }

    /**
     * Creates a dialog box for Alioth with an option to apply error styling.
     *
     * @param text Alioth's response text.
     * @param img Alioth's profile image.
     * @param isError True if the message is an error and should be styled accordingly.
     * @return A DialogBox containing Alioth's text and image, flipped.
     */
    public static DialogBox getAliothDialog(String text, Image img, boolean isError) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        db.dialog.getStyleClass().add("alioth-label"); // Add this line
        if (isError) {
            db.displayAsError();
        }
        return db;
    }

    /**
     * Changes the styling of the dialog label to indicate an error.
     * Adds the error-label style class to the label's style list.
     */
    private void displayAsError() {
        dialog.getStyleClass().add("error-label");
    }

    /**
     * Clips the profile picture into a circle for a magical, polished look.
     */
    private void clipToCircle() {
        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(49.5, 49.5, 49.5);
        displayPicture.setClip(clip);
    }
}
