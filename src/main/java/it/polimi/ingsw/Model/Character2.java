package it.polimi.ingsw.Model;

import java.util.List;

public class Character2 extends Character{

    public Character2() {
        super(2,2);
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
