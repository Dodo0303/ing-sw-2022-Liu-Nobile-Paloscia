package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.Controllers.Uncategorized.DraggableMaker;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.StudentColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML
    private Button myBoardButton, OpponentBoardButton, menuButton;
    @FXML
    private StackPane island0, island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11;
    private GUI gui;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameModel game = gui.getGame();
        for (Island island : game.getIslands().values()) {
            if (island.getNumTower() > 1) {
                //todo
            } else {

            }
        }
    }

    public void viewOpponentBoard() {
        if (gui.getNumPlayer() == 2) {

        }
        //TODO

    }

    public void back() {
        gui.playAssistant("");
    }

    public void viewMyBoard() {

    }

    public StackPane getStackPaneByIndex(int x) {
        switch (x) {
            case 0 : {
                return island0;
            }
            case 1 : {
                return island1;
            }
            case 2 : {
                return island2;
            }
            case 3 : {
                return island3;
            }
            case 4 : {
                return island4;
            }
            case 5 : {
                return island5;
            }
            case 6 : {
                return island6;
            }
            case 7 : {
                return island7;
            }
            case 8 : {
                return island8;
            }
            case 9 : {
                return island9;
            }
            case 10 : {
                return island10;
            }
            case 11 : {
                return island11;
            }
        }
        return null;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

}
