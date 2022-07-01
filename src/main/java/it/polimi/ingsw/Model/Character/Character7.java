package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * You may take up to 3 students from this card and replace them with the same number of students from your entrance.
 */
public class Character7 extends CharacterCard {

    public Character7(StudentColor[] students) {
        super(7, 1);
        super.students = new ArrayList<>();
        Collections.addAll(super.students, students);
    }

    @Override
    public void addNoEntries() throws WrongEffectException{
        throw new WrongEffectException("This card can't have no-entries");
    }

    @Override
    public void useEffect() throws WrongEffectException, NotEnoughNoEntriesException {
        throw new WrongEffectException("This card effect needs a student");
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
    public List<StudentColor> getStudents() throws WrongEffectException {
        return new ArrayList<>(students);
    }

    @Override
    public int getNumberOfNoEntries() throws WrongEffectException {
        throw new WrongEffectException("This card can't have no-entries");
    }

    @Override
    public String getDescription() {
        return "You may take up to 3 students from this card and replace them with the same number of students from your entrance.";
    }
}
