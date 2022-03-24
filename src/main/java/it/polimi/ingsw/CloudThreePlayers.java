package it.polimi.ingsw;

public class CloudThreePlayers extends Cloud {
    public void addStudents(StudentColors student) throws FullCloudException {
        if (getStudents().size() >= 4) throw new FullCloudException();
        else super.students.add(student);
    }
}
