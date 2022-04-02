package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Model.WrongEffectException;

import java.util.List;

public class Character10 extends Character{

    public Character10() {
        super(10, 1);
    }

    @Override
    public void addNoEntries() throws WrongEffectException{
        throw new WrongEffectException();
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
        throw new WrongEffectException();
    }
}
