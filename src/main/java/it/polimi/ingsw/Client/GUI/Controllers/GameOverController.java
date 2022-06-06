package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class GameOverController {

    @FXML
    private Button ExitButton, CheckBoardButton;
    @FXML
    private Label messageLabel;
    @FXML
    GUI gui;

    public void execute() {
        System.exit(0);
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void fullScreen() {
        gui.getStage().setFullScreen(true);
    }
}
