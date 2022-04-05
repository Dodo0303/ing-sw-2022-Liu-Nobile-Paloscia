package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class DiningTableTest {

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void startWithZeroStudents(StudentColor color) {
        DiningTable table = new DiningTable(color);
        assertEquals(0, table.getNumOfStudents());
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void checkColor(StudentColor color) {
        DiningTable table = new DiningTable(color);
        assertEquals(color, table.getColor());
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void addStudents_AddTooManyStudents_ShouldThrowException(StudentColor color) {
        DiningTable table = new DiningTable(color);
        for (int i = 0; i < 10; i++) {
            try{
                table.addStudent();
            } catch (FullTableException e){
                fail();
            }
        }

        assertThrows(FullTableException.class, table::addStudent);
    }


    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void testAddStudent(StudentColor color) {
        DiningTable table = new DiningTable(color);
        for (int i = 0; i < 10; i++) {
            try{
                table.addStudent();
                assertEquals(i+1, table.getNumOfStudents());
            } catch (FullTableException e){
                fail();
            }
        }
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void claimCoinTest(StudentColor color) {
        DiningTable table = new DiningTable(color);
        for (int i = 0; i < 10; i++) {
            try{
                table.addStudent();
                if (i%3 == 2){
                    assertTrue(table.claimCoin());
                } else {
                    assertFalse(table.claimCoin());
                }
                assertFalse(table.claimCoin());
            } catch (FullTableException e) {
                fail();
            }
        }
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void removeStudentTest(StudentColor color) {
        DiningTable table = new DiningTable(color);
        try {
            table.addStudent();
        } catch (FullTableException e) {
            fail();
        }
        assertEquals(1, table.getNumOfStudents());
        try {
            table.removeStudent();
        } catch (EmptyTableException e) {
            fail();
        }
        assertEquals(0, table.getNumOfStudents());
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void removeStudent_removeTooManyStudents_ShouldThrowException(StudentColor color) {
        DiningTable table = new DiningTable(color);
        assertThrows(EmptyTableException.class, table::removeStudent);
    }

    @ParameterizedTest
    @EnumSource(StudentColor.class)
    public void coinsTest_AddAndRemoveStudentOnACoin_ShouldGiveCoinTwice(StudentColor color){
        DiningTable table = new DiningTable(color);
        for (int i = 0; i < 3; i++) {
            try{
                table.addStudent();
            } catch (FullTableException e) {
                fail();
            }
        }
        assertTrue(table.claimCoin());
        try{
            table.removeStudent();
        } catch (EmptyTableException e) {
            fail();
        }
        assertFalse(table.claimCoin());
        try{
            table.addStudent();
        } catch (FullTableException e){
            fail();
        }
        assertTrue(table.claimCoin());
    }


}