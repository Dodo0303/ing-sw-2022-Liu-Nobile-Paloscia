package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Assistant;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseAssistantController implements Initializable {
    @FXML
    private RadioButton radio1, radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9, radio10;
    @FXML
    private Button ConfirmButton, CheckBoardButton;
    @FXML
    private Label messageLabel;
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
        radio6.setToggleGroup(toggleGroup);
        radio7.setToggleGroup(toggleGroup);
        radio8.setToggleGroup(toggleGroup);
        radio9.setToggleGroup(toggleGroup);
        radio10.setToggleGroup(toggleGroup);
    }

    public void execute() {
        Assistant assistant;
        if (radio1.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(0);
        } else if (radio2.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(1);
        } else if (radio3.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(2);
        } else if (radio4.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(3);
        } else if (radio5.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(4);
        } else if (radio6.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(5);
        } else if (radio7.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(6);
        } else if (radio8.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(7);
        } else if (radio9.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(8);
        } else if (radio10.isSelected()) {
            assistant = gui.getGame().getPlayers().get(gui.getGame().getPlayerIndexFromNickname(gui.getNickname())).getAssistants().get(9);
        } else {
            setMessage("Please choose an assistant card.");
            return;
        }
        if (checkAssistantAvailability(assistant)) {
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
            ConfirmButton.setDisable(true);
            CheckBoardButton.setDisable(true);
            gui.send(new SendAssistantMessage(assistant));
        } else {
            setMessage("This card is not available");
        }

    }

    public void checkBoard() {
        gui.checkBoard("");
    }

    private boolean checkAssistantAvailability(Assistant assistant) {
            if  (gui.getGame().getPlayerByNickname(gui.getNickname()).getUsedAssistant() != null && assistant.getValue() == gui.getGame().getPlayerByNickname(gui.getNickname()).getUsedAssistant().getValue() && !gui.getGame().getPlayerByNickname(gui.getNickname()).lastAssistant()) {
                return false;
            }
        return true;
    }

    public void disableRadio(int x) {
        switch (x) {
            case 1 : {
                radio1.setDisable(true);
                break;
            }
            case 2 : {
                radio2.setDisable(true);
                break;
            }
            case 3 : {
                radio3.setDisable(true);
                break;
            }
            case 4 : {
                radio4.setDisable(true);
                break;
            }
            case 5 : {
                radio5.setDisable(true);
                break;
            }
            case 6 : {
                radio6.setDisable(true);
                break;
            }
            case 7 : {
                radio7.setDisable(true);
                break;
            }
            case 8 : {
                radio8.setDisable(true);
                break;
            }
            case 9 : {
                radio9.setDisable(true);
                break;
            }
            case 10 : {
                radio10.setDisable(true);
                break;
            }
        }
    }


    public void choose1() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio1.setSelected(true);

    }


    public void choose2() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio2.setSelected(true);
    }

    public void choose3() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio3.setSelected(true);
    }


    public void choose4() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio4.setSelected(true);
    }

    public void choose5() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio5.setSelected(true);
    }


    public void choose6() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio6.setSelected(true);
    }

    public void choose7() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio7.setSelected(true);
    }


    public void choose8() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio8.setSelected(true);
    }

    public void choose9() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio9.setSelected(true);
    }


    public void choose10() {
        toggleGroup.getSelectedToggle().setSelected(false);
        radio10.setSelected(true);
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}