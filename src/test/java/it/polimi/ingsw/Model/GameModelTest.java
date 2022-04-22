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
            System.out.printf(e.getMessage() + "\n");
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
            System.out.printf(e.getMessage() + "\n");
        }
    }

    @Test
    void moveStudentToIsland() {
        try {
            game2.moveStudentToIsland(game2.getPlayers().get(0), game2.getIslands().get(0), StudentColor.BLUE);
            assertEquals(game2.getIslands().get(0).getStudents().get(StudentColor.BLUE), 1);
        } catch (GameException e) {
            System.out.printf(e.getMessage() + "\n");
        }
    }

    @Test
    void moveMotherNature_1() {
        try {
            game2.moveMotherNature(100, game2.getPlayers().get(0));
        } catch (GameException e) {
            System.out.printf(e.getMessage() + "\n");
        }
    }

    @Test
    void moveMotherNature_2() {
        game2.playAssistant(game2.getPlayers().get(0).getAssistants()[9], game2.getPlayers().get(0));
        game2.setMothernature(5);
        game2.moveMotherNature(10, game2.getPlayers().get(0));
        assertEquals(game2.getMotherNature(), game2.getIslands().get(10));
    }

    @Test
    void moveMotherNature_3() {
        Island temp = game2.getIslands().get(6);
        game2.playAssistant(game2.getPlayers().get(0).getAssistants()[9], game2.getPlayers().get(0));
        game2.setMothernature(5);
        game2.getIslands().get(5).setTowerColor(Color.WHITE);
        game2.getIslands().get(6).addStudent(StudentColor.BLUE);
        game2.getPlayers().get(0).setProfessors(StudentColor.BLUE);
        game2.moveMotherNature(6, game2.getPlayers().get(0));
        assertFalse(game2.getIslands().containsValue(temp));
        assertEquals(game2.getIslands().size(), 11);
    }

    @Test
    void moveMotherNature_5() {

    }
    @Test
    void takeStudentsFromCloud(){
        try {
            game2.getClouds().get(0).addStudent(StudentColor.BLUE);
            game2.getClouds().get(0).addStudent(StudentColor.BLUE);
            game2.getClouds().get(0).addStudent(StudentColor.BLUE);
        } catch (FullCloudException e) {

        }
        game2.getPlayers().get(0).clearEntrance();
        game2.takeStudentsFromCloud(game2.getPlayers().get(0), game2.getClouds().get(0),2);
        int num = 0;
        for(StudentColor color : game2.getPlayers().get(0).getEntranceStudents().keySet()) {
            num += game2.getPlayers().get(0).getEntranceStudents().get(color);
        }
        assertEquals(3, num);
    }

    @Test
    void getSpareCoins() {
        assertEquals(20, game2.getSpareCoins());
    }

    @Test
    void getMotherNature() {
        assertNotNull(game2.getMotherNature());
    }

    @Test
    void getCurrentPlayer() {
        assertNotNull(game2.getCurrentPlayer());
    }

    @Test
    void setCurrentPlayer() {
        game2.setCurrentPlayer(game2.getPlayers().get(1));
        assertEquals(game2.getCurrentPlayer(), game2.getPlayers().get(1));
    }

    @Test
    void getWinner() {
        assertNull(game2.getWinner());
    }

    @Test
    void gets() {
        assertNull(game2.getWinner());
    }

}