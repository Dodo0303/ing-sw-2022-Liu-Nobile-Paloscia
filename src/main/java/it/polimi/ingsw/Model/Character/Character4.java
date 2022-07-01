package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.List;

/**
 * You may move Mother Nature up to 2 additional islands than is indicated by the assistant card you've played
 */
public class Character4 extends CharacterCard {

    public Character4() {
        super(4, 1);
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
        return "You may move Mother Nature up to 2 additional islands than is indicated by the assistant card you've played";
    }
}
