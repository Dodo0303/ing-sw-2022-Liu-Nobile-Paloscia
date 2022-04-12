package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @ParameterizedTest
    @ValueSource( ints = {2,4})
    public void createCloudTwoFourPlayers(int players) {
        try {
            Cloud c = new Cloud(players);
            assertEquals(3, c.getFullLength());
            assertTrue(c.isEmpty());
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void createCloudTwoFourPlayers() {
        try {
            Cloud c = new Cloud(3);
            assertEquals(4, c.getFullLength());
            assertTrue(c.isEmpty());
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    public void createCloud_InvalidNumberOfPlayers_ShouldThrowException(int players){
        assertThrows(IllegalArgumentException.class, () -> new Cloud(players));
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4})
    public void createCloud_CheckStudents(int players) {
        Cloud c = new Cloud(players);
        List<StudentColor> students = c.getStudents();
        assertTrue(students.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddStudent(StudentColor color) {
        Cloud c = new Cloud(2);
        try {
            c.addStudent(color);
        } catch (FullCloudException e) {
            fail();
        }
        List<StudentColor> students = c.getStudents();
        assertEquals(color, students.get(students.size()-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4})
    public void addStudent_TooManyStudents_ShouldThrowException(int players) {
        Cloud c = new Cloud(players);
        for (int i = 0; i < c.getFullLength(); i++) {
            try {
                c.addStudent(StudentColor.BLUE);
            } catch (FullCloudException e) {
                fail();
            }
        }
        assertThrows(FullCloudException.class, () -> c.addStudent(StudentColor.BLUE));

    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void testExtractStudent(int numOfStudents) {
        Cloud c = new Cloud(2);
        //Add 1 BLUE student
        try {
            c.addStudent(StudentColor.BLUE);
        } catch (FullCloudException e) {
            fail();
        }
        //Add numOfStudents-1 RED students
        for (int i = 0; i < numOfStudents-1; i++) {
            try {
                c.addStudent(StudentColor.RED);
            } catch (FullCloudException e) {
                fail();
            }
        }
        //The students removed should be BLUE, because it's the first one of the list
        try {
            assertEquals(StudentColor.BLUE, c.extractStudent());
        } catch (EmptyCloudException e) {
            fail();
        }
    }



}