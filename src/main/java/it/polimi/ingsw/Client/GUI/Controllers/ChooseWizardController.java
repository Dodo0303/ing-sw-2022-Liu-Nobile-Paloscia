package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendChosenWizardMessage;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendStartInfoMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseWizardController implements Initializable {
    @FXML
    private RadioButton radio1, radio2, radio3, radio4;
    @FXML
    private Button ConfirmButton;
    @FXML
    private Label messageLabel, label1, label2,label3, label4, labelTeam1, labelTeam2;
    @FXML
    ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    Separator teamSeparator;
    GUI gui;
    Wizard wizard;
    int numPlayer;
    boolean expert;
    boolean newgame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio3.setToggleGroup(toggleGroup);
        radio4.setToggleGroup(toggleGroup);
    }

    public void execute() {
        if (radio1.isSelected() || radio2.isSelected() ||
                radio3.isSelected() || radio4.isSelected()) {
            if (radio1.isSelected()) {
                wizard = Wizard.WIZARD1;
            } else if (radio2.isSelected()) {
                wizard = Wizard.WIZARD2;
            } else if (radio3.isSelected()) {
                wizard = Wizard.WIZARD3;
            } else if (radio4.isSelected()) {
                wizard = Wizard.WIZARD4;
            }
            radio1.setDisable(true);
            radio2.setDisable(true);
            radio3.setDisable(true);
            radio4.setDisable(true);
            ConfirmButton.setDisable(true);
        } else {
            setMessage("Please choose a wizard.");
        }
        if (newgame) {
            gui.send(new SendStartInfoMessage(numPlayer, expert, wizard));
        } else {
            gui.send(new SendChosenWizardMessage(wizard));
        }
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
        }
    }

    public void setMessageForNewGame(int numPlayer, boolean expert) {
        this.numPlayer = numPlayer;
        this.expert = expert;
    }

    public void setFourPlayerGame(boolean fourPlayers) {
        label1.setVisible(true);
        label2.setVisible(true);
        label3.setVisible(true);
        label4.setVisible(true);
        if (fourPlayers){
            labelTeam1.setVisible(true); labelTeam2.setVisible(true);
        }
        if (radio1.isDisabled()) {
            if (gui.getNicknames()[0] != null) {
                label1.setText(gui.getNicknames()[0]);
                label1.setVisible(true);
                label1.setDisable(false);
            }
        }
        if (radio2.isDisabled()) {
            if (gui.getNicknames()[1] != null) {
                label2.setText(gui.getNicknames()[1]);
                label2.setVisible(true);
                label2.setDisable(false);
            }
        }
        if (radio3.isDisabled()) {
            if (gui.getNicknames()[2] != null) {
                label3.setText(gui.getNicknames()[2]);
                label3.setVisible(true);
                label3.setDisable(false);
            }
        }
        if (radio4.isDisabled()) {
            if (gui.getNicknames()[3] != null) {
                label4.setText(gui.getNicknames()[3]);
                label4.setVisible(true);
                label4.setDisable(false);
            }
        }
    }

    public Wizard getWizard() {
        return this.wizard;
    }
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    public void setNewgame(boolean newgame) {
        this.newgame = newgame;
    }
}
