package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    //The test covers also the method isEmpty()
    @Test
    public void extractionStudent_extractEveryStudent_ShouldThrowException() {
        Bag bag = new Bag();
        int i;
        StudentColor student;
        for (i = 0; i < 120; i++) {
            try {
                student = bag.extractStudent();
            } catch (EmptyBagException e) {
                fail();
            }
        }

        assertThrows(EmptyBagException.class, () -> {
            StudentColor test = bag.extractStudent();
        });

    }

    @Test
    public void startWith120Students() {
        Bag bag = new Bag();
        int numOfStudents = bag.getRemainingNum();
        assertEquals(120, numOfStudents);
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void addStudents_addTooManyStudents_ShouldThrowException(StudentColor color) {
        Bag bag = new Bag();
        try {
            bag.addStudent(color);
            bag.addStudent(color);
        } catch (Exception e) {
            fail();
        }

        assertThrows(TooManyStudentsException.class, () -> {
            bag.addStudent(color);
        });
    }
}