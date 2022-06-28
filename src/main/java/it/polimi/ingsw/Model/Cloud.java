package it.polimi.ingsw.Model;


import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullCloudException;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class for cloud tile.
 */
public class Cloud implements Serializable {
    /**
     * ArrayList of StudentColor that represent the students lying on the cloud tile.
     */
    private ArrayList<StudentColor> students;
    /**
     * Maximum amount of students that this tile can have, based on the number of players.
     */
    private final int fullLength;

    public Cloud(int numberOfPlayers) throws IllegalArgumentException{
        if (numberOfPlayers == 2 || numberOfPlayers == 4) {
            this.fullLength = 3;
        } else if (numberOfPlayers == 3){
            this.fullLength = 4;
        } else {
            throw new IllegalArgumentException();
        }
        this.students = new ArrayList<>(this.fullLength);
    }

    /**
     * @param student the student that needs to be added (extracted from bag).
     * @throws FullCloudException when the cloud is full and no student can be added.
     */

    public void addStudent(StudentColor student) throws FullCloudException {
        if (students.size() ==  fullLength) {
            throw new FullCloudException();
        } else {
            students.add(student);
        }
    }

    /**
     * @return the student that was removed.
     * @throws EmptyCloudException when the cloud is empty and no student can be extracted.
     */
    public StudentColor extractStudent() throws EmptyCloudException {
        if (students.size()==0) throw new EmptyCloudException();
        else return students.remove(0);
    }

    /** This may be of length 3 or 4, depending on the game mode.
     *
     * @return the list of students on the cloud.
     */
    public ArrayList<StudentColor> getStudents() {
        return new ArrayList<>(students);
    }

    /**
     *
     * @return maximum amount of students that this cloud can have.
     */
    public int getFullLength() {
        return fullLength;
    }

    /**
     * Checks whether the cloud is empty or not.
     * @return true if the cloud is empty, false if not.
     */
    public boolean isEmpty() { return students.size()==0; }

}
