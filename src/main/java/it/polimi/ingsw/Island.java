package it.polimi.ingsw;

import java.util.HashMap;

public class Island {


    Island() {
        _towerColor = Color.VOID;
        _noEntries = 0;
        _numMerge = 0;
        students = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            students.put(color, 0);
        }
    }

    /** Copy information of island x to this island.
     * @param x index of the island.
     */
    void copyFrom(int x) {
        _numMerge++;
        // and other things....
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
