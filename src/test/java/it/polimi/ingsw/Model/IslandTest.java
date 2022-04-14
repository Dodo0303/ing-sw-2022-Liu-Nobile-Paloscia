package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

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
}