package it.polimi.ingsw.Model;

import java.util.HashMap;

public class Island {


    Island() {
        _towerColor = Color.VOID;
        _noEntries = 0;
        _numMerge = 0; //how many times THIS island has merged other islands into itself, also equals to the number of towers on THIS island.
        students = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            students.put(color, 0);
        }
    }

    /** Copy information of island x to this island.
     * @param x index of the island.
     */
    void copyFrom(Island x) {
        _numMerge = this._numMerge + x._numMerge + 1;
        for (StudentColor color : x.students.keySet()) {
            this.students.put(color, x.students.get(color) + this.students.get(color));
        }

    }

    void addStudent(StudentColor color) {
        students.put(color, students.get(color) + 1);
    }


    private int[] _influences;

    private Color _towerColor;

    private int _noEntries;

    private int _numMerge;

    private HashMap<StudentColor, Integer> students;

}
