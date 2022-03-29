package it.polimi.ingsw.Model;

import java.util.List;

public class Character5 extends Character{

    public Character5() {
        super(5, 2);
        noEntries = 4;
    }

    //TODO Manage the case of adding a no-entry tile
    @Override
    public void useEffect() throws WrongEffectException, NotEnoughNoEntriesException {
        if (noEntries > 0) {
            super.useEffect();
            noEntries--;
        } else {
            throw new NotEnoughNoEntriesException();
        }
    }

    @Override
    public StudentColor useEffect(int studentIndex, StudentColor studentToAdd) throws WrongEffectException {
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
