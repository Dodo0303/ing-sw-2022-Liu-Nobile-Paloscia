package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GUI.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class EriantysClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.setProperty("sun.java2d.uiScale", "1.0");
        GUI gui = new GUI(stage);
        gui.start();
    }
}
