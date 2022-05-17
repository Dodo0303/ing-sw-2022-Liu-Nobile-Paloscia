package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class GUI {
    private final Stage stage;
    private ServerHandler serverHandler;

    public GUI(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        try {
            stage.getIcons().add(new Image("icon.png"));
            stage.setResizable(false);
            stage.setTitle("Eriantys");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml" + File.separator + "login.fxml")));
            Scene scene = new Scene(root, 1024, 1024);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
