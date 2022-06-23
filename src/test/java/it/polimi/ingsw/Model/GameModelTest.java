package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyBagException;
import it.polimi.ingsw.Exceptions.FullCloudException;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

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

    @ParameterizedTest
    @ValueSource(ints = {0,1,5})
    public void createNewGame_WrongNumberOfPlayers(int numOfPlayers) {
        assertThrows(GameException.class, ()->{
            new GameModel(numOfPlayers, new String[numOfPlayers], new Wizard[numOfPlayers]);
        });
    }

    @Test
    public void testSetEntranceStudents_NoStudentsLeft_ShouldPrintStakcTrace(){
        Bag bag = game2.getBag();
        while (!bag.isEmpty()) {
            try {
                bag.extractStudent();
            } catch (EmptyBagException e) {
                fail();
            }
        }
        List<StudentColor> entrance;
        entrance = game2.getPlayers().get(0).getEntranceStudents();
        game2.setEntranceStudents(2);
        assertEquals(entrance.size(), game2.getPlayers().get(0).getEntranceStudents().size());
        assertTrue(entrance.containsAll(game2.getPlayers().get(0).getEntranceStudents()));

    }

    @Test
    public void testGetEntranceOfPlayer() {
        Player p = game2.getPlayers().get(0);
        assertEquals(p.getEntranceStudents(), game2.getEntranceOfPlayer(p));
    }

    @Test
    public void testSetMotherNature() {
        game2.setMothernature(2);
        assertEquals(game2.getIslands().get(2), game2.getMotherNature());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 12, 20})
    public void testSetMothernature_InvalidIslandIndex(int index) {
        assertThrows(GameException.class, ()->{game2.setMothernature(index);});
    }

    @Test
    public void testCanAffordCharacter_ShouldReturnTrue() {
        game2.getPlayers().get(0).setCoins(100);
        for (CharacterCard character: game2.getCharacters()) {
            assertTrue(game2.canAffordCharacter(game2.getPlayers().get(0).getNickName(), character.getID()));
        }
    }
    
    @Test
    public void testCanAffordCharacter_ShouldReturnFalse(){
        for (CharacterCard character :
                game2.getCharacters()) {
            assertFalse(game2.canAffordCharacter(game2.getPlayers().get(0).getNickName(), character.getID()));
        }
        assertFalse(game2.canAffordCharacter("unexistingnickname", game2.getCharacters().get(0).getID()));
    }

}