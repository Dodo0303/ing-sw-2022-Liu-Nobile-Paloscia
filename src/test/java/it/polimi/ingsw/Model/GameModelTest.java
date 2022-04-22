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
        game2.addStudentsToCloud(game2.getClouds().get(0), 2);
        game2.addStudentsToCloud(game2.getClouds().get(1), 2);
        game3.addStudentsToCloud(game3.getClouds().get(0), 3);
        game3.addStudentsToCloud(game3.getClouds().get(1), 3);
        game3.addStudentsToCloud(game3.getClouds().get(2), 3);
        game4.addStudentsToCloud(game4.getClouds().get(0), 4);
        game4.addStudentsToCloud(game4.getClouds().get(1), 4);
        game4.addStudentsToCloud(game4.getClouds().get(2), 4);
        game4.addStudentsToCloud(game4.getClouds().get(3), 4);
        assertEquals(3, game2.getClouds().get(0).getStudents().size());
        assertEquals(3, game2.getClouds().get(1).getStudents().size());
        assertEquals(4, game3.getClouds().get(0).getStudents().size());
        assertEquals(4, game3.getClouds().get(1).getStudents().size());
        assertEquals(4, game3.getClouds().get(2).getStudents().size());
        assertEquals(3, game4.getClouds().get(0).getStudents().size());
        assertEquals(3, game4.getClouds().get(1).getStudents().size());
        assertEquals(3, game4.getClouds().get(2).getStudents().size());
        assertEquals(3, game4.getClouds().get(3).getStudents().size());
    }

    @Test
    void playAssistant_1() {
        try {
            game2.playAssistant(game2.getPlayers().get(0).getAssistants()[1], game2.getPlayers().get(0));
            game2.playAssistant(game2.getPlayers().get(1).getAssistants()[1], game2.getPlayers().get(1));
            fail("expected exception was not occured.");
        } catch (GameException e) {
            System.out.printf(e.getMessage());
        }
    }

    @Test
    void playAssistant_2() {
        Assistant temp = game2.getPlayers().get(0).getAssistants()[1];
        game2.playAssistant(game2.getPlayers().get(0).getAssistants()[1], game2.getPlayers().get(0));
        assertNull(game2.getPlayers().get(0).getAssistants()[1]);
        assertEquals(game2.getPlayers().get(0).getUsedAssistant(), temp);
    }

    @Test
    void moveStudentToDiningRoom() {
        try {
            game2.moveStudentToDiningRoom(game2.getPlayers().get(0), StudentColor.BLUE);
            assertEquals(game2.getPlayers().get(0).getDiningTables().get(StudentColor.BLUE).getNumOfStudents(), 1);
            game2.moveStudentToDiningRoom(game2.getPlayers().get(0), StudentColor.RED);
            assertEquals(game2.getPlayers().get(0).getDiningTables().get(StudentColor.RED).getNumOfStudents(), 1);
        } catch (GameException e) {
            System.out.printf(e.getMessage());
        }
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