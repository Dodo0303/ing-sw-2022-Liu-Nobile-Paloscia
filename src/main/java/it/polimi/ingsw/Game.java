package it.polimi.ingsw;

import java.util.HashMap;

public class Game {

    /**
     * @param motherNature index of the island with mother nature.
     */
    Game(int motherNature) {
        initializeIslands();
        _bag = new Bag();
        _spareCoins = 20;
        _motherNature = motherNature;
        _numIslands = 12;

    }
    /** Initialize 12 empty islands.
     */
    private void initializeIslands() {
        islands = new HashMap<>();
        for(int i = 0; i < 12; i++) {
            islands.put(i, new Island());
        }
    }


    /** In the case that x == numIslands - 1(ex. x = 11, y = 0), use the yth island to merge the xth island, just like deleting the tail node of a linked list.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    private void mergeIslands(int x, int y) {
        if (x == _numIslands - 1) {
            islands.get(y).copyFrom(x);
            if (x == _motherNature) {
                _motherNature = y;
            }
        }
        else {
            islands.get(x).copyFrom(y);
            if (y == _motherNature) {
                _motherNature = x;
            }
            for (int i = y; i < _numIslands - 1; i++) {
                islands.put(i, islands.get(i + 1)); // move islands after the yth forward by 1.
            }
        }
        islands.remove(_numIslands--);
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

    private Bag _bag;



}
