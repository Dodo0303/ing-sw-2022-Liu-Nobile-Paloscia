package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Take 1 student from this card and place it on an Island of your choice.
 * Then, draw a new student from the bag and place it on this card.
 */
public class Character1 extends CharacterCard {

    public Character1(StudentColor[] students) {
        super(1, 1);
        super.students = new ArrayList<>();
        Collections.addAll(super.students, students);
    }

    @Override
    public void addNoEntries() throws WrongEffectException{
        throw new WrongEffectException("This card can't have no-entries");
    }

    @Override
    public void useEffect() throws WrongEffectException {
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
    public List<StudentColor> getStudents(){

        return new ArrayList<>(students);
    }

    @Override
    public int getNumberOfNoEntries() throws WrongEffectException {
        throw new WrongEffectException("This card can't have no-entries");
    }

    @Override
    public String getDescription() {
        return "Take 1 student from this card and place it on an Island of your choice.\n" +
                "\tThen, draw a new student from the bag and place it on this card.";
    }


}
