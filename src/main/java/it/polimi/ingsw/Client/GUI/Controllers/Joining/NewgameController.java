package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendStartInfoMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class NewgameController implements Initializable {
    @FXML
    private Label messageLabel;
    @FXML
    private CheckBox expertBox;
    @FXML
    private ChoiceBox<String> numPlayerBox;
    private int numChosen;
    private String[] nums = {"2", "3", "4"};
    Wizard wizard;
    GUI gui;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numPlayerBox.getItems().addAll(nums);
    }

    public void execute() {
        try {
            boolean mode = expertBox.isSelected();
            int x = Integer.parseInt(numPlayerBox.getValue());
            //gui.changeScene("ChooseWizardController", "");
            gui.send(new SendStartInfoMessage(x, mode, wizard));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

}
