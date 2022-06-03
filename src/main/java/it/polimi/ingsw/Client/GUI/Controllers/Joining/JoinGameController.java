package it.polimi.ingsw.Client.GUI.Controllers.Joining;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.MatchChosenMessage;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import java.util.List;

public class JoinGameController {
    @FXML
    private Label messageLabel;
    @FXML
    private ChoiceBox<String> matchBox;
    GUI gui;

    public void execute() {
        try {
            if (matchBox.getValue() == null) {
                setMessage("Please select a match.");
            } else {
                gui.send(new MatchChosenMessage(Integer.parseInt(matchBox.getValue()))); //todo why minus one to get to correct match id
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMatches(List<Integer> matches) {
        for (int i : matches) {
            matchBox.getItems().add(String.valueOf(i));
        }
    }

    public GUI getGUI() {
        return gui;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setMessage(String msg) {
        this.messageLabel.setText(msg);
    }
}
