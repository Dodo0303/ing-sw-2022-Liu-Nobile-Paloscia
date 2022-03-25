package it.polimi.ingsw;

public class Island {


    Island() {
        _towerColor = Color.VOID;
        _noEntries = 0;
        _numMerge = 0;

    }

    /** Copy information of island x to this island.
     * @param x index of the island.
     */
    void copyFrom(int x) {
        _numMerge++;
        // and other things....
    }




    private int[] _influences;

    private Color _towerColor;

    private int _noEntries;

    private int _numMerge;

}
