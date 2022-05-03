package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.util.List;

public class Character5 extends Character{

    public Character5() {
        super(5, 2);
        noEntries = 4;
    }

    @Override
    public void useEffect() throws WrongEffectException, NotEnoughNoEntriesException {
        if (noEntries > 0) {
            super.useEffect();
            noEntries--;
        } else {
            throw new NotEnoughNoEntriesException();
        }
    }

    public void addNoEntries() throws WrongEffectException{
        if (noEntries < 4)
            noEntries++;
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
        return noEntries;
    }
}
