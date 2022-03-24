package it.polimi.ingsw;

import java.util.ArrayList;

public class CloudThreePlayers extends Cloud {

    public CloudThreePlayers() {
        this.students = new ArrayList<>();
    }

    public void addStudents(StudentColors student) throws FullCloudException {
        if (getStudents().size() >= 4) throw new FullCloudException();
        else super.students.add(student);
    }
}
