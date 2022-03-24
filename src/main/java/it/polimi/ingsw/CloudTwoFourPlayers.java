package it.polimi.ingsw;

public class CloudTwoFourPlayers extends Cloud {
    public void addStudents(StudentColors student) throws FullCloudException {
        if (getStudents().size() >= 3) throw new FullCloudException();
        else super.students.add(student);
    }
}
