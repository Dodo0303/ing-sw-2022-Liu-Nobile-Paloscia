package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ChooseGameModeController{
    @FXML
    private Label messageLabel;
    @FXML
    private Button newgameButton;
    @FXML
    private Button joingameButton;

    GUI gui;


    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void joingame(ActionEvent actionEvent) {
    }

    public void newgame(ActionEvent actionEvent) {
    }
}
