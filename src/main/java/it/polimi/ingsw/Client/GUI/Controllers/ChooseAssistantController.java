package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Assistant;
import it.polimi.ingsw.Model.Wizard;
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
    private Button ConfirmButton, goToBoardButton;
    @FXML
    private Label messageLabel;
    @FXML
    ToggleGroup toggleGroup = new ToggleGroup();
    GUI gui;
    private Assistant assistant;
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
        if (radio1.isSelected() || radio2.isSelected() ||
                radio3.isSelected() || radio4.isSelected()) {
            if (radio1.isSelected()) {
                //assistant =
            } else if (radio2.isSelected()) {
                //assistant =
            } else if (radio3.isSelected()) {
                //assistant =
            } else if (radio4.isSelected()) {
                //assistant =
            } else if (radio5.isSelected()) {
                //assistant =
            } else if (radio6.isSelected()) {
                //assistant =
            } else if (radio7.isSelected()) {
                //assistant =
            } else if (radio8.isSelected()) {
                //assistant =
            } else if (radio9.isSelected()) {
                //assistant =
            } else if (radio10.isSelected()) {
                //assistant =
            }
        }
    }

    public void choose() {

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

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
