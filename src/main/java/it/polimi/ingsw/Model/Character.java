package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    private int _ID;
    private int _initialPrice;
    private int _currentPrice;
    protected List<StudentColor> students = new ArrayList<>();
    protected int noEntries = 0;
    //!students and noEntries are protected because some concrete characters should be able to directly access them, for example in the initializer and in useEffect()

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
     */
    public abstract void useEffect();

    /**
     * Could be unused by some cards
     * @throws WrongEffectException if the concrete character doesn't expect any student
     * @return ArrayList of the students in the card
     */
    public abstract ArrayList<StudentColor> getStudents() throws WrongEffectException;

    /**
     * Could be unused by some cards
     * @throws WrongEffectException if the concrete character doesn't expect any no entry
     * @return the number of no entries in the character
     */
    public abstract int getNumberOfNoEntries() throws WrongEffectException;
}
