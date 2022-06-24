package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GUI.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class EriantysClientGUI extends Application {

    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GUI gui = new GUI(stage);
        gui.start();
    }
}
