package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Model.WrongEffectException;

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
    public void addNoEntries() throws WrongEffectException{
        throw new WrongEffectException();
    }

    @Override
    public void useEffect() throws WrongEffectException {
        throw new WrongEffectException();
    }

    @Override
    public StudentColor useEffect(int studentIndex, StudentColor studentToAdd) throws WrongEffectException {
        StudentColor result = students.get(studentIndex);
        students.set(studentIndex, studentToAdd);
        if (!used) {
            _price++;
            used = true;
        }
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
