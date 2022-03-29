package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    private int _ID;
    private int _initialPrice;
    protected int _currentPrice;
    protected List<StudentColor> students;
    protected int noEntries = 0;
    //!students, noEntries and currentPrice are protected because some concrete characters should be able to directly access them, for example in the initializer and in useEffect()

    public Character(int ID, int initialPrice){
        _ID = ID;
        _initialPrice = initialPrice;
        _currentPrice = initialPrice;
    }

    public int getID() {
        return _ID;
    }

    public int getInitialPrice() {
        return _initialPrice;
    }

    public int getCurrentPrice() {
        return _currentPrice;
    }

    /**
     * Activates the effect of the character and increments currentPrice by 1
     * @throws WrongEffectException if the character needs to manage students
     */
    public void useEffect() throws WrongEffectException {
        _currentPrice++;
    }

    /**
     * Method called when the character needs to manage students
     * @param studentIndex student that has to be extracted from the card
     * @param studentToAdd student that has to be added to the card
     * @return the student extracted
     * @throws WrongEffectException if the character doesn't expect students
     */
    public abstract StudentColor useEffect(int studentIndex, StudentColor studentToAdd) throws WrongEffectException;

    /**
     * Could be unused if the card doesn't expect to have students
     * @throws WrongEffectException if the concrete character doesn't expect any student
     * @return ArrayList of the students in the card
     */
    public abstract List<StudentColor> getStudents() throws WrongEffectException;

    /**
     * Could be unused by some cards
     * @throws WrongEffectException if the concrete character doesn't expect any no entry
     * @return the number of no entries in the character
     */
    public abstract int getNumberOfNoEntries() throws WrongEffectException;
}
