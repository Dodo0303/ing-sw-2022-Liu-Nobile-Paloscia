package it.polimi.ingsw.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    @BeforeEach
    void init() {
        Wizard[] wizards2 = {Wizard.WIZARD1, Wizard.WIZARD2};
        Wizard[] wizards3 = {Wizard.WIZARD1, Wizard.WIZARD2, Wizard.WIZARD3};
        Wizard[] wizards4 = {Wizard.WIZARD1, Wizard.WIZARD2, Wizard.WIZARD3, Wizard.WIZARD4};
        GameModel game2 = new GameModel(wizards2, 2);
        GameModel game3 = new GameModel(wizards3, 3);
        GameModel game4 = new GameModel(wizards4, 4);
    }

    @Test
    void setEntranceStudents() {


    }

    @Test
    void addStudentsToCloud() {
    }

    @Test
    void playAssistant() {
    }

    @Test
    void moveStudentToDiningRoom() {
    }

    @Test
    void moveStudentToIsland() {
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void takeStudentsFromCloud() {
    }

    @Test
    void getSpareCoins() {
    }

    @Test
    void getMotherNature() {
    }

    @Test
    void getCurrentPlayer() {
    }

    @Test
    void setCurrentPlayer() {
    }

    @Test
    void getWinner() {
    }

    @Test
    void isLegal() {
    }
}