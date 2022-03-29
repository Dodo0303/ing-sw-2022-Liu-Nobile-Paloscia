package it.polimi.ingsw.Model;

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
    public void useEffect() throws WrongEffectException, NotEnoughNoEntriesException {
        throw new WrongEffectException();
    }

    //! Il prezzo non deve aumentare di 1 ogni volta! Va fatto un altro metodo? TODO
    @Override
    public StudentColor useEffect(int studentIndex, StudentColor studentToAdd) throws WrongEffectException {
        StudentColor result = students.get(studentIndex);
        students.set(studentIndex, studentToAdd);
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
