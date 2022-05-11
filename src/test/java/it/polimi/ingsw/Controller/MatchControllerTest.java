package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.FullCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchControllerTest {
    //TODO

    private MatchController game2;
    private MatchController game3;
    private MatchController game4;

    @BeforeEach
    public void setUp() {
        Wizard[] wizards2 = {Wizard.WIZARD1, Wizard.WIZARD2};
        Wizard[] wizards3 = {Wizard.WIZARD1, Wizard.WIZARD2, Wizard.WIZARD3};
        Wizard[] wizards4 = {Wizard.WIZARD1, Wizard.WIZARD2, Wizard.WIZARD3, Wizard.WIZARD4};
        game2 = new MatchController(2, 2);
        game3 = new MatchController(3,3);
        game4 = new MatchController(4,4);
    }

    @Test
    void addStudentsToCloud() {
        game2.addStudentsToCloud(game2.getGame().getClouds().get(0), 2);
        game2.addStudentsToCloud(game2.getGame().getClouds().get(1), 2);
        game3.addStudentsToCloud(game3.getGame().getClouds().get(0), 3);
        game3.addStudentsToCloud(game3.getGame().getClouds().get(1), 3);
        game3.addStudentsToCloud(game3.getGame().getClouds().get(2), 3);
        game4.addStudentsToCloud(game4.getGame().getClouds().get(0), 4);
        game4.addStudentsToCloud(game4.getGame().getClouds().get(1), 4);
        game4.addStudentsToCloud(game4.getGame().getClouds().get(2), 4);
        game4.addStudentsToCloud(game4.getGame().getClouds().get(3), 4);
        assertEquals(3, game2.getGame().getClouds().get(0).getStudents().size());
        assertEquals(3, game2.getGame().getClouds().get(1).getStudents().size());
        assertEquals(4, game3.getGame().getClouds().get(0).getStudents().size());
        assertEquals(4, game3.getGame().getClouds().get(1).getStudents().size());
        assertEquals(4, game3.getGame().getClouds().get(2).getStudents().size());
        assertEquals(3, game4.getGame().getClouds().get(0).getStudents().size());
        assertEquals(3, game4.getGame().getClouds().get(1).getStudents().size());
        assertEquals(3, game4.getGame().getClouds().get(2).getStudents().size());
        assertEquals(3, game4.getGame().getClouds().get(3).getStudents().size());
    }

    @Test
    void playAssistant_1() {
        try {
            game2.setAssistantOfCurrentPlayer(game2.getGame().getPlayers().get(0).getAssistants().get(1));
            game2.setAssistantOfCurrentPlayer(game2.getGame().getPlayers().get(1).getAssistants().get(1));
            fail("expected exception was not occured.");
        } catch (GameException e) {
            System.out.printf(e.getMessage() + "\n");
        }
    }

    @Test
    void playAssistant_2() {
        Assistant temp = game2.getGame().getPlayers().get(0).getAssistants().get(1);
        game2.setAssistantOfCurrentPlayer(game2.getGame().getPlayers().get(0).getAssistants().get(1));
        assertNull(game2.getGame().getPlayers().get(0).getAssistants().get(1));
        assertEquals(game2.getGame().getPlayers().get(0).getUsedAssistant(), temp);
    }

    @Test
    void moveStudentToDiningRoom() {
        try {
            game2.moveStudentToDiningRoom(StudentColor.BLUE);
            assertEquals(game2.getGame().getPlayers().get(0).getDiningTables().get(StudentColor.BLUE).getNumOfStudents(), 1);
            game2.moveStudentToDiningRoom(StudentColor.RED);
            assertEquals(game2.getGame().getPlayers().get(0).getDiningTables().get(StudentColor.RED).getNumOfStudents(), 1);
        } catch (FullTableException e) {
            System.out.printf(e.getMessage() + "\n");
        }
    }
/*
    @Test
    void moveStudentToIsland() {
        try {
            game2.moveStudentToIsland(game2.getGame().getIslands().get(0), StudentColor.BLUE);
            assertEquals(game2.getGame().getIslands().get(0).getStudents().get(StudentColor.BLUE), 1);
        } catch (GameException e) {
            System.out.printf(e.getMessage() + "\n");
        }
    }
*/
    @Test
    void moveMotherNature_1() {
        try {
            game2.moveMotherNature(100);
        } catch (GameException e) {
            System.out.printf(e.getMessage() + "\n");
        }
    }

    @Test
    void moveMotherNature_2() {
        game2.setAssistantOfCurrentPlayer(game2.getGame().getPlayers().get(0).getAssistants().get(1));
        game2.getGame().setMothernature(5);
        game2.moveMotherNature(10);
        assertEquals(game2.getGame().getMotherNature(), game2.getGame().getIslands().get(10));
    }

    @Test
    void moveMotherNature_3() {
        Island temp = game2.getGame().getIslands().get(6);
        game2.setAssistantOfCurrentPlayer(game2.getGame().getPlayers().get(0).getAssistants().get(1));
        game2.getGame().setMothernature(5);
        game2.getGame().getIslands().get(5).setTowerColor(Color.WHITE);
        game2.getGame().getIslands().get(6).addStudent(StudentColor.BLUE);
        game2.getGame().getPlayers().get(0).setProfessors(StudentColor.BLUE);
        game2.moveMotherNature(6);
        assertFalse(game2.getGame().getIslands().containsValue(temp));
        assertEquals(game2.getGame().getIslands().size(), 11);
    }

    @Test
    void takeStudentsFromCloud(){
        try {
            game2.getGame().getClouds().get(0).addStudent(StudentColor.BLUE);
            game2.getGame().getClouds().get(0).addStudent(StudentColor.BLUE);
            game2.getGame().getClouds().get(0).addStudent(StudentColor.BLUE);
        } catch (FullCloudException e) {

        }
        game2.getGame().getPlayers().get(0).clearEntrance();
        game2.takeStudentsFromCloud(0);
        int num = 0;
        for(StudentColor color : game2.getGame().getPlayers().get(0).getEntranceStudents().keySet()) {
            num += game2.getGame().getPlayers().get(0).getEntranceStudents().get(color);
        }
        assertEquals(3, num);
    }
}
