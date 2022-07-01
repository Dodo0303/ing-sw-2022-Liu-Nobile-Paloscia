package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.List;

/**
 * Choose an island and resolve the island as if Mother Nature had ended her movement there.
 * Mother Nature will still move and the island where she ends her movement will also be resolved.
 */
public class Character3 extends CharacterCard {

    public Character3() {
        super(3, 3);
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
        return "Choose an island and resolve the island as if Mother Nature had ended her movement there.\n" +
                "\tMother Nature will still move and the island where she ends her movement will also be resolved.";
    }
}
