package it.polimi.ingsw.Client.GUI.Phases.Login;

import it.polimi.ingsw.Client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Login {
    @FXML
    private Button loginButton;
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private Label messageLabel;

    Stage stage;
    int port;
    String host;

    public void login(ActionEvent actionEvent) {
        try {
            host = hostField.getText();
            port = Integer.parseInt(portField.getText());
            messageLabel.setText("");
            //todo make socket
        } catch (NumberFormatException e) {
            messageLabel.setText(portField.getText() + " is not a valid port.");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
