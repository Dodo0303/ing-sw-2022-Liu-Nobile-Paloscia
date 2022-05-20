package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;

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
            setMessage("Connecting...");
            gui.setCurrPhase(Phase_GUI.BuildingConnection);
            if (gui.settingUpConnection(host, port)) {
                gui.setCurrPhase(Phase_GUI.PickingNickname);
                gui.startServerHandler();
                gui.requireNickname(false);
            } else {
                setMessage("Network not reachable, please try again.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText(portField.getText() + " is not a valid port.");
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
