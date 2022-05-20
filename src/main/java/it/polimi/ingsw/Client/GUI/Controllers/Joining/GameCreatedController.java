package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameCreatedController {
    @FXML
    private Label messageLabel;
    private GUI gui;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
