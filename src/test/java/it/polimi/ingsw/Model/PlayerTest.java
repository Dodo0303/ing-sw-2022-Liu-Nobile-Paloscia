package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyTableException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.GameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @ParameterizedTest
    @ValueSource( ints = {-1, 1, 5})
    public void createPlayers_IllegalNumberOfPlayers_ShouldThrowException(int players){
        assertThrows(IllegalArgumentException.class, () -> {
            Player p = new Player(Color.BLACK, Wizard.WIZARD1, players);
        });
    }

    @ParameterizedTest
    @ValueSource( ints = {2,4})
    public void createPlayer_IllegalColor_ShouldThrowException(int players){
        assertThrows(IllegalArgumentException.class, () -> new Player(Color.GRAY, Wizard.WIZARD1, players));
    }

    @ParameterizedTest
    @ValueSource( ints = {2,3,4})
    public void createPlayer_VoidColor_ShouldThrowException(int players){
        assertThrows(IllegalArgumentException.class, () -> new Player(Color.VOID, Wizard.WIZARD1, players));
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void createPlayer_CheckAssistants(int players){
        Player player = new Player(Color.BLACK, Wizard.WIZARD1, players);
        Assistant[] ass = player.getAssistants();
        for (int i = 0; i < 10; i++) {
            assertEquals(i+1, ass[i].getValue());
            //Using the switch so we don't use any algorithm that could be wrong
            switch (i){
                case 0:
                case 1:
                    assertEquals(1, ass[i].getMaxSteps());
                    break;
                case 2:
                case 3:
                    assertEquals(2, ass[i].getMaxSteps());
                    break;
                case 4:
                case 5:
                    assertEquals(3, ass[i].getMaxSteps());
                    break;
                case 6:
                case 7:
                    assertEquals(4, ass[i].getMaxSteps());
                    break;
                case 8:
                case 9:
                    assertEquals(5, ass[i].getMaxSteps());
                    break;
            }
        }
    }

    @ParameterizedTest
    @ValueSource( ints = {2,4})
    public void createPlayer_CheckEntranceStudents_TwoFourPlayers(int players) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, players);
        HashMap<StudentColor, Integer> students = p.getEntranceStudents();
        for (StudentColor color :
                StudentColor.values()) {
            assertEquals(0, students.get(color));
        }
        assertEquals(7, p.getMaxEntranceStudents());

    }

    @Test
    public void createPlayer_CheckEntranceStudents_TwoFourPlayers() {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 3);
        HashMap<StudentColor, Integer> students = p.getEntranceStudents();
        for (StudentColor color :
                StudentColor.values()) {
            assertEquals(0, students.get(color));
        }
        assertEquals(9, p.getMaxEntranceStudents());

    }
    
    @ParameterizedTest
    @ValueSource( ints = {2,3,4})
    public void createPlayer_CheckProfessors(int players) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, players);
        List<StudentColor> professors = p.getProfessors();
        assertTrue(professors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource( ints = {2,3,4})
    public void createPlayer_CheckDiningTables(int players){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, players);
        Map<StudentColor, DiningTable> tables = p.getDiningTables();
        for (StudentColor color :
                StudentColor.values()) {
            DiningTable table = tables.get(color);
            assertEquals(0, table.getNumOfStudents());
            assertEquals(color, table.getColor());
            assertFalse(table.claimCoin());
        }
    }

    @Test
    public void testRemoveTower(){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 3);
        p.removeTower();
        assertEquals(p.getMaxTowerNum()-1, p.getTowerNum());
    }

    @Test
    public void removeTower_RemoveTooManyTowers_ShouldThrowException() {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(GameException.class, p::removeTower);
    }

    @Test
    public void testAddTower(){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        int towers = p.getTowerNum();
        p.addTower();
        assertEquals(towers + 1, p.getTowerNum());
    }

    @Test
    public void addTower_TooManyTowers_ShouldThrowException() {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 3);
        assertThrows(GameException.class, p::addTower);
    }

    @Test
    public void testAddCoin() {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        int initCoins = p.getCoins();
        p.addCoin();
        assertEquals(initCoins+1, p.getCoins());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    public void testRemoveCoins(int coinsToRemove) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        for (int i = 0; i < 10; i++) {
            p.addCoin();
        }
        p.removeCoins(coinsToRemove);
        assertEquals(10-coinsToRemove, p.getCoins());
    }

    @Test
    public void removeCoins_RemoveTooManyCoins_ShouldThrowException() {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(GameException.class, () -> p.removeCoins(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    public void removeCoins_InvalidParameter_ShouldThrowException(int coinsToRemove) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(GameException.class, () -> p.removeCoins(coinsToRemove));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddProfessor(StudentColor color) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        p.addProfessor(color);
        assertTrue(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void addProfessor_ProfessorAlreadyOwned_NothingShouldChange(StudentColor color) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        p.addProfessor(color);
        p.addProfessor(color);
        assertTrue(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testRemoveProfessor(StudentColor color) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        p.addProfessor(color);
        p.removeProfessor(color);
        assertFalse(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void removeProfessor_ProfessorNotOwned_NothingShouldChange(StudentColor color) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        p.removeProfessor(color);
        assertFalse(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddStudentToEntrance(StudentColor color) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        int oldNumOfStudents = p.getEntranceStudents().get(color);
        p.addStudentToEntrance(color);
        assertEquals(oldNumOfStudents+1, p.getEntranceStudents().get(color));
    }
    
    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void addStudentToEntrance_TooManyStudents_ShouldThrowException(StudentColor color) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        for (int i = 0; i < p.getMaxEntranceStudents(); i++) {
            p.addStudentToEntrance(color);
        }
        assertThrows(GameException.class, () -> p.addStudentToEntrance(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testRemoveStudentFromEntrance(StudentColor color) {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        p.addStudentToEntrance(color);
        p.removeStudentFromEntrance(color);
        assertEquals(0, p.getEntranceStudents().get(color));

    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void removeStudentFromEntrance_NotEnoughStudents_ShouldThrowException(StudentColor color){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(GameException.class, () -> p.removeStudentFromEntrance(color));
    }

    @Test
    public void testUseAssistant(){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        Assistant a = new Assistant(1,1,Wizard.WIZARD1);
        assertEquals(a, p.useAssistant(a));
        assertEquals(a, p.getUsedAssistant());
    }

    @Test
    public void useAssistant_AssistantAlreadyUsed_ShouldThrowException(){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        Assistant a = new Assistant(1,1,Wizard.WIZARD1);
        p.useAssistant(a);
        assertThrows(GameException.class, () -> p.useAssistant(a));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddToDiningTable(StudentColor color){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        int oldNumOfStudents = p.getDiningTables().get(color).getNumOfStudents();
        try {
            p.addToDiningTable(color);
        } catch (FullTableException e) {
            fail();
        }
        assertEquals(oldNumOfStudents+1, p.getDiningTables().get(color).getNumOfStudents());

    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void addToDiningTable_TooManyStudents_ShouldThrowException(StudentColor color){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        for (int i = 0; i < 10; i++) {
            try{
                p.addToDiningTable(color);
            } catch (FullTableException e) {
                fail();
            }
        }
        assertThrows(FullTableException.class, () -> p.addToDiningTable(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testRemoveFromDiningTable(StudentColor color){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        int expectedRes = p.getDiningTables().get(color).getNumOfStudents();
        try {
            p.addToDiningTable(color);
        } catch (FullTableException e) {
            fail();
        }
        try {
            p.removeFromDiningTable(color);
        } catch (EmptyTableException e) {
            fail();
        }
        assertEquals(expectedRes, p.getDiningTables().get(color).getNumOfStudents());
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void removeStudentFromDiningTable_NotEnoughStudents_ShouldThrowException(StudentColor color){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(EmptyTableException.class, () -> p.removeFromDiningTable(color));
    }

    @ParameterizedTest
    @EnumSource(value = Color.class, names = {"VOID"}, mode = EnumSource.Mode.EXCLUDE)
    public void testColor(Color color){
        Player p = new Player(color, Wizard.WIZARD1, 3);
        assertEquals(color, p.getColor());
    }

    @Test
    public void testNickname(){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        String nick = "Cla";
        p.setNickName(nick);
        assertEquals(nick, p.getNickName());
    }

    @Test
    public void changeNickName_ShouldBeImpossible(){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        String trueNickname = "Cla";
        p.setNickName(trueNickname);
        p.setNickName("Dodo");
        assertEquals(trueNickname, p.getNickName());
    }



}