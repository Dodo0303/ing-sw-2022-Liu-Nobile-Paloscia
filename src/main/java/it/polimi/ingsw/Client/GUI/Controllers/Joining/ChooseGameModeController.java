package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.CreateMatchMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void newgame(ActionEvent actionEvent) {
        gui.send(new CreateMatchMessage(true));
        gui.setCurrPhase(Phase_GUI.CreatingGame);
        gui.newgame();
    }

    public void joingame(ActionEvent actionEvent) {
        gui.send(new CreateMatchMessage(false));
        gui.setCurrPhase(Phase_GUI.JoiningGame1);
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }



}
