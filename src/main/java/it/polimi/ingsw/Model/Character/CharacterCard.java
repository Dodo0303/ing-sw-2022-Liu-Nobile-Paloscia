package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract class for the characters
 */
public abstract class CharacterCard implements Serializable {
    /**
     * ID of the character
     */
    private int _ID;
    /**
     * Current price of the character
     */
    protected int _price;
    /**
     * Tells whether the character has been already used or not
     */
    protected boolean used = false;
    /**
     * Students on the character card, if the character allows them
     */
    protected List<StudentColor> students;
    /**
     * Number of no-entry tiles, if the character allows them
     */
    protected int noEntries = 0;

    public CharacterCard(int ID, int initialPrice){
        _ID = ID;
        _price = initialPrice;
    }

    public int getID() {
        return _ID;
    }

    public int getPrice() {
        return _price;
    }

    /**
     * Add a new-entry tile if the character allows it
     * @throws WrongEffectException the character doesn't expect to have no-entry tiles
     */
    public abstract void addNoEntries() throws WrongEffectException;

    /**
     * Activates the effect of the character and increments currentPrice by 1
     * @throws WrongEffectException if the character needs to manage students
     * @throws NotEnoughNoEntriesException if the character doesn't have any No Entry tile
     */
    public void useEffect() throws WrongEffectException, NotEnoughNoEntriesException {
        if (!used) {
            _price++;
            used = true;
        }
    }

    /**
     * Method called when the character needs to manage students
     * @param studentIndex index of the student that has to be extracted from the card
     * @param studentToAdd student that has to be added to the card
     * @return the students extracted
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

    /**
     *
     * @return whether the card has already been used or not
     */
    public boolean isUsed() {
        return used;
    }

}
