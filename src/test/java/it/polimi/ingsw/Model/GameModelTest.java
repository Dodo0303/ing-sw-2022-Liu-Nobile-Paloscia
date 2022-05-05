package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.FullCloudException;
import it.polimi.ingsw.Exceptions.GameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}