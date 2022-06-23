package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.Character.CharacterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    @Test
    public void testRemoveSpareProfessor(){
        assertTrue(game2.getProfessors().contains(StudentColor.BLUE));
        game2.removeSpareProfessor(StudentColor.BLUE);
        assertFalse(game2.getProfessors().contains(StudentColor.BLUE));
    }

    @Test
    public void testSetAssistantOfPlayer(){
        Assistant assistantToBeUsed = new Assistant(1,1,Wizard.WIZARD1);
        Player player = game2.getPlayers().get(0);
        game2.setAssistantOfPlayer(player.getNickName(), assistantToBeUsed);
        assertEquals(player.getUsedAssistant(), assistantToBeUsed);
    }

    @Test
    public void testGetPlayerIndexFromNickname(){
        assertEquals(0, game2.getPlayerIndexFromNickname(game2.getPlayers().get(0).getNickName()));
    }

    @Test
    public void testGetPlayerIndexFromNickname_WrongNickname_ShouldThrowException() {
        assertThrows(NoSuchElementException.class, ()->{
            game2.getPlayerIndexFromNickname("unexistingNickname");
        });
    }

    @Test
    public void testRemoveStudentFromEntrance(){
        Player player = game2.getPlayers().get(0);
        StudentColor colorToBeRemoved = player.getEntranceStudents().get(0);
        StudentColor followingColor = player.getEntranceStudents().get(1);
        assertEquals(colorToBeRemoved, game2.removeStudentFromEntrance(player, 0));
        assertEquals(followingColor, player.getEntranceStudents().get(0));
    }

    @Test
    public void testAddStudentToEntrance() {
        Player player = game2.getPlayers().get(0);
        player.removeStudentFromEntrance(0);
        game2.addStudentToEntrance(player, StudentColor.BLUE);
        assertEquals(StudentColor.BLUE, player.getEntranceStudents().get(player.getEntranceStudents().size() - 1));
    }

    @Test
    public void testRemoveStudentFromTable(){
        Player player = game2.getPlayers().get(0);
        try {
            player.addToDiningTable(StudentColor.BLUE);
        } catch (FullTableException e) {
            fail();
        }
        try {
            game2.removeStudentFromTable(player, StudentColor.BLUE);
        } catch (EmptyTableException e) {
            fail();
        }
        for (StudentColor color :
                StudentColor.values()) {
            assertEquals(0, player.getDiningTables().get(StudentColor.BLUE).getNumOfStudents());
        }

    }

    @Test
    public void testAddToDiningTable(){
        Player player = game2.getPlayers().get(0);
        try {
            game2.addToDiningTable(player, StudentColor.BLUE);
        } catch (FullTableException e) {
            fail();
        }
        for (StudentColor color :
                StudentColor.values()) {
            if (color != StudentColor.BLUE)
                assertEquals(0, player.getDiningTables().get(color).getNumOfStudents());
        }
        assertEquals(1, player.getDiningTables().get(StudentColor.BLUE).getNumOfStudents());

    }

    @Test
    public void testAddStudentToIsland(){
        StudentColor color = StudentColor.BLUE;
        Island island = game2.getIslands().get(0);
        int oldStudents = island.getStudents().get(color);
        game2.addStudentToIsland(color, island);
        assertEquals(oldStudents + 1, island.getStudents().get(color));
    }

    @Test
    public void testGetTableNumber() {
        Player player = game2.getPlayers().get(0);
        try {
            player.addToDiningTable(StudentColor.BLUE);
        } catch (FullTableException e) {
            fail();
        }
        assertEquals(game2.getTableNumber(player, StudentColor.BLUE), player.getDiningTables().get(StudentColor.BLUE).getNumOfStudents());
    }

    @Test
    public void testAddNoEntry(){
        Island island = game2.getIslands().get(0);
        int oldValue = island.getNoEntries();
        game2.addNoEntry(island);
        assertEquals(oldValue + 1, island.getNoEntries());
    }

    @Test
    public void testGetCharacterById(){
        CharacterCard character = game2.getCharacters().get(0);
        assertEquals(character, game2.getCharacterById(character.getID()));
    }

    @Test
    public void testGetCharacterById_WrongId_ShouldThrowException(){
        assertThrows(GameException.class, ()->{
            game2.getCharacterById(13);
        });
    }

    @Test
    public void testUpdateCharacter(){
        CharacterCard character = game2.getCharacters().get(0);
        int id = character.getID();
        CharacterFactory newFactory = new CharacterFactory(game2);
        CharacterCard newCharacter = newFactory.createCharacter(id);
        game2.updateCharacterById(newCharacter);
        assertSame(newCharacter, game2.getCharacters().get(0));
    }

    @Test
    public void testUpdateCharacter_WrongCharacter_ShouldThrowException(){
        List<CharacterCard> characters = game2.getCharacters();
        List<Integer> ids = new ArrayList<>();
        for (CharacterCard character :
                characters) {
            ids.add(character.getID());
        }

        CharacterFactory factory = new CharacterFactory(game2);
        for (int i = 1; i < 13; i++) {
            if (!ids.contains(i)) {
                CharacterCard newCharacter = factory.createCharacter(i);
                assertThrows(GameException.class, ()->{game2.updateCharacterById(newCharacter);});
                break;
            }
        }
    }
}