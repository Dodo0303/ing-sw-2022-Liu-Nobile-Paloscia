package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.NotEnoughNoEntriesException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Model.WrongEffectException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Character7 extends Character{

    public Character7(StudentColor[] students) {
        super(7, 1);
        super.students = new ArrayList<>();
        Collections.addAll(super.students, students);
    }

    @Override
    public void addNoEntries() throws WrongEffectException{
        throw new WrongEffectException();
    }

    @Override
    public void useEffect() throws WrongEffectException, NotEnoughNoEntriesException {
        throw new WrongEffectException();
    }

    @Override
    public List<StudentColor> useEffect(List<Integer> studentIndexes, List<StudentColor> studentsToAdd) throws WrongEffectException {
        if (studentIndexes.size() > 3 || studentsToAdd.size() > 3 || studentsToAdd.size() != studentIndexes.size())
            throw new WrongEffectException();
        List<StudentColor> result = new ArrayList<>();
        for (Integer index :
                studentIndexes) {
            result.add(students.get(index));
            // Substitute the student in position 'index' with the last student of studentsToAdd
            students.set(index, studentsToAdd.remove(studentsToAdd.size()-1));
        }
        _currentPrice++;
        return result;
    }

    @Override
    public List<StudentColor> getStudents() throws WrongEffectException {
        return new ArrayList<>(students);
    }

    @Override
    public int getNumberOfNoEntries() throws WrongEffectException {
        throw new WrongEffectException();
    }
}
