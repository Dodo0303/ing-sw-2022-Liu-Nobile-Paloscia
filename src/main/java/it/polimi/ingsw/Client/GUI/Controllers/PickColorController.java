package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Assistant;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toServer.CharacterPhase.ChooseStudentColorMessage;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.Model.StudentColor.*;

public class PickColorController implements Initializable {
    @FXML
    private RadioButton radio1, radio2, radio3, radio4, radio5;
    @FXML
    private Button ConfirmButton, CheckBoardButton;
    @FXML
    ToggleGroup toggleGroup = new ToggleGroup();
    GUI gui;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio3.setToggleGroup(toggleGroup);
        radio4.setToggleGroup(toggleGroup);
        radio5.setToggleGroup(toggleGroup);
    }

    public void execute() {
        StudentColor color;
        if (radio1.isSelected()) {
            color = GREEN;
        } else if (radio2.isSelected()) {
            color = BLUE;
        } else if (radio3.isSelected()) {
            color = YELLOW;
        } else if (radio4.isSelected()) {
            color = RED;
        } else if (radio5.isSelected()) {
            color = PINK;
        } else {
            return;
        }
        radio1.setDisable(true);
        radio2.setDisable(true);
        radio3.setDisable(true);
        radio4.setDisable(true);
        radio5.setDisable(true);
        ConfirmButton.setDisable(true);
        CheckBoardButton.setDisable(true);
        gui.send(new ChooseStudentColorMessage(color));
    }

    public void checkBoard() {
        gui.checkBoard("");
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
