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
    @FXML
    private ImageView stud00_01, stud00_02, stud00_03, stud00_04, stud00_05, stud00_06, stud00_07, stud00_08, stud00_09, mn0, t01, t02;
    @FXML
    private ImageView stud01_01, stud01_02, stud01_03, stud01_04, stud01_05, stud01_06, stud01_07, stud01_08, stud01_09, mn1, t11, t12;
    @FXML
    private ImageView stud02_01, stud02_02, stud02_03, stud02_04, stud02_05, stud02_06, stud02_07, stud02_08, stud02_09, mn2, t21, t22;
    @FXML
    private ImageView stud03_01, stud03_02, stud03_03, stud03_04, stud03_05, stud03_06, stud03_07, stud03_08, stud03_09, mn3, t31, t32;
    @FXML
    private ImageView stud04_01, stud04_02, stud04_03, stud04_04, stud04_05, stud04_06, stud04_07, stud04_08, stud04_09, mn4, t41, t42;
    @FXML
    private ImageView stud05_01, stud05_02, stud05_03, stud05_04, stud05_05, stud05_06, stud05_07, stud05_08, stud05_09, mn5, t51, t52;
    @FXML
    private ImageView stud06_01, stud06_02, stud06_03, stud06_04, stud06_05, stud06_06, stud06_07, stud06_08, stud061_09, mn6, t61, t62;
    @FXML
    private ImageView stud07_01, stud07_02, stud07_03, stud07_04, stud07_05, stud07_06, stud07_07, stud07_08, stud07_09, mn7, t71, t72;
    @FXML
    private ImageView stud08_01, stud08_02, stud08_03, stud08_04, stud08_05, stud08_06, stud08_07, stud08_08, stud08_09, mn8, t81, t82;
    @FXML
    private ImageView stud09_01, stud09_02, stud09_03, stud09_04, stud09_05, stud09_06, stud09_07, stud09_08, stud09_09, mn9, t91, t92;
    @FXML
    private ImageView stud10_01, stud10_02, stud10_03, stud10_04, stud10_05, stud10_06, stud10_07, stud10_08, stud10_09, mn10, t10_1, t10_2;
    @FXML
    private ImageView stud11_01, stud11_02, stud11_03, stud11_04, stud11_05, stud11_06, stud11_07, stud11_08, stud11_09, mn11, t11_1, t11_2;
    private GUI gui;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameModel game = gui.getGame();
        for (Island island : game.getIslands().values()) {
            if (island.getNumTower() > 1) {
                //todo make images of 2,3, and more combined islands.
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
