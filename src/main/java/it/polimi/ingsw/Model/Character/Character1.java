package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Model.WrongEffectException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Character1 extends Character{

    public Character1(StudentColor[] students) {
        super(1, 1);
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
