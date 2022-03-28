package it.polimi.ingsw.Model;


import java.util.ArrayList;


/**
 * Class for cloud tile. It can have a maximum of 3 or 4 students, depending on number of players (given to constructor).
 */
public class Cloud {
    /**
     * ArrayList of StudentColor that represent the students lying on the cloud tile.
     */
    private ArrayList<StudentColor> students;
    private int fullLength;

    public Cloud(int numberOfPlayers) {
        if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
            this.students = new ArrayList<>(4);
            this.fullLength = numberOfPlayers;
        } else {
            //TODO exception
        }
    }

    /**
     * @param student the student that needs to be added (extracted from bag)
     * @throws FullCloudException when the cloud is full and no student can be added
     */

    public void addStudent(StudentColor student) throws FullCloudException {
        if (students.size() ==  fullLength) {
            throw new FullCloudException();
        } else {
            students.add(student);
        }
    }

    /**
     * @return the student that was removed
     * @throws EmptyCloudException when the cloud is empty and no student can be extracted
     */
    public StudentColor extractStudent() throws EmptyCloudException {
        if (students.size()==0) throw new EmptyCloudException();
        else return students.remove(0);
    }

    /** This may be of length 3 or 4, depending on the game mode
     *
     * @return the list of students on the cloud
     */
    public ArrayList<StudentColor> getStudents() {
        return new ArrayList<>(students);
    }

    public boolean isEmpty() { return students.size()==0; }

}
