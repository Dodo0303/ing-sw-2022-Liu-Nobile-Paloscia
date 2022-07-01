package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.List;

/**
 * Choose a color of student: during the influence calculation this turn, that color adds no influence.
 */
public class Character9 extends CharacterCard {

    public Character9() {
        super(9, 3);
    }

    @Override
    public void addNoEntries() throws WrongEffectException{
        throw new WrongEffectException("This card can't have no-entries");
    }

    @Override
    public StudentColor useEffect(int studentIndex, StudentColor studentToAdd) throws WrongEffectException {
        throw new WrongEffectException("This card doesn't expect to have students");
    }

    @Override
    public List<StudentColor> getStudents() throws WrongEffectException {
        throw new WrongEffectException("This card doesn't expect to have students");
    }

    @Override
    public int getNumberOfNoEntries() throws WrongEffectException {
        throw new WrongEffectException("This card can't have no-entries");
    }

    @Override
    public String getDescription() {
        return "Choose a color of student: during the influence calculation this turn, that color adds no influence.";
    }
}
