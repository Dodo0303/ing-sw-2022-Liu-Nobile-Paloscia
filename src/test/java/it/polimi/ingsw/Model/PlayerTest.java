package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyTableException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.GameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @ParameterizedTest
    @ValueSource( ints = {-1, 1, 5})
    public void createPlayers_IllegalNumberOfPlayers_ShouldThrowException(int players) {
        assertThrows(IllegalArgumentException.class, () -> {
            Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, players);
        });
    }

    @ParameterizedTest
    @ValueSource( ints = {2,4})
    public void createPlayer_IllegalColor_ShouldThrowException(int players){
        assertThrows(IllegalArgumentException.class, () -> new Player("Test", Color.GRAY, Wizard.WIZARD1, players));
    }

    @ParameterizedTest
    @ValueSource( ints = {2,3,4})
    public void createPlayer_VoidColor_ShouldThrowException(int players){
        assertThrows(IllegalArgumentException.class, () -> new Player("Test", Color.VOID, Wizard.WIZARD1, players));
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void createPlayer_CheckAssistants(int players){
        Player player = new Player("Test", Color.BLACK, Wizard.WIZARD1, players);
        ArrayList<Assistant> ass = player.getAssistants();
        for (int i = 0; i < 10; i++) {
            assertEquals(i+1, ass.get(i).getValue());
            assertEquals(Wizard.WIZARD1, ass.get(i).getWizard());
            //Using the switch so we don't use any algorithm that could be wrong
            switch (i){
                case 0:
                case 1:
                    assertEquals(1, ass.get(i).getMaxSteps());
                    break;
                case 2:
                case 3:
                    assertEquals(2, ass.get(i).getMaxSteps());
                    break;
                case 4:
                case 5:
                    assertEquals(3, ass.get(i).getMaxSteps());
                    break;
                case 6:
                case 7:
                    assertEquals(4, ass.get(i).getMaxSteps());
                    break;
                case 8:
                case 9:
                    assertEquals(5, ass.get(i).getMaxSteps());
                    break;
            }
        }
    }

    @ParameterizedTest
    @ValueSource( ints = {2,4})
    public void createPlayer_CheckEntranceStudents_TwoFourPlayers(int players) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, players);
        List<StudentColor> students = p.getEntranceStudents();
        assertEquals(0, students.size());
        assertEquals(7, p.getMaxEntranceStudents());

    }

    @Test
    public void createPlayer_CheckEntranceStudents_TwoFourPlayers() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 3);
        List<StudentColor> students = p.getEntranceStudents();
        assertEquals(0, students.size());
        assertEquals(9, p.getMaxEntranceStudents());

    }
    
    @ParameterizedTest
    @ValueSource( ints = {2,3,4})
    public void createPlayer_CheckProfessors(int players) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, players);
        List<StudentColor> professors = p.getProfessors();
        assertTrue(professors.isEmpty());
    }

    @Test
    public void createPlayer_checkName() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        assertEquals("Test", p.getNickName());
    }

    @ParameterizedTest
    @ValueSource( ints = {2,3,4})
    public void createPlayer_CheckDiningTables(int players){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, players);
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
    public void createPlayerWithCaptain_ThreePlayers() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 3, null);
        assertEquals(6, p.getTowerNum());
        assertEquals(6, p.getMaxTowerNum());
        assertEquals(9, p.getMaxEntranceStudents());
    }

    @Test
    public void createPlayerWithCaptain_InvalidArgument_ShouldThrowException(){
        assertThrows(IllegalArgumentException.class, ()->{
            new Player("Test", Color.BLACK, Wizard.WIZARD1, 5, null);
        });
    }

    @Test
    public void testRemoveTower(){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 3);
        p.removeTower();
        assertEquals(p.getMaxTowerNum()-1, p.getTowerNum());
    }

    @Test
    public void removeTower_RemoveTooManyTowers_ShouldThrowException() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        for (int i = 0; i < p.getMaxTowerNum(); i++) {
            p.removeTower();
        }
        assertThrows(GameException.class, p::removeTower);
        assertThrows(GameException.class, ()->{p.removeTower(1);});

    }

    @Test
    public void testAddTower(){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.removeTower(2);
        int towers = p.getTowerNum();
        p.addTower(1);
        assertEquals(towers + 1, p.getTowerNum());
        p.addTower();
        assertEquals(towers+2, p.getTowerNum());

    }

    @Test
    public void testAddTowerWithCaptain() {
        Player captain = new Player("Cap", Color.BLACK, Wizard.WIZARD4, 4, null);
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 4, captain);
        int towers = p.getTowerNum();
        p.addTower(1);
        assertEquals(towers + 1, p.getTowerNum());
        p.addTower();
        assertEquals(towers + 2, p.getTowerNum());

    }

    @Test
    public void removeTowerWithCaptain() {
        Player captain = new Player("Cap", Color.BLACK, Wizard.WIZARD4, 4, null);
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 4, captain);
        p.addTower(p.getMaxTowerNum());
        p.removeTower();
        assertEquals(p.getMaxTowerNum()-1, p.getTowerNum());
        p.removeTower(1);
        assertEquals(p.getMaxTowerNum()-2, p.getTowerNum());
        assertEquals(p.getMaxTowerNum()-2, p.getCaptain().getTowerNum());

    }

    @Test
    public void addTower_TooManyTowers_ShouldThrowException() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 3);
        assertThrows(GameException.class, () -> {p.addTower(1);});
        assertThrows(GameException.class, p::addTower);

    }

    @Test
    public void testAddCoin() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        int initCoins = p.getCoins();
        p.addCoin();
        assertEquals(initCoins+1, p.getCoins());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    public void testRemoveCoins(int coinsToRemove) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        for (int i = 0; i < 10; i++) {
            p.addCoin();
        }
        p.removeCoins(coinsToRemove);
        assertEquals(10-coinsToRemove, p.getCoins());
    }

    @Test
    public void removeCoins_RemoveTooManyCoins_ShouldThrowException() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(GameException.class, () -> p.removeCoins(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    public void removeCoins_InvalidParameter_ShouldThrowException(int coinsToRemove) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(GameException.class, () -> p.removeCoins(coinsToRemove));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddProfessor(StudentColor color) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.addProfessor(color);
        assertTrue(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void addProfessor_ProfessorAlreadyOwned_NothingShouldChange(StudentColor color) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.addProfessor(color);
        p.addProfessor(color);
        assertTrue(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testRemoveProfessor(StudentColor color) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.addProfessor(color);
        p.removeProfessor(color);
        assertFalse(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void removeProfessor_ProfessorNotOwned_NothingShouldChange(StudentColor color) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.removeProfessor(color);
        assertFalse(p.getProfessors().contains(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddStudentToEntrance(StudentColor color) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        int oldNumOfStudents = p.getEntranceStudents().size();
        p.addStudentToEntrance(color);
        assertEquals(oldNumOfStudents+1, p.getEntranceStudents().size());
        assertEquals(color, p.getEntranceStudents().get(oldNumOfStudents));
    }
    
    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void addStudentToEntrance_TooManyStudents_ShouldThrowException(StudentColor color) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        for (int i = 0; i < p.getMaxEntranceStudents(); i++) {
            p.addStudentToEntrance(color);
        }
        assertThrows(GameException.class, () -> p.addStudentToEntrance(color));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testRemoveStudentFromEntrance(StudentColor color) {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.addStudentToEntrance(color);
        p.removeStudentFromEntrance(0);
        assertEquals(0, p.getEntranceStudents().size());

    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void removeStudentFromEntrance_NotEnoughStudents_ShouldThrowException(StudentColor color){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(IndexOutOfBoundsException.class, () -> p.removeStudentFromEntrance(0));
    }

    @Test
    public void testUseAssistant(){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        Assistant a = new Assistant(1,1,Wizard.WIZARD1);
        assertEquals(a, p.useAssistant(a));
        assertEquals(a, p.getUsedAssistant());
    }

    @Test
    public void useAssistant_AssistantAlreadyUsed_ShouldThrowException(){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        Assistant a = new Assistant(1,1,Wizard.WIZARD1);
        p.useAssistant(a);
        assertThrows(GameException.class, () -> p.useAssistant(a));
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddToDiningTable(StudentColor color){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
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
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
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
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
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
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        assertThrows(EmptyTableException.class, () -> p.removeFromDiningTable(color));
    }

    @ParameterizedTest
    @EnumSource(value = Color.class, names = {"VOID"}, mode = EnumSource.Mode.EXCLUDE)
    public void testColor(Color color){
        Player p = new Player("Test", color, Wizard.WIZARD1, 3);
        assertEquals(color, p.getColor());
    }

    @Test
    public void testClearEntrance() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.clearEntrance();
        assertEquals(0, p.getEntranceStudents().size());
    }

    @Test
    public void testEquals() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        Player p1 = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        assertEquals(p, p1);
        assertEquals(p,p);
    }

    @Test
    public void testEquals_NotEquals(){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        Player p1 = new Player("Test1", Color.BLACK, Wizard.WIZARD1, 2);
        assertNotEquals(p, p1);
        assertNotEquals(p, null);
    }

    @Test
    public void testHasProfessor(){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.addProfessor(StudentColor.BLUE);
        assertTrue(p.hasProfessor(StudentColor.BLUE));
    }

    @Test
    public void testHasProfessor_NoProfessor(){
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        for (StudentColor color :
                StudentColor.values()) {
            assertFalse(p.hasProfessor(color));
        }
    }

    @Test
    public void testLastAssistant() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        int index = 0;
        for (int maxSteps = 1; maxSteps <= 5; maxSteps++) {
            for (int j = 0; j < 2; j++) {
                if (maxSteps == 5 && j == 1) {
                    break;
                }
                assertFalse(p.lastAssistant());
                p.useAssistant(new Assistant(++index, maxSteps, Wizard.WIZARD1));
            }
        }
        assertTrue(p.lastAssistant());
    }

    @Test
    public void testSetCoins() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.setCoins(10);
        assertEquals(10, p.getCoins());
    }

    @Test
    public void testSetTowers() {
        Player p = new Player("Test", Color.BLACK, Wizard.WIZARD1, 2);
        p.setTowers(5);
        assertEquals(5, p.getTowerNum());
    }

}