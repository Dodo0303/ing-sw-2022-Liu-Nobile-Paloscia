package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Exceptions.EmptyBagException;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CharacterFactoryTest {
    private GameModel game;

    @BeforeEach
    public void createGame() {
        game = new GameModel(2, new String[]{"player1", "player2"}, new Wizard[]{Wizard.WIZARD1, Wizard.WIZARD2});
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,10,11,12})
    public void testCreateCharacter(int id) {
        CharacterFactory factory = new CharacterFactory(game);
        CharacterCard character = factory.createCharacter(id);
        assertEquals(id, character.getID());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 13})
    public void testCreateCharacter_InvalidId_ShouldThrowException(int id) {
        CharacterFactory factory = new CharacterFactory(game);
        assertThrows(GameException.class, ()->{
            factory.createCharacter(id);
        });
    }

    @Test
    public void testCreateCharacterThatNeedsStudents_EmptyBag_ShouldThrowException() {
        CharacterFactory factory = new CharacterFactory(game);
        int students = game.getBag().getRemainingNum();
        while (students > 0) {
            try {
                game.getBag().extractStudent();
            } catch (EmptyBagException e) {
                fail();
            }
            students = game.getBag().getRemainingNum();
        }
        assertThrows(GameException.class, ()->{
            factory.createCharacter(1);
        });
    }

}