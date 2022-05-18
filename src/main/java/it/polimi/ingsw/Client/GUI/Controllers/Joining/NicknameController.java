package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendNickMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NicknameController {
    @FXML
    private TextField nicknameField;
    @FXML
    private Label messageLabel;

    GUI gui;
    String nickname;

    public void requireNickname() {
        nickname = nicknameField.getText();
        gui.send(new SendNickMessage(nickname));
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
