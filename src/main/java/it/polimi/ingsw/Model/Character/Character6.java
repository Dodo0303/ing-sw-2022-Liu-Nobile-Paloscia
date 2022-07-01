package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.List;

/**
 * When resolving a conquering on an island, towers do not count towards influence.
 */
public class Character6 extends CharacterCard {

    public Character6() {
        super(6, 3);
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
        return "When resolving a conquering on an island, towers do not count towards influence.";
    }

}
