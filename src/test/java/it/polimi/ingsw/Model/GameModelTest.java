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
        game2 = new GameModel(2, new String[]{"ciao", "ciao2"}, wizards2);
        game3 = new GameModel(3, new String[]{"ciao", "ciao2", "ciao3"}, wizards3);
        game4 = new GameModel(4, new String[]{"ciao", "ciao2", "ciao3", "ciao4"}, wizards4);
    }

    @Test
    void getSpareCoins() {
        assertEquals(20, game2.getSpareCoins());
    }

    @Test
    void getMotherNature() {
        assertNotNull(game2.getMotherNature());
    }

}