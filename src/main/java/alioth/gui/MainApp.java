package alioth.gui;

import java.io.IOException;

import alioth.Alioth;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Alioth using FXML.
 */
public class MainApp extends Application {
    private final Alioth alioth = new Alioth("data/alioth.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            MainWindow controller = fxmlLoader.getController();
            controller.setAlioth(alioth);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
