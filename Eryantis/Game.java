package Eryantis;

import java.util.HashMap;

public class Game {

    /**
     * @param motherNature index of the island with the mother nature.
     */
    Game(int motherNature) {
        initializeIslands();
        _spareCoins = 20;
        _motherNature = motherNature;
        _numIslands = 12;
    }
    /** Initialize 12 empty islands.
     */
    private void initializeIslands() {
        for(int i = 0; i < 12; i++) {
            islands.put(i, new Island());
        }
    }
    /** In the case that x > y (ex. x = 11, y = 1), use the yth island to merge the xth island.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    private void mergeIslands(int x, int y) {
        if (x > y) {
            islands.get(y).copyIsland(x);
        }
        else {
            islands.get(x).copyIsland(y);
        }
        _numIslands--;

    }


    private int getSpareCoins() {
        return this._spareCoins;
    }

    private Island getMotherNature() {
        return islands.get(_motherNature);
    }

    private void moveMotherNature(int x) {
        return;
    }

    private Player getCurrentPlayer() {
        return this._currentPlayer;
    }

    private void setCurrentPlayer(Player player) {
        return;
    }


    private Player getWinner() {
        return this._winner;
    }

    private HashMap<Integer, Island> islands;

    private int _spareCoins;

    private int _motherNature;

    private Player _currentPlayer;

    private Player _winner;

    private int _numIslands;



}
