package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    //TODO Check influences when implemented

    @Test
    public void testCreateIsland() {
        Island i = new Island();
        assertEquals(0, i.getNoEntries());
        assertEquals(0, i.getNumMerge());
        assertEquals(0, i.getNumTower());
        assertEquals(Color.VOID, i.getTowerColor());
        Map<StudentColor, Integer> students = i.getStudents();
        for (StudentColor color :
                StudentColor.values()) {
            assertEquals(0, students.get(color));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Color.class, names = "VOID", mode = EnumSource.Mode.EXCLUDE)
    public void testCopyFrom_NoStudents(Color color) {
        Island i1 = new Island();
        Island i2 = new Island();
        i1.setTowerColor(color);
        i2.setTowerColor(color);
        i1.copyFrom(i2);
        Map<StudentColor, Integer> students = i1.getStudents();
        for (StudentColor studentColor :
                StudentColor.values()) {
            assertEquals(0, students.get(studentColor));
        }
        assertEquals(color, i1.getTowerColor());
        assertEquals(1, i1.getNumMerge());
        assertEquals(2, i1.getNumTower());
        assertEquals(0, i1.getNoEntries());
    }

    @ParameterizedTest
    @EnumSource(value = Color.class, names = "VOID", mode = EnumSource.Mode.EXCLUDE)
    public void testCopyFrom_WithStudents(Color color) {
        Island i1 = new Island();
        Island i2 = new Island();
        i1.setTowerColor(color);
        i2.setTowerColor(color);
        i1.addStudent(StudentColor.BLUE);
        i2.addStudent(StudentColor.BLUE);
        i1.copyFrom(i2);
        Map<StudentColor, Integer> students = i1.getStudents();
        for (StudentColor studentColor :
                StudentColor.values()) {
            if (studentColor == StudentColor.BLUE)
                assertEquals(2, students.get(studentColor));
            else
                assertEquals(0, students.get(studentColor));
        }
    }

    @Test
    public void testCopyFrom_VoidTowerColor_ShouldThrowException() {
        Island i1 = new Island();
        Island i2 = new Island();
        assertThrows(GameException.class, () -> i1.copyFrom(i2));
    }

    @ParameterizedTest
    @EnumSource(value = Color.class, names = "VOID", mode = EnumSource.Mode.EXCLUDE)
    public void testCopyFrom_DifferentTowerColor_ShouldThrowException(Color color) {
        Island i1 = new Island();
        Island i2 = new Island();
        i1.setTowerColor(color);
        if (color == Color.BLACK)
            i2.setTowerColor(Color.GRAY);
        else
            i2.setTowerColor(Color.BLACK);
        assertThrows(GameException.class, () -> i1.copyFrom(i2));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2})
    public void testCopyFrom_AddSomeNoEntries(int numTiles) {
        Island i1 = new Island();
        Island i2 = new Island();
        i1.setTowerColor(Color.GRAY);
        i2.setTowerColor(Color.GRAY);
        for (int i = 0; i < numTiles; i++) {
            i1.addNoEntry();
        }
        for (int i = 0; i < numTiles-1; i++) {
            i2.addNoEntry();
        }
        i1.copyFrom(i2);
        assertEquals(numTiles*2-1, i1.getNoEntries());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 10})
    public void testCopyFrom_MergeMultipleIslands_CheckNumMerge(int numOfIslands) {
        List<Island> islands = new ArrayList<>();
        for (int i = 0; i < numOfIslands; i++) {
            islands.add(new Island());
        }
        for (Island island :
                islands) {
            island.setTowerColor(Color.GRAY);
        }
        for (int i = 0; i < numOfIslands - 1; i++) {
            islands.get(0).copyFrom(islands.get(i+1));
        }
        assertEquals(numOfIslands-1, islands.get(0).getNumMerge());
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddStudent(StudentColor color) {
        Island i = new Island();
        i.addStudent(color);
        assertEquals(1, i.getStudents().get(color));
    }

    @Test
    public void testAddNoEntry() {
        Island i = new Island();
        i.addNoEntry();
        assertEquals(1, i.getNoEntries());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,10})
    public void testRemoveNoEntry(int noEntries) {
        Island i = new Island();
        for (int j = 0; j < noEntries; j++) {
            i.addNoEntry();
        }
        for (int j = 0; j < noEntries; j++) {
            i.removeNoEntry();
        }
        assertEquals(0, i.getNoEntries());
    }

    @Test
    public void testRemoveNoEntry_NotEnoughNoEntries_ValueShouldBeZero() {
        Island i = new Island();
        for (int j = 0; j < 5; j++) {
            i.removeNoEntry();
        }
        assertEquals(0, i.getNoEntries());
    }

    @ParameterizedTest
    @EnumSource(value = Color.class, names = "VOID", mode = EnumSource.Mode.EXCLUDE)
    public void testSetTowerColor(Color color){
        Island i = new Island();
        i.setTowerColor(color);
        assertEquals(color, i.getTowerColor());
        assertEquals(1, i.getNumTower());
    }
/*
    @Test
    public void testCalculateInfluence_EmptyIslandVoidTower() {
        Island i = new Island();
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 2);
        for (StudentColor color :
                StudentColor.values()) {
            p.addProfessor(color);
        }
        assertEquals(0, i.calculateInfluence(p));
    }

    @ParameterizedTest
    @EnumSource(value = Color.class, names = "VOID", mode = EnumSource.Mode.EXCLUDE)
    public void testCalculateInfluence_EmptyIslandWithTower(Color color) {
        Island i = new Island();
        i.setTowerColor(color);
        Player p = new Player(color, Wizard.WIZARD1, 3);
        assertEquals(1, i.calculateInfluence(p));
    }

    @Test
    public void testCalculateInfluence_WithStudentsAndTower() {
        Island i = new Island();
        i.setTowerColor(Color.BLACK);
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 3);
        for (StudentColor color :
                StudentColor.values()) {
            i.addStudent(color);
            p.addProfessor(color);
        }
        assertEquals(StudentColor.values().length + 1, i.calculateInfluence(p));
    }
*/
}