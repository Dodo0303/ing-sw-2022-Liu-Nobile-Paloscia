package Eryantis;
public class Island {


    Island() {
        _towerColor = Colors.VOID;
        _noEntries = 0;
        _numMerge = 0;
    }

    void copyIsland(int x) {
        _numMerge++;

    }




    private int[] _influences;

    private Colors _towerColor;

    private int _noEntries;

    private int _numMerge;

}
