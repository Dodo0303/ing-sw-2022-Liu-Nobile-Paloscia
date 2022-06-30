package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Exceptions.EmptyBagException;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Exceptions.WrongEffectException;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Model.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class CharactersTest {
    private GameModel game;
    private CharacterFactory factory;

    @BeforeEach
    public void initialization(){
        game = new GameModel(2, new String[]{"player1", "player2"}, new Wizard[]{Wizard.WIZARD2, Wizard.WIZARD1});
        factory = new CharacterFactory(game);
    }

    @Test
    public void testAddNoEntries() {
        CharacterCard character = factory.createCharacter(5);
        try {
            character.useEffect();
        } catch (WrongEffectException | NotEnoughNoEntriesException e) {
            fail();
        }
        try {
            character.addNoEntries();
            assertEquals(4, character.getNumberOfNoEntries());
        } catch (WrongEffectException e) {
            fail();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,6,7,8,9,10,11,12})
    public void testAddNoEntries_WrongCharacter_ShouldThrowsException(int id) {
        CharacterCard character = factory.createCharacter(id);
        assertThrows(WrongEffectException.class, character::addNoEntries);
        assertThrows(WrongEffectException.class, character::getNumberOfNoEntries);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3, 4, 5, 6, 8, 9, 10, 12})
    public void testUseEffect (int id) {
        CharacterCard character = factory.createCharacter(id);
        int initialPrice = character.getPrice();
        try {
            character.useEffect();
        } catch (WrongEffectException | NotEnoughNoEntriesException e) {
            fail();
        }
        assertEquals(initialPrice+1, character.getPrice());
    }

    @Test
    public void testUseEffectOnCharacter5_NotEnoughNoEntries_ShouldThrowException(){
        CharacterCard character = factory.createCharacter(5);
        for (int i = 0; i < 4; i++) {
            try {
                character.useEffect();
            } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                fail();
            }
        }
        assertThrows(NotEnoughNoEntriesException.class, character::useEffect);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,7,11})
    public void testUseEffect_WrongCharacter_ShouldThrowException(int id) {
        CharacterCard character = factory.createCharacter(id);
        assertThrows(WrongEffectException.class, character::useEffect);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4,5,6,8,9,10,12})
    public void testGetStudents_WrongCharacters_ShouldThrowException(int id) {
        CharacterCard character = factory.createCharacter(id);
        assertThrows(WrongEffectException.class, character::getStudents);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,7,11})
    public void testGetStudents(int id) {
        CharacterCard character = factory.createCharacter(id);
        assertDoesNotThrow(character::getStudents);
        try {
            assertNotNull(character.getStudents());
        } catch (WrongEffectException e) {
            fail();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1,7,11})
    public void testUseEffectWithStudents(int id) {
        CharacterCard character = factory.createCharacter(id);
        int initialPrice = character.getPrice();
        try {
            character.useEffect(0, StudentColor.BLUE);
            assertEquals(StudentColor.BLUE, character.getStudents().get(0));
        } catch (WrongEffectException e) {
            fail();
        }
        assertEquals(initialPrice+1, character.getPrice());
        assertTrue(character.isUsed());
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4,5,6,8,9,10,12})
    public void testUseEffectWithStudents_WrongCharacter_ShouldThrowException(int id) {
        CharacterCard character = factory.createCharacter(id);
        assertThrows(WrongEffectException.class, ()->{
            character.useEffect(0, StudentColor.BLUE);
        });
    }
}
