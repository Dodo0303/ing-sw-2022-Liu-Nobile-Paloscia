package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.List;

public class Character12 extends Character{

    public Character12() {
        super(12, 3);
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
}
