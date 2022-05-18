package it.polimi.ingsw.Client.GUI.Controllers.Uncategorized;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Wizard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class ChooseWizardController {
    @FXML
    private RadioButton radio1, radio2, radio3, radio4;
    @FXML
    private Button ConfirmButton;
    @FXML
    private Label messageLabel;
    GUI gui;
    Wizard wizard;

    public Wizard execute() {
        if (radio1.isSelected()) {
            wizard = Wizard.WIZARD1;
        } else if (radio2.isSelected()) {
            wizard = Wizard.WIZARD2;
        } else if (radio3.isSelected()) {
            wizard = Wizard.WIZARD3;
        } else if (radio4.isSelected()) {
            wizard = Wizard.WIZARD4;
        }
        return wizard;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

}
