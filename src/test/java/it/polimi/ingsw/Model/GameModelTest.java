package it.polimi.ingsw.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    private GameModel game2;
    private GameModel game3;
    private GameModel game4;

    @BeforeEach
    public void setUp() {
        Wizard[] wizards2 = {Wizard.WIZARD1, Wizard.WIZARD2};
        Wizard[] wizards3 = {Wizard.WIZARD1, Wizard.WIZARD2, Wizard.WIZARD3};
        Wizard[] wizards4 = {Wizard.WIZARD1, Wizard.WIZARD2, Wizard.WIZARD3, Wizard.WIZARD4};
        game2 = new GameModel(wizards2, 2);
        game3 = new GameModel(wizards3, 3);
        game4 = new GameModel(wizards4, 4);
    }

    @Test
    public void testSetEntranceStudents() {
        for (Player player : game2.getPlayers()) {
            assertEquals(7, player.getEntranceStudents().get(StudentColor.BLUE)
                    + player.getEntranceStudents().get(StudentColor.GREEN)
                    + player.getEntranceStudents().get(StudentColor.PINK)
                    + player.getEntranceStudents().get(StudentColor.RED)
                    + player.getEntranceStudents().get(StudentColor.YELLOW));
        }
        for (Player player : game3.getPlayers()) {
            assertEquals(9, player.getEntranceStudents().get(StudentColor.BLUE)
                    + player.getEntranceStudents().get(StudentColor.GREEN)
                    + player.getEntranceStudents().get(StudentColor.PINK)
                    + player.getEntranceStudents().get(StudentColor.RED)
                    + player.getEntranceStudents().get(StudentColor.YELLOW));
        }
        for (Player player : game4.getPlayers()) {
            assertEquals(7, player.getEntranceStudents().get(StudentColor.BLUE)
                    + player.getEntranceStudents().get(StudentColor.GREEN)
                    + player.getEntranceStudents().get(StudentColor.PINK)
                    + player.getEntranceStudents().get(StudentColor.RED)
                    + player.getEntranceStudents().get(StudentColor.YELLOW));
        }
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