package it.polimi.ingsw.Model;

import java.util.HashMap;

public class Island {

    /** A new island.
     */
    Island() {
        _towerColor = Color.VOID;
        _noEntries = 0;
        _numMerge = 0; //how many times THIS island has merged other islands into itself, also equals to the number of towers on THIS island.
        _students = new HashMap<>();
        _influences = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            _students.put(color, 0);
            _influences.put(color, 0);
        }
    }

    /** Copy information of island x to this island.
     * @param x index of the island.
     */
    void copyFrom(Island x) {
        if (this._towerColor == x._towerColor) {
            _numMerge = this._numMerge + x._numMerge + 1;
            for (StudentColor color : x._students.keySet()) {
                this._students.put(color, x._students.get(color) + this._students.get(color));
            }
        } else {
            throw new GameException("You cannot unify islands with different tower colors.");
        }

    }
    /** Add a student of color COLOR to THIS island.
     * @param color the color of the student to be added to THIS island.
     */
    void addStudent(StudentColor color) {
        _students.put(color, _students.get(color) + 1);
    }

    /** Set color of the tower(s) on this island to color COLOR.
     */
    void setTowerColor(Color color) {
        _towerColor = color;
    }


    /** Influence of each color. */
    private HashMap<StudentColor, Integer> _influences;

    /** The color of tower(s) on THIS island. */
    private Color _towerColor;

    /** //TODO */
    private int _noEntries;

    /** Represents how many times THIS island has merged other islands into itself, also equals to the number of towers on THIS island. */
    private int _numMerge;

    /** Students on THIS island counted by color. */
    private HashMap<StudentColor, Integer> _students;

}
