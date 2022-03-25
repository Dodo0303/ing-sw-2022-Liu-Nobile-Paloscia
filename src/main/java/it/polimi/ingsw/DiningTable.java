package it.polimi.ingsw;

public class DiningTable {
    private final StudentColors color;
    private int numOfStudents;
    private int availableCoins;

    public DiningTable(StudentColors color) {
        this.color = color;
        this.numOfStudents = 0;
        this.availableCoins = 3;
    }

    /**
     * Pushes a new student on top of the dining table "stack"
     * @throws FullTableException when the dining table is full (10 students)
     */
    public void addStudent() throws FullTableException {
        if (numOfStudents == 10) throw new FullTableException();
        else numOfStudents++;
    }

    /**
     * Checks whether the number of students on the table requires a new coin to be provided to the player
     * @return true if a new coin should be given, false if not
     */
    public Boolean checkCoin() {
        return numOfStudents == 3*(4-availableCoins);
    }

    public StudentColors getColor() {
        return color;
    }

    public int getNumOfStudents() {
        return numOfStudents;
    }

}
