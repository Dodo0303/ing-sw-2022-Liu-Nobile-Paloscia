package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
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
            gui.setPrevPhase(gui.getCurrPhase());
            gui.setCurrPhase(Phase_GUI.Character1);
        } else {
            setMessage("You cannot use this card.");
        }
    }

    public void back() {
        gui.checkBoard("");
    }

    private boolean check(int x) {
        try {
            gui.getGame().canAffordCharacter(gui.getNickname(),x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void setAvailableCharacters() {
        for (int i = 0; i < gui.getGame().getCharacters().size(); i++) {
            if (gui.getGame().getCharacters().get(i).getID() == 1) {
                radio1.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 2) {
                radio2.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 3) {
                radio3.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 4) {
                radio4.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 5) {
                radio5.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 6) {
                radio6.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 7) {
                radio7.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 8) {
                radio8.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 9) {
                radio9.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 10) {
                radio10.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 11) {
                radio11.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 12) {
                radio12.setDisable(false);
            }
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
