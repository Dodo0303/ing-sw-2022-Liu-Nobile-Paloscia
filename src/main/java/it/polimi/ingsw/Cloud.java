package it.polimi.ingsw;


import java.util.ArrayList;


/**
 * Abstract class for cloud tile. It has two concrete classes, CloudTwoFourPlayer and CloudThreePlayer, depending on
 * number of players.
 */
public class Cloud {
    /**
     * ArrayList of StudentColor that represent the students lying on the cloud tile
     */
    protected StudentColor[] students;

    public Cloud(int numberOfStudents) {
        if (numberOfStudents == 3) {
            this.students = new StudentColor[3];
        } else if (numberOfStudents == 4) {
            this.students = new StudentColor[4];
        }
    }

    /**
     * @param student the student that needs to be added (extracted from bag)
     * @throws FullCloudException when the cloud is full and no student can be added
     */

    public void addStudent(StudentColor student) throws FullCloudException {

    }

    /**
     * @return the student that was removed
     * @throws EmptyCloudException when the cloud is empty and no student can be extracted
     */
    public StudentColor extractStudent() throws EmptyCloudException {
        if (students.length==0) throw new EmptyCloudException();
        else return students.remove(0);
    }

    /** This may be of length 3 or 4, depending on the game mode
     *
     * @return the list of students on the cloud
     */
    public ArrayList<StudentColor> getStudents() {
        return new ArrayList<>(students);
    }      //REP EXPOSED?

    public boolean isEmpty() { return students.length==0; }

}
