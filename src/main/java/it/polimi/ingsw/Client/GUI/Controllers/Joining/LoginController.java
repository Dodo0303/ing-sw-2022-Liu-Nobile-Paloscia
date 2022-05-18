package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private Label messageLabel;

    int port;
    String host;
    GUI gui;

    public LoginController() {

    }

    public void login() {
        try {
            host = hostField.getText();
            port = Integer.parseInt(portField.getText());
            messageLabel.setText("Connecting...");
            gui.setCurrPhase(Phase_GUI.BuildingConnection);
            gui.settingUpConnection(host, port);
            nextScene("/fxml/nickname.fxml", "", gui.getStage());
        } catch (NumberFormatException e) {
            messageLabel.setText(portField.getText() + " is not a valid port.");
        } catch (IOException e1) {
            messageLabel.setText("Network is unreachable, please try again.");
        }

    }
    public void nextScene(String file, String message, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file));
            Parent root = fxmlLoader.load();
            NicknameController nicknameController = fxmlLoader.getController();
            nicknameController.setGUI(gui);
            Scene scene = new Scene(root, 600, 402);
            if (!message.isEmpty()) {
                messageLabel.setText(message);
            }
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {

        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
