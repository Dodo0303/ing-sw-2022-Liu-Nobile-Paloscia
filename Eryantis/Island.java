package Eryantis;
public class Island {


    Island() {
        _towerColor = Colors.VOID;
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

    private Colors _towerColor;

    private int _noEntries;

    private int _numMerge;

}
