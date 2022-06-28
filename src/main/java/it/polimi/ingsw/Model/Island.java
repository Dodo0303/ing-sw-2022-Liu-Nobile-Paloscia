package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.GameException;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class for the islands
 */
public class Island implements Serializable {
    /** The color of tower(s) on THIS island. */
    private Color _towerColor;

    /** Number of no-entry tiles in the island */
    private int _noEntries;

    /** Represents how many times this island has merged other islands into itself.
     * The total number of islands merged is _numMerge+1
     */
    private int _numMerge;

    /** Number of towers on this island. */
    private int _numTower;

    /** Students on this island counted by color. */
    private HashMap<StudentColor, Integer> _students;

    Island() {
        _towerColor = Color.VOID;
        _noEntries = 0;
        _numMerge = 0; //how many times THIS island has merged other islands into itself.
        _numTower = 0;
        _students = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            _students.put(color, 0);
        }
    }

    /** Copy information of island to this island.
     * @param toCopy index of the island.
     */
    public void copyFrom(Island toCopy) {
        if (this._towerColor != Color.VOID && this._towerColor == toCopy._towerColor) {
            _numMerge = this._numMerge + toCopy.getNumMerge() + 1;
            _numTower += toCopy._numTower;
            this._noEntries += toCopy.getNoEntries();
            for (StudentColor color : toCopy.getStudents().keySet()) {
                this._students.put(color, toCopy.getStudents().get(color) + this._students.get(color));
            }
        } else {
            throw new GameException("You cannot unify islands with different tower colors.");
        }

    }
    /** Add a student to this island.
     * @param color color of the student to be added to this island.
     */
    public void addStudent(StudentColor color) {
        _students.put(color, _students.get(color) + 1);
    }

    /**
     * Add a no-entry tile to this island
     */
    public void addNoEntry(){
        _noEntries++;
    }

    /**
     * Remove a no-entry tile from this island
     */
    public void removeNoEntry(){
        if (_noEntries > 0)
            _noEntries--;
    }

    /** Set color of the tower(s) on this island.
     * If the island doesn't have a tower, it set towerNum to 1
     */
    public void setTowerColor(Color color) {
        if (_numTower == 0)
            _numTower++;
        _towerColor = color;
    }

    /**
     *
     * @return the color of the tower(s) on this island
     */
    public Color getTowerColor() {
        return this._towerColor;
    }

    /**
     *
     * @return the amount of no-entry tiles on this island
     */
    public int getNoEntries() {
        return this._noEntries;
    }

    /**
     *
     * @return how many times this island has been merged with other islands.
     */
    public int getNumMerge() {
        return this._numMerge;
    }

    /**
     *
     * @return the amount of towers on this island.
     */
    public int getNumTower() {
        return this._numTower;
    }

    /**
     *
     * @return the students on this island
     */
    public HashMap<StudentColor, Integer> getStudents() {
        return new HashMap<>(_students);
    }

    /**
     * Set the number of tower for this island.
     * This method is used for test purposes
     * @param numTower number of tower to be set
     */
    protected void setNumTower(int numTower) {
        this._numTower = numTower;
    }

}
