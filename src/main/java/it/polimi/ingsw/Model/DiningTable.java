package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyTableException;
import it.polimi.ingsw.Exceptions.FullTableException;

import java.io.Serializable;

/**
 * Class for the dining tables.
 */
public class DiningTable implements Serializable {

    /**
     * Color of the table.
     */
    private final StudentColor color;
    /**
     * Number of students in this table.
     */
    private int numOfStudents;
    /**
     * Number of coins yet to be taken from this table.
     */
    private int availableCoins;

    public DiningTable(StudentColor color) {
        this.color = color;
        this.numOfStudents = 0;
        this.availableCoins = 3;
    }

    /**
     * Pushes a new student on top of the dining table "stack".
     * @throws FullTableException when the dining table is full (10 students).
     */
    public void addStudent() throws FullTableException {
        if (numOfStudents == 10) throw new FullTableException();
        else numOfStudents++;
    }

    /**
     * Checks whether the number of students on the table requires a new coin to be given to the player.
     * @return true if a new coin should be given, false if not.
     */
    private Boolean checkCoin() {
        return numOfStudents == 3*(4-availableCoins);
    }

    /**
     * Decreases availableCoins, if possible.
     * @return true if a new coin could be claimed, false if not.
     */
    public boolean claimCoin() {
        if (!checkCoin()) return false;
        availableCoins--;
        return true;
    }

    /**
     *
     * @return the color of the table
     */
    public StudentColor getColor() {
        return color;
    }

    /**
     *
     * @return the number of students in this table
     */
    public int getNumOfStudents() {
        return numOfStudents;
    }

    /**
     * Remove a student from the table and adds an availableCoin if needed.
     * @throws EmptyTableException if the table doesn't have any students.
     */
    public void removeStudent() throws EmptyTableException {
        if (numOfStudents == 0) {
            throw new EmptyTableException();
        }
        availableCoins++;
        if (!checkCoin())
            availableCoins--;
        numOfStudents--;
    }

}
