package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Character11 extends Character{

    public Character11(StudentColor[] students) {
        super(11, 2);
        super.students = new ArrayList<>();
        Collections.addAll(super.students, students);
    }

    @Override
    public void useEffect() throws WrongEffectException {
        throw new WrongEffectException();
    }

    @Override
    public StudentColor useEffect(int studentIndex, StudentColor studentToAdd){
        StudentColor result = students.get(studentIndex);
        students.set(studentIndex, studentToAdd);
        _currentPrice++;
        return result;

    }

    @Override
    public List<StudentColor> getStudents(){

        return new ArrayList<>(students);
    }

    @Override
    public int getNumberOfNoEntries() throws WrongEffectException {
        throw new WrongEffectException();
    }
}
