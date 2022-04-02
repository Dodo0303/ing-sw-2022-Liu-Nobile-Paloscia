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
    public void addNoEntries() throws WrongEffectException{
        throw new WrongEffectException();
    }

    @Override
    public void useEffect() throws WrongEffectException {
        throw new WrongEffectException();
    }

    @Override
    public List<StudentColor> useEffect(List<Integer> studentIndex, List<StudentColor> studentToAdd) throws WrongEffectException {
        if (studentIndex.size() != 1 || studentToAdd.size() != 1)
            throw new WrongEffectException();
        Integer index = studentIndex.get(0);
        List<StudentColor> result = new ArrayList<>();
        result.add(students.get(index));
        students.set(index, studentToAdd.get(0));
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
