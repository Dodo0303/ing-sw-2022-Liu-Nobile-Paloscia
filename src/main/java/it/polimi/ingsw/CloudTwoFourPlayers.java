package it.polimi.ingsw;

import java.util.ArrayList;

public class CloudTwoFourPlayers extends Cloud {

    public CloudTwoFourPlayers() {
        this.students = new ArrayList<>();
    }

    public void addStudent(StudentColors student) throws FullCloudException {
        if (getStudents().size() >= 3) throw new FullCloudException();
        else super.students.add(student);
    }
}
