package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.NotEnoughNoEntriesException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Model.WrongEffectException;

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
    public List<StudentColor> useEffect(List<Integer> studentIndex, List<StudentColor> studentToAdd) throws WrongEffectException {
        throw new WrongEffectException();
    }

    @Override
    public List<StudentColor> getStudents() throws WrongEffectException {
        throw new WrongEffectException();
    }

    @Override
    public int getNumberOfNoEntries() throws WrongEffectException {
        return noEntries;
    }
}
