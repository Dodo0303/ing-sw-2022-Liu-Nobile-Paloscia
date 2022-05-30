package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Assistant;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseCharacterController implements Initializable {
    @FXML
    private RadioButton radio1, radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9, radio10, radio11, radio12;
    @FXML
    private Button ConfirmButton, CheckBoardButton;
    @FXML
    private Label messageLabel;
    @FXML
    ToggleGroup toggleGroup = new ToggleGroup();
    GUI gui;
    int res;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio3.setToggleGroup(toggleGroup);
        radio4.setToggleGroup(toggleGroup);
        radio5.setToggleGroup(toggleGroup);
        radio6.setToggleGroup(toggleGroup);
        radio7.setToggleGroup(toggleGroup);
        radio8.setToggleGroup(toggleGroup);
        radio9.setToggleGroup(toggleGroup);
        radio10.setToggleGroup(toggleGroup);
        radio11.setToggleGroup(toggleGroup);
        radio12.setToggleGroup(toggleGroup);
    }

    public void execute() {
        if (radio1.isSelected()) {
            res = 1;
        } else if (radio2.isSelected()) {
            res = 2;
        } else if (radio3.isSelected()) {
            res = 3;
        } else if (radio4.isSelected()) {
            res = 4;
        } else if (radio5.isSelected()) {
            res = 5;
        } else if (radio6.isSelected()) {
            res = 6;
        } else if (radio7.isSelected()) {
            res = 7;
        } else if (radio8.isSelected()) {
            res = 8;
        } else if (radio9.isSelected()) {
            res = 9;
        } else if (radio10.isSelected()) {
            res = 10;
        } else if (radio11.isSelected()) {
            res = 11;
        } else if (radio12.isSelected()) {
            res = 12;
        }
        else {
            setMessage("Please choose a card.");
            return;
        }
        if (check(res)) {
            radio1.setDisable(true);
            radio2.setDisable(true);
            radio3.setDisable(true);
            radio4.setDisable(true);
            radio5.setDisable(true);
            radio6.setDisable(true);
            radio7.setDisable(true);
            radio8.setDisable(true);
            radio9.setDisable(true);
            radio10.setDisable(true);
            radio11.setDisable(true);
            radio12.setDisable(true);
            ConfirmButton.setDisable(true);
            CheckBoardButton.setDisable(true);
            gui.send(new UseCharacterMessage(res));
        } else {
            setMessage("This card is not available");
        }

    }

    private boolean check(int x) {
        //TODO
        return true;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
