package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.List;

public class Island {

    /** A new island.
     */
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

    /** Copy information of island x to this island.
     * @param x index of the island.
     */
    public void copyFrom(Island x) {
        if (this._towerColor != Color.VOID && this._towerColor == x._towerColor) {
            _numMerge = this._numMerge + x.getNumMerge() + 1;
            _numTower += x._numTower;
            this._noEntries += x.getNoEntries();
            for (StudentColor color : x.getStudents().keySet()) {
                this._students.put(color, x.getStudents().get(color) + this._students.get(color));
            }
        } else {
            throw new GameException("You cannot unify islands with different tower colors.");
        }

    }
    /** Add a student of color COLOR to THIS island.
     * @param color the color of the student to be added to THIS island.
     */
    public void addStudent(StudentColor color) {
        _students.put(color, _students.get(color) + 1);
    }

    public void addNoEntry(){
        _noEntries++;
    }

    public void removeNoEntry(){
        if (_noEntries > 0)
            _noEntries--;
    }

    /** Set color of the tower(s) on this island to color COLOR.
     * If the island doesn't have a tower, it set towerNum to 1
     */
    public void setTowerColor(Color color) {
        if (_numTower == 0)
            _numTower++;
        _towerColor = color;
    }

    /** Getter of _towercolor. */
    public Color getTowerColor() {
        return this._towerColor;
    }

    /** Getter of _noEntries. */
    public int getNoEntries() {
        return this._noEntries;
    }

    /** Getter of _numMerge. */
    public int getNumMerge() {
        return this._numMerge;
    }

    /** Getter of _numTower. */
    public int getNumTower() {
        return this._numTower;
    }

    /** Getter of _students. */
    public HashMap<StudentColor, Integer> getStudents() {
        return new HashMap<>(_students);
    }

    /**
     * Calculate the influence of a certain player given his professors and color
     * @param player player of which we want to calculate the influence on the island
     * @return the influence the player has on the island
     */
    public int calculateInfluence(Player player) {
        //TODO special cases
        StandardInfluenceCalculator calculator = new StandardInfluenceCalculator();
        return calculator.calculateInfluence(player, this);
    }

    /** The color of tower(s) on THIS island. */
    private Color _towerColor;

    /** Number of no-entry tiles in the island */
    private int _noEntries;

    /** Represents how many times THIS island has merged other islands into itself.
     * The total number of islands merged is _numMerge+1
     * TODO Could be replaced by numTower
     */
    private int _numMerge;

    /** the number of towers on THIS island. */
    private int _numTower;

    /** Students on THIS island counted by color. */
    private HashMap<StudentColor, Integer> _students;

}
